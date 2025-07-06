package ru.NDKechkin.markdownreader_mvvm.Save

import android.content.Context
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object MarkdownFileSaver {

    fun saveMarkdownToDownloads(context: Context, content: String): Boolean {
        return try {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) downloadsDir.mkdirs()

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "Markdown_$timestamp.md"
            val file = File(downloadsDir, fileName)

            FileOutputStream(file).use { fos ->
                fos.write(content.toByteArray())
            }

            Toast.makeText(context, "Файл сохранён: ${file.absolutePath}", Toast.LENGTH_LONG).show()
            true
        } catch (e: Exception) {
            Toast.makeText(context, "Ошибка сохранения файла", Toast.LENGTH_SHORT).show()
            false
        }
    }
}
