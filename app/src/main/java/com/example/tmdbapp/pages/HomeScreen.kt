package com.example.tmdbapp.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.tmdbapp.R
import com.example.tmdbapp.api.HomeViewModel
import com.example.tmdbapp.api.MovieCategory
import com.example.tmdbapp.dataclasses.Movie
import com.example.tmdbapp.ui.theme.TMDBappTheme

@Composable
fun HomeScreen(navController: NavController, vm: HomeViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF14181C))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .background(Color(0xFF032541))
        ) {
            Image(
                painter = painterResource(id = R.drawable.tmdbbanner),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                MovieSection(
                    title = MovieCategory.TOP_RATED.title,
                    movies = vm.topRatedMovies,
                    navController = navController,
                    onTitleClick = {
                        navController.navigate("${Routes.CATEGORY}/${MovieCategory.TOP_RATED.routeName}")
                    }
                )
            }

            item {
                MovieSection(
                    title = MovieCategory.TRENDING.title,
                    movies = vm.trendingMovies,
                    navController = navController,
                    onTitleClick = {
                        navController.navigate("${Routes.CATEGORY}/${MovieCategory.TRENDING.routeName}")
                    }
                )
            }

            item {
                MovieSection(
                    title = MovieCategory.POPULAR.title,
                    movies = vm.popularMovies,
                    navController = navController,
                    onTitleClick = {
                        navController.navigate("${Routes.CATEGORY}/${MovieCategory.POPULAR.routeName}")
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun MovieSection(
    title: String,
    movies: List<Movie>,
    navController: NavController,
    onTitleClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 10.dp)
                .clickable { onTitleClick() }
        )

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            movies.forEach { movie ->
                MoviePoster(
                    movie = movie,
                    modifier = Modifier.size(width = 120.dp, height = 210.dp)
                ) {
                    navController.navigate("${Routes.DETAILS}/${movie.id}")
                }
            }
        }
    }
}

@Composable
fun CategoryMoviesScreen(
    categoryName: String,
    navController: NavController,
    vm: HomeViewModel = viewModel()
) {
    val category = MovieCategory.fromRouteName(categoryName)
    val movies = vm.getMoviesForCategory(category)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF14181C))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .background(Color(0xFF032541)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { navController.popBackStack() }
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(movies) { movie ->
                MoviePoster(movie = movie) {
                    navController.navigate("${Routes.DETAILS}/${movie.id}")
                }
            }
        }
    }
}

@Composable
fun MoviePoster(movie: Movie, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .background(Color(0xFF14181C))
            .padding(4.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(8.dp),
                    clip = false
                )
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(8.dp)
                    )
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.22f)
                            )
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = String.format("%.1f", movie.vote_average),
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Hscreen() {
    TMDBappTheme {
        val navController = rememberNavController()
        HomeScreen(navController)
    }
}
