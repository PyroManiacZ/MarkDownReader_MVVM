package ru.NDKechkin.markdownreader_mvvm

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import ru.NDKechkin.markdownreader_mvvm.navigation.MarkNavHost
import ru.NDKechkin.markdownreader_mvvm.ui.theme.MarkDownReader_MVVMTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Устанавливаем цвет статус-бара и иконок
        WindowCompat.setDecorFitsSystemWindows(window, false)
        @Suppress("DEPRECATION")
        window.statusBarColor = Color(0xFFF2F2F2).toArgb() // светло-серый цвет статус-бара
        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = true // тёмные иконки в статус-баре

        setContent {
            MarkDownReader_MVVMTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(WindowInsets.statusBars.asPaddingValues()),
                    backgroundColor = MaterialTheme.colors.background,
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "Markdown Reader") },
                            backgroundColor = Color.Blue,
                            contentColor = Color.White,
                            elevation = 12.dp
                        )
                    },
                    content = { innerPadding ->
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            color = MaterialTheme.colors.background
                        ) {
                            MarkNavHost()
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MarkDownReader_MVVMTheme {
        MarkNavHost()
    }
}
