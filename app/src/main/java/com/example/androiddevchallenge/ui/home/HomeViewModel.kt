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

import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.data.fakeForecast
import com.example.androiddevchallenge.model.DailyForecast
import com.example.androiddevchallenge.model.Forecast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    private val _state: MutableStateFlow<HomeViewState>

    init {
        val data = fakeForecast()
        val initial = HomeViewState(data, data.days.first())
        _state = MutableStateFlow(initial)
    }

    val state: StateFlow<HomeViewState>
        get() = _state

    fun onNextDaySelected() {
        val currentDate = _state.value.selectedForecast.date
        val nextDay = _state.value.forecast.days.firstOrNull { it.date > currentDate }
        if (nextDay != null) {
            _state.value = _state.value.copy(selectedForecast = nextDay)
        }
    }

    fun onPreviousDaySelected() {
        val currentDate = _state.value.selectedForecast.date
        val previousDay = _state.value.forecast.days.firstOrNull { it.date < currentDate }
        if (previousDay != null) {
            _state.value = _state.value.copy(selectedForecast = previousDay)
        }
    }
}

data class HomeViewState(val forecast: Forecast, val selectedForecast: DailyForecast)
