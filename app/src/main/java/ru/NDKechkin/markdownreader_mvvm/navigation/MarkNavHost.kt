package ru.NDKechkin.markdownreader_mvvm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.NDKechkin.markdownreader_mvvm.screens.Add
import ru.NDKechkin.markdownreader_mvvm.screens.Download
import ru.NDKechkin.markdownreader_mvvm.screens.Preview

sealed class NavRoute (val route: String) {
    object Download: NavRoute("Download_screen")
    object Preview: NavRoute("Preview_screen")
    object Add: NavRoute("Add_screen")
}

@Composable
fun MarkNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoute.Download.route) {
        composable(NavRoute.Download.route) { Download(navController) }
        composable(NavRoute.Preview.route) { Preview(navController) }
        composable(NavRoute.Add.route) { Add(navController) }
    }
}