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
package com.example.androiddevchallenge.util

import androidx.compose.ui.geometry.Offset
import kotlin.math.pow
import kotlin.math.sqrt

data class Circle(val center: Offset, val radius: Float) {
    fun pointsAt(x: Float): Pair<Offset, Offset> {
        val a = 1
        val b = -2 * center.y
        val c = -(radius.pow(2f) - center.y.pow(2f) - x.pow(2f) + 2 * x * center.x - center.x.pow(2f))

        val delta = b.pow(2f) - 4 * a * c
        val y1 = (-b - sqrt(delta)) / (2 * a)
        val y2 = (-b + sqrt(delta)) / (2 * a)

        return Pair(Offset(x, y1), Offset(x, y2))
    }

    companion object {
        fun fromPoints(p1: Offset, p2: Offset, p3: Offset): Circle {
            val x12 = p1.x - p2.x
            val x13 = p1.x - p3.x

            val y12 = p1.y - p2.y
            val y13 = p1.y - p3.y

            val y31 = p3.y - p1.y
            val y21 = p2.y - p1.y

            val x31 = p3.x - p1.x
            val x21 = p2.x - p1.x

            val sx13 = p1.x.pow(2f) - p3.x.pow(2f)
            val sy13 = p1.y.pow(2f) - p3.y.pow(2f)
            val sx21 = p2.x.pow(2f) - p1.x.pow(2f)
            val sy21 = p2.y.pow(2f) - p1.y.pow(2f)

            val f = (
                (sx13 * x12 + sy13 * x12 + sx21 * x13 + sy21 * x13) /
                    (2 * (y31 * x12 - y21 * x13))
                )
            val g = (
                (sx13 * y12 + sy13 * y12 + sx21 * y13 + sy21 * y13) /
                    (2 * (x31 * y12 - x21 * y13))
                )
            val c = -p1.x.pow(2f) - p1.y.pow(2f) - 2 * g * p1.x - 2 * f * p1.y

            val h = -g
            val k = -f
            val rSqr = h * h + k * k - c
            return Circle(Offset(h, k), sqrt(rSqr))
        }
    }
}
