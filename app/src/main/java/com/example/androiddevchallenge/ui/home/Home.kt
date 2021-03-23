package com.example.androiddevchallenge.ui.home

import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.model.Forecast
import com.example.androiddevchallenge.model.HourlyForecast
import com.example.androiddevchallenge.util.foregroundColor
import dev.chrisbanes.accompanist.insets.LocalWindowInsets
import dev.chrisbanes.accompanist.insets.toPaddingValues
import java.time.format.TextStyle

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

    BoxWithConstraints {
        val height = this.constraints.maxHeight
        val overviewHeight = with(LocalDensity.current) { height / density }
        val currentInsets = LocalWindowInsets.current
        val navigationBarPadding = currentInsets.navigationBars.toPaddingValues()

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
        ) {
            WeatherOverview(modifier = Modifier.requiredHeight(overviewHeight.dp), forecast)

            Text("Scroll to see me!")

            Spacer(modifier = Modifier.padding(navigationBarPadding))
        }
    }
}

@Composable
fun WeatherOverview(modifier: Modifier = Modifier, forecast: Forecast) {
    val currentInsets = LocalWindowInsets.current
    val statusBarPadding = currentInsets.statusBars.toPaddingValues()
    val navigationBarPadding = currentInsets.navigationBars.toPaddingValues()

    val state = forecast.selectedDay.state
    val place = forecast.place

    val color = remember { Animatable(Color.Gray) }
    val contentColor = color.value.foregroundColor()

    LaunchedEffect(state.color()) {
        color.animateTo(state.color(), animationSpec = tween(1000, easing = FastOutSlowInEasing))
    }

    CompositionLocalProvider(LocalContentColor provides contentColor) {

        Column(modifier.background(color.value)) {
            Heading(
                Modifier.padding(
                    bottom = 32.dp,
                    top = 32.dp + statusBarPadding.calculateTopPadding()
                ),
                place = place,
            )

            WeatherCanvas(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = forecast.selectedDay.state,
            )

            HourlyForecastList(forecast.selectedDay.hours)

            Spacer(modifier = Modifier.padding(navigationBarPadding))
        }
    }
}

@Composable
fun Heading(modifier: Modifier = Modifier, place: String) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        val typography = MaterialTheme.typography
        Text(place.toUpperCase(), style = typography.h1)
    }
}

@Composable
fun HourlyForecastList(hourlyForecasts: List<HourlyForecast>) {
    val temperatures = remember(hourlyForecasts) { hourlyForecasts.map { it.temperature } }
    val graphCaptionTextStyle = MaterialTheme.typography.caption.copy(color = LocalContentColor.current)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .temperatureGraph(
                context = LocalContext.current,
                color = MaterialTheme.colors.primary,
                values = temperatures,
                size = 80.dp,
                textStyle = graphCaptionTextStyle
            )
    ) {
        for (index in hourlyForecasts.indices) {
            HourlyForecast(hourlyForecasts[index], index)
        }
    }
}

@Composable
fun HourlyForecast(forecast: HourlyForecast, hour: Int) {
    Column(modifier = Modifier.padding(8.dp)) {
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = forecast.state.drawableRes()),
            contentDescription = stringResource(
                id = forecast.state.drawableContentDescription()
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(String.format("%02d:00", hour), style = MaterialTheme.typography.caption)
    }
}
