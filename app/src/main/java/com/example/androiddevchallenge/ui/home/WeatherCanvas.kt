package com.example.androiddevchallenge.ui.home

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.util.fastMap
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.util.Circle
import kotlinx.coroutines.selects.selectUnbiased

const val SunHiddenAtLeftSide = 0.0f
const val SunPresented = 0.5f
const val SunHiddenAtRightSide = 1.0f
const val SunAnimationDuration = 3000

@Composable
fun WeatherCanvas(modifier: Modifier = Modifier) {
    val sunPositionX = remember { Animatable(SunHiddenAtLeftSide) }

    LaunchedEffect(true) {
        sunPositionX.animateTo(SunPresented, animationSpec = TweenSpec(SunAnimationDuration))
    }

    SubcomposeLayout(modifier) { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight

        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        layout(layoutWidth, layoutHeight) {
            val halfWidth = layoutWidth / 2.0f
            val sunGuideline = layoutHeight / 4.0f


            val sun = subcompose("sun") {
                Image(
                    painter = painterResource(R.drawable.ic_sun),
                    contentDescription = "sun",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .requiredWidth(96.dp)
                        .aspectRatio(1.0f)
                )
            }.fastMap { it.measure(looseConstraints) }.first()

            val clouds = subcompose("cloud-left") {
                for (i in 0 until 2) {
                    Image(
                        painter = painterResource(R.drawable.ic_cloud),
                        contentDescription = "cloud",
                        modifier = Modifier
                            .requiredWidth(144.dp)
                            .aspectRatio(1.3333f)
                    )
                }
            }.fastMap { it.measure(looseConstraints) }

            val sunZeroPosition = Offset(halfWidth, sunGuideline)
            val sunMinusPosition = Offset(-sun.width.toFloat(), layoutHeight / 2f)
            val sunPlusPosition = Offset(layoutWidth + sun.width.toFloat(), layoutHeight / 2f)

            val sunCircle = Circle.fromPoints(sunMinusPosition, sunZeroPosition, sunPlusPosition)
            val sunX = (sunPlusPosition.x - sunMinusPosition.x) * sunPositionX.value + sunMinusPosition.x
            val (p1, p2) = sunCircle.pointsAt(x = sunX)
            val sunCenter = if(p1.y < p2.y) p1 else p2
            val sunPosition = sunCenter.minus(Offset(sun.width / 2f, sun.height / 2f))

            Log.d("Wompose", "SunX: $sunX")

            sun.place(
                sunPosition.round(),
                zIndex = 0.0f
            )

            clouds[0].place(x = 20, y = 20, zIndex = 1.0f)
            clouds[1].place(
                x = (halfWidth - clouds[1].width / 6.0f).toInt(),
                y = (sunGuideline - clouds[1].height / 3.0f).toInt(),
                zIndex = 2.0f
            )
        }
    }
}