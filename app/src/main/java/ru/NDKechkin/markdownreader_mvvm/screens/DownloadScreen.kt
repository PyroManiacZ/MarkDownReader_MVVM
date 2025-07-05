package ru.NDKechkin.markdownreader_mvvm.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.NDKechkin.markdownreader_mvvm.VM.MarkdownViewModel
import java.net.HttpURLConnection
import java.net.URL
@Composable
fun DownloadScreen(navController: NavController, viewModel: MarkdownViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var fileName by rememberSaveable { mutableStateOf("Нет выбранного файла") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    var urlInput by rememberSaveable { mutableStateOf("") }
    val markdownText = viewModel.markdownText

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult

        val name = queryFileName(context.contentResolver, uri)
        fileName = name

        if (!name.endsWith(".md", ignoreCase = true)) {
            errorMessage = "Пожалуйста, выберите файл с расширением .md"
            return@rememberLauncherForActivityResult
        }

        coroutineScope.launch {
            val content = loadFileContent(context, uri)
            if (content == null) {
                errorMessage = "Ошибка чтения файла"
                viewModel.updateMarkdown("")
            } else {
                viewModel.updateMarkdown(content)
                errorMessage = null
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Выбранный файл: $fileName")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            fileName = "Нет выбранного файла"
            viewModel.updateMarkdown("")
            errorMessage = null
            launcher.launch("*/*")
        }) {
            Text("Выбрать .md файл")
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = urlInput,
            onValueChange = { urlInput = it },
            label = { Text("Введите URL файла .md") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                loading = true
                errorMessage = null
                coroutineScope.launch {
                    val content = loadUrlContent(urlInput)
                    loading = false
                    if (content == null) {
                        errorMessage = "Ошибка загрузки по URL"
                        viewModel.updateMarkdown("")
                    } else {
                        viewModel.updateMarkdown(content)
                        errorMessage = null
                    }
                }
            },
            enabled = !loading
        ) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
            }
            Text("Загрузить по URL")
        }

        Spacer(modifier = Modifier.height(12.dp))

        errorMessage?.let {
            Text(text = "Ошибка: $it", color = MaterialTheme.colors.error)
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (markdownText.isNotBlank()) {
            Text(text = "Содержимое загружено. Нажмите кнопку ниже, чтобы перейти к просмотру.")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                navController.navigate("Preview_screen") {
                    popUpTo("Download_screen") { inclusive = false }
                    launchSingleTop = true
                }
            }) {
                Text("Перейти к просмотру")
            }
            Button(onClick = {
                navController.navigate("Add_screen")
            }) {
                Text("Редактировать")
            }
        }
    }
}

fun queryFileName(contentResolver: android.content.ContentResolver, uri: Uri): String {
    val cursor = contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
        if (it.moveToFirst() && nameIndex >= 0) {
            return it.getString(nameIndex)
        }
    }
    return "Неизвестный файл"
}

suspend fun loadFileContent(context: android.content.Context, uri: Uri): String? =
    withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openInputStream(uri)?.bufferedReader().use { it?.readText() }
        } catch (e: Exception) {
            null
        }
    }

suspend fun loadUrlContent(urlStr: String): String? = withContext(Dispatchers.IO) {
    try {
        val url = URL(urlStr)
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 5000
        conn.readTimeout = 5000
        conn.inputStream.bufferedReader().use { it.readText() }.also { conn.disconnect() }
    } catch (e: Exception) {
        null
    }
}
