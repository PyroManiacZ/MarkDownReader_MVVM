package ru.NDKechkin.markdownreader_mvvm.Redactor

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun RedactorToolbarSimple(
    text: String,
    selectionStart: Int,
    selectionEnd: Int,
    onTextChange: (String, Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(onClick = {
            val (newText, newCursor) = wrapTextWith(text, selectionStart, selectionEnd, "**")
            onTextChange(newText, newCursor)
        }) {
            Text("B")
        }

        Button(onClick = {
            val (newText, newCursor) = wrapTextWith(text, selectionStart, selectionEnd, "*")
            onTextChange(newText, newCursor)
        }) {
            Text("I")
        }

        Button(onClick = {
            val (newText, newCursor) = wrapTextWith(text, selectionStart, selectionEnd, "~~")
            onTextChange(newText, newCursor)
        }) {
            Text("S")
        }
    }
}

/**
 * Оборачивает выделенный текст (или вставляет в курсор, если выделения нет) в wrapper.
 * Возвращает пару: новый текст + позиция курсора (ставим курсор после добавленного обертки).
 */
fun wrapTextWith(text: String, selectionStart: Int, selectionEnd: Int, wrapper: String): Pair<String, Int> {
    return if (selectionStart == selectionEnd) {
        // Нет выделения — вставляем wrapper в позицию курсора дважды, курсор между ними
        val newText = text.substring(0, selectionStart) + wrapper + wrapper + text.substring(selectionEnd)
        val newCursor = selectionStart + wrapper.length
        Pair(newText, newCursor)
    } else {
        // Есть выделение — оборачиваем выделенный текст в wrapper
        val newText = text.substring(0, selectionStart) + wrapper +
                text.substring(selectionStart, selectionEnd) + wrapper +
                text.substring(selectionEnd)
        val newCursor = selectionEnd + 2 * wrapper.length
        Pair(newText, newCursor)
    }
}
