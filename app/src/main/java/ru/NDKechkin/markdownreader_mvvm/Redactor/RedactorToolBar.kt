package ru.NDKechkin.markdownreader_mvvm.Redactor

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

        Button(onClick = {
            val (newText, newCursor) = applyHeading(text, selectionStart, selectionEnd)
            onTextChange(newText, newCursor)
        }) {
            Text("H")
        }
    }
}

fun wrapTextWith(text: String, selectionStart: Int, selectionEnd: Int, wrapper: String): Pair<String, Int> {
    return if (selectionStart == selectionEnd) {
        val newText = text.substring(0, selectionStart) + wrapper + wrapper + text.substring(selectionEnd)
        val newCursor = selectionStart + wrapper.length
        Pair(newText, newCursor)
    } else {
        val newText = text.substring(0, selectionStart) + wrapper +
                text.substring(selectionStart, selectionEnd) + wrapper +
                text.substring(selectionEnd)
        val newCursor = selectionEnd + 2 * wrapper.length
        Pair(newText, newCursor)
    }
}

fun applyHeading(text: String, selectionStart: Int, selectionEnd: Int): Pair<String, Int> {
    val before = text.substring(0, selectionStart)
    val selected = text.substring(selectionStart, selectionEnd)
    val after = text.substring(selectionEnd)

    return if (selected.isNotBlank()) {
        val transformed = "#$selected"
        val newText = before + transformed + after
        newText to (before.length + transformed.length)
    } else {
        val transformed = "#"
        val newText = before + transformed + after
        newText to (before.length + transformed.length)
    }
}
