package com.example.androiddevchallenge.extensions

import java.util.Calendar

fun Calendar.daysTo(other: Calendar): Int {
    val yearsDiff = other.get(Calendar.YEAR) - get(Calendar.YEAR)
    if(yearsDiff == 0) {
        return other.get(Calendar.DAY_OF_YEAR) - get(Calendar.DAY_OF_YEAR)
    }
    return yearsDiff * 365 + other.get(Calendar.DAY_OF_YEAR) - get(Calendar.DAY_OF_YEAR)
}