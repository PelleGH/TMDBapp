package com.example.tmdbapp.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tmdbapp.api.DetailsViewModel
import com.example.tmdbapp.dataclasses.Cast
import com.example.tmdbapp.dataclasses.MovieDetailsResponse
import com.example.tmdbapp.ui.theme.TMDBappTheme
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext

@Composable
fun MovieDetails(
    movieId: Int,
    onBackClick: () -> Unit,
    vm: DetailsViewModel = viewModel()
) {
    LaunchedEffect(movieId) {
        vm.loadMovie(movieId)
    }

    val movie = vm.movie
    val directors = vm.directors
    val cast = vm.cast

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF14181C))
    ) {
        if (movie == null) {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                TopBar(onBackClick)
            }
        } else {
            DetailsContent(
                movie = movie,
                directors = directors,
                cast = cast
            )

            TopBar(onBackClick)
        }
    }
}

@Composable
fun BackdropSection(movie: MovieDetailsResponse) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w780${movie.backdrop_path}",
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.15f),
                            Color.Black.copy(alpha = 0.35f),
                            Color(0xFF14181C)
                        )
                    )
                )
        )
    }
}

@Composable
fun DetailsContent(
    movie: MovieDetailsResponse,
    directors: List<String>,
    cast: List<Cast>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        BackdropSection(movie)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFF14181C),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = movie.title,
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (directors.isNotEmpty()) {
                        Text(
                            text = "Directed by: ${directors.joinToString()}",
                            color = Color(0xFF90CEA1),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Released ${movie.release_date}",
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Runtime: ${movie.runtime} min",
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Column {
                        Text(
                            text = "${String.format("%.1f", movie.vote_average)} / 10",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${movie.vote_count} ratings",
                            color = Color(0xFFB8C0C8),
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))
                Column() {
                    if (movie.poster_path != null) {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w342${movie.poster_path}",
                            contentDescription = movie.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(180.dp)
                                .aspectRatio(2f / 3f)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                    val context = LocalContext.current

                    if (movie.imdb_id != null) {
                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .clickable {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://www.imdb.com/title/${movie.imdb_id}")
                                    )
                                    context.startActivity(intent)
                                },
                            color = Color(0xFFF5C518),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "IMDb",
                                color = Color.Black,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = movie.overview,
                color = Color(0xFFB8C0C8),
                fontSize = 15.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

//            Text(
//                text = "Credits",
//                color = Color.White,
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            if (directors.isNotEmpty()) {
//                Text(
//                    text = "Director",
//                    color = Color(0xFF90CEA1),
//                    fontSize = 13.sp,
//                    fontWeight = FontWeight.SemiBold
//                )
//
//                Text(
//                    text = directors.joinToString(),
//                    color = Color(0xFFB8C0C8),
//                    fontSize = 15.sp
//                )
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Cast",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            cast.take(10).forEach { actor ->
                Text(
                    text = actor.name,
                    color = Color(0xFFB8C0C8),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
@Composable
fun TopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Go back",
                tint = Color(144,206,161,255)
            )
        }
    }
}

@Preview
@Composable
fun Dscreen() {
    TMDBappTheme {
        val dir = listOf("Sean Banan", "Edward Blom")
        val cast = listOf(
            Cast(
                id = "1",
                name = "Ryan Gosling",
                character = "Literally Me"
            )
        )

        DetailsContent(
            movie = MovieDetailsResponse(
                id = 1,
                title = "Movie 1",
                overview = "overview here",
                backdrop_path = null,
                poster_path = null,
                release_date = "2026-05-100",
                runtime = 120,
                vote_average = 8.2,
                vote_count = 1,
                imdb_id = null,
            ),
            directors = dir,
            cast = cast
        )
    }
}