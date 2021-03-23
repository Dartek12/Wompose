package com.example.androiddevchallenge.ui.home

import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.data.fakeForecast
import com.example.androiddevchallenge.model.Forecast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeViewState(fakeForecast()))

    val state: StateFlow<HomeViewState>
        get() = _state
}

data class HomeViewState(val forecast: Forecast)