/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui.home

import androidx.compose.animation.Animatable
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.extensions.daysTo
import com.example.androiddevchallenge.model.DailyForecast
import com.example.androiddevchallenge.model.HourlyForecast
import com.example.androiddevchallenge.util.ConfigurationCompat
import com.example.androiddevchallenge.util.foregroundColor
import dev.chrisbanes.accompanist.insets.LocalWindowInsets
import dev.chrisbanes.accompanist.insets.toPaddingValues
import java.text.SimpleDateFormat
import java.util.Calendar

@Composable
fun Home() {
    val viewModel = viewModel(HomeViewModel::class.java)
    val viewState by viewModel.state.collectAsState()

    Surface(Modifier.fillMaxSize()) {
        HomeContent(
            viewState,
            onNextDaySelected = {
                viewModel.onNextDaySelected()
            },
            onPreviousDaySelected = {
                viewModel.onPreviousDaySelected()
            }
        )
    }
}

@Composable
fun HomeContent(
    viewState: HomeViewState,
    onNextDaySelected: () -> Unit,
    onPreviousDaySelected: () -> Unit
) {
    val scrollState = rememberScrollState()

    BoxWithConstraints {
        val height = this.constraints.maxHeight
        val overviewHeight = with(LocalDensity.current) { height / density }

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
        ) {
            WeatherOverview(
                modifier = Modifier.requiredHeight(overviewHeight.dp),
                place = viewState.forecast.place,
                selectedDay = viewState.selectedForecast,
                onPreviousDaySelected = onPreviousDaySelected,
                onNextDaySelected = onNextDaySelected
            )
        }
    }
}

@Composable
fun WeatherOverview(
    modifier: Modifier = Modifier,
    place: String,
    selectedDay: DailyForecast,
    onPreviousDaySelected: () -> Unit,
    onNextDaySelected: () -> Unit
) {
    val currentInsets = LocalWindowInsets.current
    val statusBarPadding = currentInsets.statusBars.toPaddingValues()
    val navigationBarPadding = currentInsets.navigationBars.toPaddingValues()

    val color = remember { Animatable(Color.Gray) }
    val contentColor = color.value.foregroundColor()

    val state = selectedDay.summary

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
                selectedDay = selectedDay,
            )

            WeatherCanvas(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = state,
                date = selectedDay.date,
                onBlueBodySwipedBackward = onPreviousDaySelected,
                onBlueBodySwipedForward = onNextDaySelected
            )

            HourlyForecastList(selectedDay.hours)

            Spacer(modifier = Modifier.padding(navigationBarPadding))
        }
    }
}

@Composable
fun Heading(
    modifier: Modifier = Modifier,
    place: String,
    selectedDay: DailyForecast,
) {
    val date = selectedDay.date
    val daysFromToday = remember(date) {
        val c1 = Calendar.getInstance()
        val c2 = Calendar.getInstance()
        c2.time = date
        c1.daysTo(c2)
    }

    val currentLocale = ConfigurationCompat.getCurrentLocale(LocalContext.current)
    val weekdayFormat = SimpleDateFormat("EEEE", currentLocale)
    val dateFormat = SimpleDateFormat.getDateInstance()

    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        val typography = MaterialTheme.typography

        val dayDescription = when (daysFromToday) {
            0 -> stringResource(id = R.string.today)
            1 -> stringResource(id = R.string.tomorrow)
            in 2..6 -> weekdayFormat.format(date)
            else -> dateFormat.format(date)
        }.capitalize(currentLocale)

        Text("$place • $dayDescription", style = typography.h3)

        val annotatedString = with(AnnotatedString.Builder()) {
            selectedDay.dailyTemperature?.also {
                withStyle(typography.caption.toSpanStyle()) {
                    append(stringResource(id = R.string.day).toUpperCase(currentLocale))
                }
                append(" ")
                append(stringResource(id = R.string.celsius_placeholder, it.toInt()))
                append("   ") // separator
            }
            selectedDay.nightlyTemperature?.also {
                withStyle(typography.caption.toSpanStyle()) {
                    append(stringResource(id = R.string.night).toUpperCase(currentLocale))
                }
                append(" ")
                append(stringResource(id = R.string.celsius_placeholder, it.toInt()))
            }

            toAnnotatedString()
        }

        Text(annotatedString, style = typography.h3)
    }
}

@Composable
fun HourlyForecastList(hourlyForecasts: List<HourlyForecast>) {
    val temperatures = remember(hourlyForecasts) { hourlyForecasts.map { it.temperature } }
    val graphCaptionTextStyle =
        MaterialTheme.typography.caption.copy(color = LocalContentColor.current)

    Crossfade(targetState = hourlyForecasts, animationSpec = tween(durationMillis = 500)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .temperatureGraph(
                    context = LocalContext.current,
                    color = MaterialTheme.colors.primary,
                    values = temperatures,
                    height = 80.dp,
                    cellWidth = 60.dp,
                    textStyle = graphCaptionTextStyle
                )
        ) {
            for (forecast in hourlyForecasts) {
                HourlyForecast(modifier = Modifier.width(60.dp), forecast)
            }
        }
    }
}

@Composable
fun HourlyForecast(modifier: Modifier = Modifier, forecast: HourlyForecast) {
    Column(modifier = modifier.padding(8.dp)) {
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = forecast.state.drawableRes()),
            contentDescription = stringResource(
                id = forecast.state.drawableContentDescription()
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            String.format("%02d:00", forecast.hour),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.caption
        )
    }
}
