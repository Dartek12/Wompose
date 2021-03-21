package com.example.androiddevchallenge.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.model.Forecast
import com.example.androiddevchallenge.model.allWeatherStates
import dev.chrisbanes.accompanist.insets.LocalWindowInsets
import dev.chrisbanes.accompanist.insets.toPaddingValues

@Composable
fun Home() {
    val viewModel = viewModel(HomeViewModel::class.java)
    val viewState by viewModel.state.collectAsState()

    Surface(Modifier.fillMaxSize()) {
        HomeContent(viewState.forecast)
    }
}

@Composable
fun HomeContent(forecast: Forecast) {
    val scrollState = rememberScrollState()

    val currentInsets = LocalWindowInsets.current
    val statusBarPadding = currentInsets.statusBars.toPaddingValues()
    val navigationBarPadding = currentInsets.navigationBars.toPaddingValues()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(statusBarPadding)
            .padding(navigationBarPadding)
    ) {

        Summary(modifier = Modifier.fillMaxWidth(), place = forecast.place)

        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(allWeatherStates()) { item ->
                Column {
                    Image(
                        painter = painterResource(id = item.drawableRes()),
                        contentDescription = stringResource(
                            item.drawableContentDescription()
                        )
                    )
                }
            }
        }
    }

    BoxWithConstraints {
        SideEffect {
            Log.d("Wompose", "Contraints max height: ${constraints.maxHeight}")
        }
    }
}

@Composable
fun Summary(modifier: Modifier = Modifier, place: String) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        val typography = MaterialTheme.typography
        Text(place.toUpperCase(), style = typography.h4)
    }
}