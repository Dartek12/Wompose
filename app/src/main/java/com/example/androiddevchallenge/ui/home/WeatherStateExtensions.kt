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
package com.example.androiddevchallenge.ui.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.model.Cloudy
import com.example.androiddevchallenge.model.CloudyAndSunny
import com.example.androiddevchallenge.model.CloudyNight
import com.example.androiddevchallenge.model.Night
import com.example.androiddevchallenge.model.Raining
import com.example.androiddevchallenge.model.RainingAndSunny
import com.example.androiddevchallenge.model.RainingNight
import com.example.androiddevchallenge.model.Snowing
import com.example.androiddevchallenge.model.SnowingAndSunny
import com.example.androiddevchallenge.model.SnowingNight
import com.example.androiddevchallenge.model.Sunny
import com.example.androiddevchallenge.model.Thunder
import com.example.androiddevchallenge.model.ThunderAndSunny
import com.example.androiddevchallenge.model.ThunderNight
import com.example.androiddevchallenge.model.WeatherState
import com.example.androiddevchallenge.model.Windy
import com.example.androiddevchallenge.model.WindyAndSunny
import com.example.androiddevchallenge.model.WindyNight

@DrawableRes
fun WeatherState.drawableRes(): Int {
    return when (this) {
        Cloudy -> R.drawable.cloudy
        CloudyAndSunny -> R.drawable.cloudy_and_sunny
        CloudyNight -> R.drawable.cloudy_night
        Night -> R.drawable.night
        Raining -> R.drawable.raining
        RainingAndSunny -> R.drawable.raining_and_sunny
        RainingNight -> R.drawable.raining_night
        Snowing -> R.drawable.snowing
        SnowingAndSunny -> R.drawable.snowing_and_sunny
        SnowingNight -> R.drawable.snowing_night
        Sunny -> R.drawable.sunny
        Thunder -> R.drawable.thunder
        ThunderAndSunny -> R.drawable.thunder // TODO
        ThunderNight -> R.drawable.thunder_night
        Windy -> R.drawable.windy
        WindyAndSunny -> R.drawable.windy_and_sunny
        WindyNight -> R.drawable.windy_night
    }
}

fun WeatherState.color(): Color {
    val nightColor = Color(0xff0c1445)
    return when (this) {
        Cloudy -> Color(0xffc2d5dc)
        CloudyAndSunny -> Color(0xffc2d5dc)
        Raining -> Color(0xffafc3cc)
        RainingAndSunny -> Color(0xffafc3cc)
        Snowing -> Color(0xff83a8c3)
        SnowingAndSunny -> Color(0xff83a8c3)
        Sunny -> Color(0xff69d0ff)
        Thunder -> Color(0xff493855)
        ThunderAndSunny -> Color(0xff493855)
        Windy -> Color(0xffafc3cc)
        WindyAndSunny -> Color(0xffafc3cc)
        RainingNight -> nightColor
        SnowingNight -> nightColor
        CloudyNight -> nightColor
        Night -> nightColor
        WindyNight -> nightColor
        ThunderNight -> Color(0xff2f2436)
    }
}

@StringRes
fun WeatherState.drawableContentDescription(): Int {
    return when (this) {
        Cloudy -> R.string.cloudy
        CloudyAndSunny -> R.string.cloudyAndSunny
        CloudyNight -> R.string.cloudyNight
        Night -> R.string.night
        Raining -> R.string.raining
        RainingAndSunny -> R.string.rainingAndSunny
        RainingNight -> R.string.rainingNight
        Snowing -> R.string.snowing
        SnowingAndSunny -> R.string.snowingAndSunny
        SnowingNight -> R.string.snowingNight
        Sunny -> R.string.sunny
        Thunder -> R.string.thunder
        ThunderAndSunny -> R.string.thunderAndSunny
        ThunderNight -> R.string.thunderNight
        Windy -> R.string.windy
        WindyAndSunny -> R.string.windyAndSunny
        WindyNight -> R.string.windyNight
    }
}
