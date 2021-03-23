package com.example.androiddevchallenge.data

import com.example.androiddevchallenge.model.Cloudy
import com.example.androiddevchallenge.model.CloudyAndSunny
import com.example.androiddevchallenge.model.CloudyNight
import com.example.androiddevchallenge.model.DailyForecast
import com.example.androiddevchallenge.model.Forecast
import com.example.androiddevchallenge.model.HourlyForecast
import com.example.androiddevchallenge.model.Night
import com.example.androiddevchallenge.model.Raining
import com.example.androiddevchallenge.model.Snowing
import com.example.androiddevchallenge.model.SnowingAndSunny
import com.example.androiddevchallenge.model.Sunny
import java.util.Calendar
import kotlin.math.abs
import kotlin.math.roundToInt

fun fakeForecast(): Forecast {
    val todayCalendar = Calendar.getInstance()
    val todaysHour = todayCalendar.get(Calendar.HOUR_OF_DAY)
    todayCalendar.apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val today = todayCalendar.time
    todayCalendar.add(Calendar.DAY_OF_YEAR, 1)
    val tomorrow = todayCalendar.time
    todayCalendar.add(Calendar.DAY_OF_YEAR, 1)
    val dayAfterTomorrow = todayCalendar.time

    val todaysFourlyForecast = generateFakeHourlyForecasts(from = todaysHour)

    return Forecast(
        place = "Mountain View", days = listOf(
            DailyForecast(today, todaysFourlyForecast, todaysFourlyForecast.first().state),
            DailyForecast(tomorrow, generateFakeHourlyForecasts()),
            DailyForecast(dayAfterTomorrow, generateFakeHourlyForecasts()),
        )
    )
}

fun generateFakeHourlyForecasts(from: Int = 0): List<HourlyForecast> {
    val forecast = mutableListOf<HourlyForecast>()

    for (i in from until 24) {
        val temperature = 20f - abs(i - 12f) * 1.5f
        var state = if (i in 7..20) CloudyAndSunny else CloudyNight

        when (i) {
            12 -> state = CloudyAndSunny
            13 -> state = Cloudy
            14 -> state = Raining
            17 -> state = Sunny
            18 -> state = SnowingAndSunny
        }

        val hourlyForecast =
            HourlyForecast(i, temperature.roundToInt().toFloat(), state)
        forecast.add(hourlyForecast)
    }
    return forecast
}