package ru.NDKechkin.markdownreader_mvvm.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
                // Обновляем ViewModel только при сохранении, а не на каждый ввод
                viewModel.updateMarkdown(textFieldValue.text)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить и вернуться к просмотру")
        }
    }
}
