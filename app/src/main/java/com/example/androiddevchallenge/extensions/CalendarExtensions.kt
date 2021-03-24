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
package com.example.androiddevchallenge.extensions

import java.util.Calendar

fun Calendar.daysTo(other: Calendar): Int {
    val yearsDiff = other.get(Calendar.YEAR) - get(Calendar.YEAR)
    if (yearsDiff == 0) {
        return other.get(Calendar.DAY_OF_YEAR) - get(Calendar.DAY_OF_YEAR)
    }
    return yearsDiff * 365 + other.get(Calendar.DAY_OF_YEAR) - get(Calendar.DAY_OF_YEAR)
}
