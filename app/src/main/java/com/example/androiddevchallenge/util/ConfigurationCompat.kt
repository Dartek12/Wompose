package com.example.androiddevchallenge.util

import android.content.Context
import android.os.Build
import java.util.Locale

object ConfigurationCompat {
    fun getCurrentLocale(context: Context): Locale {
        val configuration = context.resources.configuration
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            configuration.locale
        }
    }
}