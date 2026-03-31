package com.example.tmdbapp.pages

import android.graphics.Paint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.tmdbapp.ui.theme.TMDBappTheme
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tmdbapp.api.HomeViewModel
import com.example.tmdbapp.dataclasses.Movie
import com.example.tmdbapp.R
@Composable
fun HomeScreen(navController: NavController, vm: HomeViewModel = viewModel()) {
    // val movies = remember { sampleMovies } // replace with API later
    Column() {
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
        MovieGrid(vm.movies)
    }

}

@Composable
fun MovieGrid(movies: List<Movie>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3)
    ) {
        items(movies) { movie ->
            MoviePoster(movie)
        }
    }
}



val sampleMovies = listOf(
    Movie(1, "Movie 1", poster_path = null,"action", 2.0),
    Movie(2, "Movie 2", poster_path = null,"romance", 4.0),
    Movie(3, "Movie 3", poster_path = null,"thriller", 1.0),
    Movie(4, "Movie 4", poster_path = null,"comedy", 7.0),
)

@Composable
fun MoviePoster(movie: Movie) {
    Column(
        modifier = Modifier
            .background(Color(0xFF14181C))
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(8.dp),
                    clip = false
                )
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(8.dp)
                    )
            )

            Box( // edge gradient
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
            text = "${movie.vote_average}",
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