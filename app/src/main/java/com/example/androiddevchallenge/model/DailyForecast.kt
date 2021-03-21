package com.example.androiddevchallenge.model

import java.util.Date

data class DailyForecast(val date: Date, val hours: List<HourlyForecast>)