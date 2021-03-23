package com.example.androiddevchallenge.data

import com.example.androiddevchallenge.model.Cloudy
import com.example.androiddevchallenge.model.CloudyAndSunny
import com.example.androiddevchallenge.model.DailyForecast
import com.example.androiddevchallenge.model.Forecast
import com.example.androiddevchallenge.model.HourlyForecast
import com.example.androiddevchallenge.model.Night
import com.example.androiddevchallenge.model.Raining
import com.example.androiddevchallenge.model.Sunny
import java.util.Calendar
import kotlin.math.abs
import kotlin.math.roundToInt

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

    for (i in 0 until 24) {
        val temperature = 20f - abs(i - 12f) * 1.5f
        var state = if(i in 7..20) Sunny else Night

        when(i) {
            12 -> state = CloudyAndSunny
            13 -> state = Cloudy
            14 -> state = Raining
        }

        val hourlyForecast =
            HourlyForecast(temperature.roundToInt().toFloat(), state)
        forecast.add(hourlyForecast)
    }
    return forecast
}