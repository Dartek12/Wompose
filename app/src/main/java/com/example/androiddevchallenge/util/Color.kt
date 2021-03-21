package com.example.androiddevchallenge.util

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import kotlin.math.pow

// Based on this SO answer: https://stackoverflow.com/questions/1855884/determine-font-color-based-on-background-color#answer-62303960
@Composable
fun Color.foregroundColor(fallback: Color = if (MaterialTheme.colors.isLight) Color.Black else Color.White): Color {
    val cb = blackContrast(this)
    val cw = whiteContrast(this)
    if (cb >= 7.0 && cw >= 7.0f) {
        return fallback
    }
    return if (cw > cb) Color.White else Color.Black
}

private fun relativeLuminance(color: Color): Float {
    val rRGB = color.red
    val gRGB = color.green
    val bRGB = color.blue

    return 0.2126f * rRGB.luminanceCoefficient() + 0.7152f * gRGB.luminanceCoefficient() + 0.0722f * bRGB.luminanceCoefficient()
}

private fun Float.luminanceCoefficient() =
    if (this <= 0.03928f) this / 12.92f else ((this + 0.055f) / 1.055f).pow(2.4f)

private fun blackContrast(color: Color) = (relativeLuminance(color) + 0.05f) / 0.05f

private fun whiteContrast(color: Color) = (relativeLuminance(color) + 1.05f) / 0.05f