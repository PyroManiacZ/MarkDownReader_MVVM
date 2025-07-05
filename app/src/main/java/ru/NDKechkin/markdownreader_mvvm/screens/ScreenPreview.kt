package ru.NDKechkin.markdownreader_mvvm.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.NDKechkin.markdownreader_mvvm.Formatter.MarkdownRender
import ru.NDKechkin.markdownreader_mvvm.Formatter.parseMarkdown
import ru.NDKechkin.markdownreader_mvvm.VM.MarkdownViewModel
@Composable
fun ScreenPreview(
    navController: NavController,
    viewModel: MarkdownViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val markdownContent = viewModel.markdownText

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = {
            navController.navigate("Add_screen")
        }) {
            Text("Редактировать")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Назад к загрузке")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (markdownContent.isBlank()) {
            Text("Нет содержимого для отображения")
        } else {
            val blocks = remember(markdownContent) { parseMarkdown(markdownContent) }
            MarkdownRender(blocks)
        }
    }
}
