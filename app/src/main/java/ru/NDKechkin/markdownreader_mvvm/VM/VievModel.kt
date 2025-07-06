package ru.NDKechkin.markdownreader_mvvm.VM

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MarkdownViewModel : ViewModel() {

    var markdownText by mutableStateOf("")
        private set

    fun updateMarkdown(text: String) {
        markdownText = text
    }
}


