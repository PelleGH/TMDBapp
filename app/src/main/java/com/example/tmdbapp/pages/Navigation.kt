package com.example.tmdbapp.pages

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(navController)
        }
        composable(Routes.DETAILS) {
            MovieDetails(navController)
        }

        composable(Routes.REVIEWS) {
            Reviews(navController)
        }
    }
}

object Routes {
    const val HOME = "home"
    const val DETAILS = "details"
    const val REVIEWS = "reviews"
}
