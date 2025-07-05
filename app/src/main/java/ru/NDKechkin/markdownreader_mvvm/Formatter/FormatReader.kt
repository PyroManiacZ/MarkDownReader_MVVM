package ru.NDKechkin.markdownreader_mvvm.Formatter

sealed class MarkdownBlock {
    data class Heading(val level: Int, val text: String): MarkdownBlock()
    data class Paragraph(val inlines: List<MarkdownInline>): MarkdownBlock()
    data class Table(val headers: List<String>, val rows: List<List<String>>): MarkdownBlock()
    data class Image(val alt: String, val url: String): MarkdownBlock()
}

sealed class MarkdownInline {
    data class Text(val text: String): MarkdownInline()
    data class Bold(val content: List<MarkdownInline>): MarkdownInline()
    data class Italic(val content: List<MarkdownInline>): MarkdownInline()
    data class StrikeThrough(val content: List<MarkdownInline>): MarkdownInline()
}

fun parseMarkdown(text: String): List<MarkdownBlock> {
    val lines = text.lines()
    val blocks = mutableListOf<MarkdownBlock>()
    var i = 0

    while (i < lines.size) {
        val line = lines[i]

        // Проверка заголовка
        val headingMatch = Regex("^(#{1,6})\\s+(.*)").find(line)
        if (headingMatch != null) {
            val level = headingMatch.groupValues[1].length
            val content = headingMatch.groupValues[2]
            blocks.add(MarkdownBlock.Heading(level, content))
            i++  // Обязательно увеличиваем индекс
            continue
        }

        // Проверка изображения
        val imageMatch = Regex("^!\\[(.*?)]\\((.*?)\\)").find(line)
        if (imageMatch != null) {
            val alt = imageMatch.groupValues[1]
            val url = imageMatch.groupValues[2]
            blocks.add(MarkdownBlock.Image(alt, url))
            i++  // Обязательно увеличиваем индекс
            continue
        }

        // Проверка таблицы (многострочный блок)
        if (line.trim().startsWith("|")) {
            val tableLines = mutableListOf<String>()
            // Собираем все строки таблицы подряд
            while (i < lines.size && lines[i].trim().startsWith("|")) {
                tableLines.add(lines[i].trim())
                i++  // Увеличиваем i здесь — чтобы выйти из внешнего while, не нужно делать i++ ниже
            }

            if (tableLines.size >= 2) {
                // первая строка — заголовки, вторая — разделители (---)
                val headers = tableLines[0].split("|").map { it.trim() }.filter { it.isNotEmpty() }
                val separatorLine = tableLines[1]
                val validSeparator = Regex("^\\|?\\s*(:?-+:?\\s*\\|\\s*)+:?-+:?\\s*\\|?\$").matches(separatorLine)
                if (validSeparator) {
                    val rows = tableLines.drop(2).map { row ->
                        row.split("|").map { it.trim() }.filter { it.isNotEmpty() }
                    }
                    blocks.add(MarkdownBlock.Table(headers, rows))
                } else {
                    // Если разделители отсутствуют или неправильные, считаем это не таблицей
                    // Добавляем все строки по отдельности как параграфы
                    tableLines.forEach { l ->
                        blocks.add(MarkdownBlock.Paragraph(parseInlines(l)))
                    }
                }
            } else {
                // Если строк меньше двух — добавляем их как параграфы
                tableLines.forEach { l ->
                    blocks.add(MarkdownBlock.Paragraph(parseInlines(l)))
                }
            }
            // Здесь не нужно i++ — мы уже дошли до следующей строки за таблицей
            continue
        }

        // Если строка не пустая — добавляем параграф
        if (line.isNotBlank()) {
            blocks.add(MarkdownBlock.Paragraph(parseInlines(line)))
            i++
            continue
        }

        // Пустая строка — просто увеличиваем индекс
        i++
    }

    return blocks
}

fun parseInlines(text: String): List<MarkdownInline> {
    val result = mutableListOf<MarkdownInline>()
    var i = 0

    while (i < text.length) {
        when {
            text.startsWith("**", i) -> {
                val end = text.indexOf("**", i + 2)
                if (end != -1) {
                    val inner = text.substring(i + 2, end)
                    result.add(MarkdownInline.Bold(parseInlines(inner)))
                    i = end + 2
                } else {
                    result.add(MarkdownInline.Text(text.substring(i)))
                    break
                }
            }
            text.startsWith("*", i) -> {
                val end = text.indexOf("*", i + 1)
                if (end != -1) {
                    val inner = text.substring(i + 1, end)
                    result.add(MarkdownInline.Italic(parseInlines(inner)))
                    i = end + 1
                } else {
                    result.add(MarkdownInline.Text(text.substring(i)))
                    break
                }
            }
            text.startsWith("~~", i) -> {
                val end = text.indexOf("~~", i + 2)
                if (end != -1) {
                    val inner = text.substring(i + 2, end)
                    result.add(MarkdownInline.StrikeThrough(parseInlines(inner)))
                    i = end + 2
                } else {
                    result.add(MarkdownInline.Text(text.substring(i)))
                    break
                }
            }
            else -> {
                val nextSpecial = listOf(
                    text.indexOf("**", i),
                    text.indexOf("*", i),
                    text.indexOf("~~", i)
                ).filter { it != -1 }.minOrNull() ?: text.length

                val textPart = text.substring(i, nextSpecial)
                result.add(MarkdownInline.Text(textPart))
                // Защита от застревания
                i = if (nextSpecial > i) nextSpecial else i + 1
            }
        }
    }

    return result
}
