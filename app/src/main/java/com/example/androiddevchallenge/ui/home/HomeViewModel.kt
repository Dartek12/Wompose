package com.example.androiddevchallenge.ui.home

import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.model.DailyForecast
import com.example.androiddevchallenge.model.Forecast
import com.example.androiddevchallenge.model.HourlyForecast
import com.example.androiddevchallenge.model.allWeatherStates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeViewState(fakeForecast()))

    val state: StateFlow<HomeViewState>
        get() = _state
}

data class HomeViewState(val forecast: Forecast)

@Deprecated("use other fake data")
fun fakeForecast(): Forecast {
    val todayCalendar = Calendar.getInstance().also {
        it.set(Calendar.HOUR_OF_DAY, 0)
        it.set(Calendar.MINUTE, 0)
        it.set(Calendar.SECOND, 0)
        it.set(Calendar.MILLISECOND, 0)
    }
    val today = todayCalendar.time
    todayCalendar.add(Calendar.DAY_OF_YEAR, 1)
    val tomorrow = todayCalendar.time

    return Forecast(
        place = "Mountain View", days = listOf(
            DailyForecast(today, generateFakeHourlyForecasts()),
            DailyForecast(tomorrow, generateFakeHourlyForecasts()),
        )
    )
}

fun generateFakeHourlyForecasts(): List<HourlyForecast> {
    val forecast = mutableListOf<HourlyForecast>()

    for (i in 0..24) {
        val hourlyForecast = HourlyForecast(1.0, allWeatherStates().random())
        forecast.add(hourlyForecast)
    }
    return forecast
}