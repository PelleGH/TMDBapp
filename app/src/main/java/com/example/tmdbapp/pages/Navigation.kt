package com.example.tmdbapp.pages

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

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
        composable(
            route = "${Routes.DETAILS}/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            MovieDetails(movieId, { navController.popBackStack() }
            )
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
