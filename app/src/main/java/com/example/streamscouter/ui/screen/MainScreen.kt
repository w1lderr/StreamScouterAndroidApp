package com.example.streamscouter.ui.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.streamscouter.R
import com.example.streamscouter.data.model.Movie

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState(MainUiState())
    val movie_description by viewModel.movie_description.collectAsState("")
    val context = LocalContext.current

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
                value = movie_description,
                onValueChange = { viewModel.onMovieDescriptionChanged(it) },
                label = {
                    Text("Describe movie summary")
                }
            )

            Spacer(modifier = Modifier.width(5.dp))

            Button(
                onClick = {
                    viewModel.getMovies()
                    viewModel.setShowResult(true)
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
            if (uiState.showResult) {
                Text(
                    text = "Result:",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            uiState.movies.let {
                LazyColumn(
                    content = {
                        itemsIndexed(it) { _,item ->
                            Log.d("ITEM", item.toString())
                            MovieItem(
                                item = item,
                            )
                        }
                    }
                )
            }
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
            .fillMaxWidth()
            .height(140.dp)
            .padding(20.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = colorResource(R.color.streamscouter_gray)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.title,
                fontSize = 20.sp,
                color = colorResource(R.color.streamscouter_eee)
            )

            Text(
                text = item.match_percentage,
                fontSize = 20.sp,
                color = colorResource(R.color.streamscouter_eee)
            )
        }
    }
}

