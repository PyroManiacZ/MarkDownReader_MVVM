package ru.NDKechkin.markdownreader_mvvm.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import ru.NDKechkin.markdownreader_mvvm.Redactor.RedactorToolbarSimple
import ru.NDKechkin.markdownreader_mvvm.VM.MarkdownViewModel

@Composable
fun ScreenRedactor(
    navController: NavController,
    viewModel: MarkdownViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(text = viewModel.markdownText))
    }

    val view = LocalView.current
    val insets = remember {
        ViewCompat.getRootWindowInsets(view)?.getInsets(WindowInsetsCompat.Type.systemBars())
    }
    val bottomPadding = with(LocalDensity.current) { (insets?.bottom ?: 0).toDp() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = bottomPadding + 16.dp)
    ) {
        RedactorToolbarSimple(
            text = textFieldValue.text,
            selectionStart = textFieldValue.selection.start,
            selectionEnd = textFieldValue.selection.end,
            onTextChange = { newText, newCursor ->
                textFieldValue = TextFieldValue(
                    text = newText,
                    selection = TextRange(newCursor, newCursor)
                )
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                textFieldValue = newValue
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            label = { Text("Редактировать Markdown") },
            singleLine = false,
            maxLines = Int.MAX_VALUE,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.updateMarkdown(textFieldValue.text)
                navController.navigate("Preview_screen") {
                    popUpTo("Add_screen") { inclusive = true }
                    launchSingleTop = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить и вернуться к просмотру")
        }
    }
}
