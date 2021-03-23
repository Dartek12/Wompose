package com.example.androiddevchallenge.model

sealed class WeatherState {
    open val hasClouds: Boolean = true
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
    override val hasClouds = false
}
object CloudyNight : WeatherState()
object RainingNight : WeatherState()
object SnowingNight : WeatherState()
object ThunderNight : WeatherState()
object WindyNight : WeatherState()