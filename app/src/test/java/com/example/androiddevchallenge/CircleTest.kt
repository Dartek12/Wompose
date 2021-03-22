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
package com.example.androiddevchallenge

import androidx.compose.ui.geometry.Offset
import com.example.androiddevchallenge.util.Circle
import org.junit.Assert
import org.junit.Test

class CircleTest {
    @Test
    fun circleCanBeConstructedFromPoints() {
        val (center, radius) = Circle.fromPoints(Offset(-6f, 3f), Offset(-3f, 2f), Offset(0f, 3f))

        Assert.assertEquals(Offset(-3f, 7f), center)
        Assert.assertEquals(5f, radius)
    }

    @Test
    fun circleCanReturnPositionsForX() {
        val circle = Circle.fromPoints(Offset(-6f, 3f), Offset(-3f, 2f), Offset(0f, 3f))

        val (point1, point2) = circle.pointsAt(0f)

        Assert.assertEquals(Offset(0f, 3f), point1)
        Assert.assertEquals(Offset(0f, 11f), point2)
    }
}
