package com.example.androiddevchallenge.model

sealed class WeatherState

object Sunny : WeatherState()
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
object Night : WeatherState()
object CloudyNight : WeatherState()
object RainingNight : WeatherState()
object SnowingNight : WeatherState()
object ThunderNight : WeatherState()
object WindyNight : WeatherState()

fun allWeatherStates() = listOf(
    Sunny,
    Cloudy,
    CloudyAndSunny,
    Raining,
    RainingAndSunny,
    Snowing,
    SnowingAndSunny,
    Thunder,
    ThunderAndSunny,
    Windy,
    WindyAndSunny,
    Night,
    CloudyNight,
    RainingNight,
    SnowingNight,
    ThunderNight,
    WindyNight,
)