package ru.NDKechkin.markdownreader_mvvm.Formatter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun MarkdownRender(blocks: List<MarkdownBlock>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(blocks.size) { index ->
            val block = blocks[index]
            when (block) {
                is MarkdownBlock.Heading -> {
                    Text(
                        text = block.text,
                        style = when (block.level) {
                            1 -> MaterialTheme.typography.h4
                            2 -> MaterialTheme.typography.h5
                            3 -> MaterialTheme.typography.h6
                            4 -> MaterialTheme.typography.subtitle1
                            5 -> MaterialTheme.typography.subtitle2
                            else -> MaterialTheme.typography.body2
                        },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                is MarkdownBlock.Paragraph -> {
                    Text(
                        text = buildAnnotatedString { appendInlines(block.inlines) },
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                is MarkdownBlock.Image -> {
                    Text("[Image: ${block.alt}]")
                    Spacer(Modifier.height(8.dp))
                }
                is MarkdownBlock.Table -> {
                    TableView(block.headers, block.rows)
                }
            }
        }
    }
}

fun AnnotatedString.Builder.appendInlines(inlines: List<MarkdownInline>) {
    inlines.forEach { inline ->
        when (inline) {
            is MarkdownInline.Text -> append(inline.text)
            is MarkdownInline.Bold -> withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                appendInlines(inline.content)
            }
            is MarkdownInline.Italic -> withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                appendInlines(inline.content)
            }
            is MarkdownInline.StrikeThrough -> withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                appendInlines(inline.content)
            }
        }
    }
}

@Composable
fun TableView(headers: List<String>, rows: List<List<String>>) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row {
            headers.forEach { header ->
                Text(
                    text = header,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.primary
                )
            }
        }
        rows.forEach { row ->
            Row {
                row.forEach { cell ->
                    Text(
                        text = cell,
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp),
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
}
