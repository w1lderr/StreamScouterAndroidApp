package com.example.streamscouter.ui.screen

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.streamscouter.R
import com.example.streamscouter.data.model.Movie
import java.util.Locale

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState(MainUiState())
    val movie_description by viewModel.movie_description.collectAsState("")
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val data = it.data
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            viewModel.onMovieDescriptionChanged(result?.get(0) ?: "No speech detected.")
        } else {
            Toast.makeText(context, "Speech recognition failed.", Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(uiState.toast) {
        if (uiState.toast.isNotEmpty()) {
            Toast.makeText(context, uiState.toast, Toast.LENGTH_SHORT).show()
            viewModel.setToast("")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Header()

        Spacer(modifier = Modifier.height(120.dp))

        Text(
            text = "Just describe your\n\nmovie/serial etc",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.streamscouter_gray)
        )

        Spacer(modifier = Modifier.height(75.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier.size(width = 280.dp, height = 60.dp),
                value = movie_description,
                onValueChange = { viewModel.onMovieDescriptionChanged(it) },
                label = {
                    Text("Describe movie summary")
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )

            Spacer(modifier = Modifier.width(5.dp))

            Button(
                onClick = {
                    viewModel.getMovies()
                },
                modifier = Modifier.size(height = 60.dp, width = 60.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.streamscouter_gray))
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search"
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            Button(
                onClick = {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Go on then, say something.")
                    launcher.launch(intent)
                },
                modifier = Modifier.size(height = 60.dp, width = 60.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.streamscouter_gray))
            ) {
                Icon(
                    painter = painterResource(R.drawable.mic),
                    contentDescription = "Microphone"
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = uiState.showResult,
                fontSize = 25.sp,
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                content = {
                    items(uiState.movies) {
                        MovieItem(item = it)
                    }
                }
            )
        }
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(R.color.streamscouter_gray)),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Stream Scouter",
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            color = colorResource(R.color.streamscouter_eee),
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Composable
fun MovieItem(
    item: Movie,
) {
    Row(
        modifier = Modifier
            .size(width = 280.dp, height = 370.dp)
            .padding(20.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = colorResource(R.color.streamscouter_gray)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(color = Color.Black)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = RoundedCornerShape(10.dp)),
                    model = item.poster_url,
                    contentDescription = null,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = item.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.streamscouter_eee)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "Match percentage: ${item.match_percentage}",
                    fontSize = 13.sp,
                    color = colorResource(R.color.streamscouter_eee)
                )
            }
        }
    }
}