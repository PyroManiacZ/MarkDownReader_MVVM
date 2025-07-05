package ru.NDKechkin.markdownreader_mvvm.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.NDKechkin.markdownreader_mvvm.VM.MarkdownViewModel
import ru.NDKechkin.markdownreader_mvvm.screens.ScreenRedactor
import ru.NDKechkin.markdownreader_mvvm.screens.DownloadScreen
import ru.NDKechkin.markdownreader_mvvm.screens.ScreenPreview

sealed class NavRoute (val route: String) {
    object Download: NavRoute("Download_screen")
    object Preview: NavRoute("Preview_screen")
    object Add: NavRoute("Add_screen")
}

@Composable
fun MarkNavHost() {
    val navController = rememberNavController()
    val viewModel: MarkdownViewModel = viewModel() // Создаём один раз

    NavHost(navController = navController, startDestination = NavRoute.Download.route) {
        composable(NavRoute.Download.route) {
            DownloadScreen(navController, viewModel)
        }
        composable(NavRoute.Preview.route) {
            ScreenPreview(navController, viewModel)
        }
        composable(NavRoute.Add.route) {
            ScreenRedactor(navController, viewModel)
        }
    }
}
