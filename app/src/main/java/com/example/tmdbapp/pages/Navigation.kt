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
            MovieDetails(
                movieId = movieId,
                onBackClick = { navController.popBackStack() },
                onReviewsClick = { navController.navigate(Routes.REVIEWS) }
            )
        }
        composable(
            route = "${Routes.CATEGORY}/{categoryName}",
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName").orEmpty()
            CategoryMoviesScreen(categoryName = categoryName, navController = navController)
        }
        composable(Routes.REVIEWS) {
            Reviews()
        }
    }
}

object Routes {
    const val HOME = "home"
    const val DETAILS = "details"
    const val REVIEWS = "reviews"
    const val CATEGORY = "category"
}
