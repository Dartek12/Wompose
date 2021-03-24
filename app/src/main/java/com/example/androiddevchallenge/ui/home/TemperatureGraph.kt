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

import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontListFontFamily
import androidx.compose.ui.text.font.ResourceFont
import androidx.compose.ui.unit.Dp
import androidx.core.content.res.ResourcesCompat

fun Modifier.temperatureGraph(
    context: Context,
    color: Color,
    values: List<Float>,
    height: Dp,
    cellWidth: Dp,
    textStyle: TextStyle,
) =
    then(
        TemperatureGraph(
            context = context,
            color = color,
            height = height,
            cellWidth = cellWidth,
            values = values,
            textStyle = textStyle,
        )
    ).padding(top = height)

class TemperatureGraph(
    context: Context,
    private val color: Color,
    private val height: Dp,
    private val cellWidth: Dp,
    private val textStyle: TextStyle,
    private val values: List<Float>
) : DrawModifier {
    private val textPaint = Paint().asFrameworkPaint().also {
        it.color = textStyle.color.toArgb()
        it.isAntiAlias = true
        it.typeface = textStyle.asTypeface(context)
    }
    private val textBounds = Rect()

    override fun ContentDrawScope.draw() {
        textPaint.textSize = textStyle.fontSize.toPx()

        val circleRadius = this.density * 4f
        val strokeWidth = this.density * 2f
        val fontHeight = textPaint.textSize * 2f
        val cellWidthPx = cellWidth.toPx()

        val minY = values.minByOrNull { it }!!
        val maxY = values.maxByOrNull { it }!!
        val diffY = maxY - minY

        val graphWidth = this@draw.size.width
        val graphHeight = this.density * this@TemperatureGraph.height.value

        val calculateX = { index: Int -> cellWidthPx * (index + 0.5f) + 0.5f }
        val calculateY =
            { value: Float -> (1 - (value - minY) / diffY) * (graphHeight - 2 * circleRadius - fontHeight) + circleRadius + fontHeight }

        for (index in values.indices) {
            val value = values[index]
            val p = Offset(calculateX(index), calculateY(value))

            drawCircle(color, center = p, radius = circleRadius)
            if (index < values.size - 1) {
                val np = Offset(calculateX(index + 1), calculateY(values[index + 1]))
                drawLine(color, start = p, end = np, strokeWidth = strokeWidth)
            }

            drawIntoCanvas { canvas ->
                val text = String.format("%d °C", value.toInt())
                val measured = textPaint.measureText(text)
                textPaint.getTextBounds(text, 0, text.length, textBounds)
                canvas.nativeCanvas.drawText(
                    text,
                    0,
                    text.length,
                    p.x - measured / 2f,
                    p.y - textBounds.height(),
                    textPaint
                )
            }
        }
        drawContent()
    }
}

private fun TextStyle.asTypeface(context: Context): Typeface {
    if (fontFamily is FontListFontFamily) {
        val fontList = fontFamily as FontListFontFamily
        val font = fontList.fonts.first { it.weight == this.fontWeight } as ResourceFont

        return ResourcesCompat.getFont(context, font.resId)!!
    }
    return Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
}
