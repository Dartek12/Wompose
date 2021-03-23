package com.example.androiddevchallenge.model

import java.util.Date

data class DailyForecast(
    val date: Date,
    val hours: List<HourlyForecast>,
    private val _summary: WeatherState? = null
) {
    val summary: WeatherState
        get() {
            val dayForecast = hours.map { it.state }.filter { it.day }.groupBy { it }
                .maxByOrNull { it.value.size }?.key
            val nightForecast = hours.map { it.state }.filter { !it.day }.groupBy { it }
                .maxByOrNull { it.value.size }?.key
            return dayForecast ?: nightForecast!!
        }

    val dailyTemperature: Float?
        get() {
            val temperatures = hours.filter { it.state.day }.map { it.temperature }
            if (temperatures.isEmpty()) {
                return null
            }
            return temperatures.average().toFloat()
        }

    val nightlyTemperature: Float?
        get() {
            val temperatures = hours.filter { it.state.night }.map { it.temperature }
            if (temperatures.isEmpty()) {
                return null
            }
            return temperatures.average().toFloat()
        }
}
