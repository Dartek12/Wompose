package com.example.androiddevchallenge.ui.home

import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.model.Forecast
import com.example.androiddevchallenge.model.WeatherState
import com.example.androiddevchallenge.model.allWeatherStates
import com.example.androiddevchallenge.util.foregroundColor
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
    ) {
        Summary(
            modifier = Modifier
                .fillMaxWidth()
                .padding(statusBarPadding),
            place = forecast.place,
            state = forecast.selectedDay.state,
        )

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

        Spacer(modifier = Modifier.padding(navigationBarPadding))
    }
}

@Composable
fun Summary(modifier: Modifier = Modifier, state: WeatherState, place: String) {
    val color = remember { Animatable(Color.Gray) }
    LaunchedEffect(state.color()) {
        color.animateTo(state.color(), animationSpec = tween(1000, easing = FastOutSlowInEasing))
    }

    Column(
        modifier = Modifier
            .background(color.value)
            .then(modifier)
    ) {
        Heading(
            Modifier.padding(bottom = 32.dp, top = 32.dp),
            place = place,
            backgroundColor = color.value
        )

        WeatherCanvas(
            modifier = Modifier
                .requiredHeight(400.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun Heading(modifier: Modifier = Modifier, place: String, backgroundColor: Color) {
    val textColor by animateColorAsState(backgroundColor.foregroundColor())
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        val typography = MaterialTheme.typography
        Text(place.toUpperCase(), style = typography.h5.copy(color = textColor))
    }
}

@Composable
fun WeatherCanvas(modifier: Modifier = Modifier) {

    SubcomposeLayout(modifier) { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight

        Log.d("Wompose", "$layoutWidth $layoutHeight")
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        layout(layoutWidth, layoutHeight) {
            val halfWidth = layoutWidth / 2.0f
            val thirdOfHeight = layoutHeight / 3.0f

            val sun = subcompose("sun") {
                Image(
                    painter = painterResource(R.drawable.ic_sun),
                    contentDescription = "sun",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .requiredWidth(96.dp)
                        .aspectRatio(1.0f)
                )
            }.fastMap { it.measure(looseConstraints) }.first()

            val clouds = subcompose("cloud-left") {
                for (i in 0 until 2) {
                    Image(
                        painter = painterResource(R.drawable.ic_cloud),
                        contentDescription = "cloud",
                        modifier = Modifier
                            .requiredWidth(144.dp)
                            .aspectRatio(1.3333f)
                    )
                }
            }.fastMap { it.measure(looseConstraints) }

            sun.place(
                x = (halfWidth - sun.width / 2.0f).toInt(),
                y = (thirdOfHeight - sun.height / 2.0f).toInt(),
                zIndex = 0.0f
            )
            clouds[0].place(x = 20, y = 20, zIndex = 1.0f)
            clouds[1].place(
                x = (halfWidth - clouds[1].width / 6.0f).toInt(),
                y = (thirdOfHeight - clouds[1].height / 3.0f).toInt(),
                zIndex = 2.0f
            )
        }
    }
}