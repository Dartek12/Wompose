/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
