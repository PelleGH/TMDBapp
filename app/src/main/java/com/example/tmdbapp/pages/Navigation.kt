package com.example.tmdbapp.pages

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tmdbapp.api.AppViewModelFactory
import com.example.tmdbapp.api.DetailsViewModel
import com.example.tmdbapp.api.HomeViewModel

@Composable
fun Navigation(factory: AppViewModelFactory) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            val vm: HomeViewModel = viewModel(factory = factory)
            HomeScreen(navController, vm)
        }

        composable(
            route = "${Routes.DETAILS}/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            val vm: DetailsViewModel = viewModel(factory = factory)

            MovieDetails(
                movieId = movieId,
                onBackClick = { navController.popBackStack() },
                onReviewsClick = { navController.navigate(Routes.REVIEWS) },
                vm = vm
            )
        }

        composable(
            route = "${Routes.CATEGORY}/{categoryName}",
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName").orEmpty()
            val vm: HomeViewModel = viewModel(factory = factory)

            CategoryMoviesScreen(
                categoryName = categoryName,
                navController = navController,
                vm = vm
            )
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