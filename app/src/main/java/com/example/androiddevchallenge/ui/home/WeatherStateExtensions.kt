package com.example.androiddevchallenge.ui.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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