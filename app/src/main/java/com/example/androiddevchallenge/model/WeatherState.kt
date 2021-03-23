package com.example.androiddevchallenge.model

sealed class WeatherState {
    open val day: Boolean = true
    open val hasClouds: Boolean = true
    val night
        get() = !day
}

object Sunny : WeatherState() {
    override val hasClouds = false
}

object Cloudy : WeatherState()
object CloudyAndSunny : WeatherState()
object Raining : WeatherState()
object RainingAndSunny : WeatherState()
object Snowing : WeatherState()
object SnowingAndSunny : WeatherState()
object Thunder : WeatherState()
object ThunderAndSunny : WeatherState()
object Windy : WeatherState()
object WindyAndSunny : WeatherState()
object Night : WeatherState() {
    override val day = false
    override val hasClouds = false
}

object CloudyNight : WeatherState() {
    override val day = false
}

object RainingNight : WeatherState() {
    override val day = false
}

object SnowingNight : WeatherState() {
    override val day = false
}

object ThunderNight : WeatherState() {
    override val day = false
}

object WindyNight : WeatherState() {
    override val day = false
}