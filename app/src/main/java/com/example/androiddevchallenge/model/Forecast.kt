package com.example.androiddevchallenge.model

data class Forecast(val place: String, val days: List<DailyForecast>) {
    val selectedDay = days.first()
}