package com.example.androiddevchallenge.ui.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.round
import androidx.compose.ui.util.fastMap
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.util.Circle
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sqrt

const val AnimationDuration = 3000
const val CloudPresented = 1f

@Composable
fun WeatherCanvas(modifier: Modifier = Modifier) {
    val sunSize = 96.dp
    val sunSizePx = with(LocalDensity.current) { sunSize.toPx() }

    BoxWithConstraints(modifier) {
        val canvasWidth = constraints.maxWidth
        val canvasHeight = constraints.maxHeight
        val guideline = canvasHeight / 4f

        val sunLeftPosition = Offset(-sunSizePx, canvasHeight * 0.7f)
        val sunRightPosition = Offset(canvasWidth + sunSizePx, canvasHeight * 0.7f)
        val sunCenterPosition = Offset(canvasWidth / 2f, guideline)
        val sunCircle = remember(sunLeftPosition, sunRightPosition, sunCenterPosition) {
            Circle.fromPoints(sunLeftPosition, sunCenterPosition, sunRightPosition)
        }

        val sunPositionX = remember { Animatable(sunLeftPosition.x) }
        val cloudPosition = remember { Animatable(0.0f) }

        LaunchedEffect(true) {
            launch {
                sunPositionX.animateTo(
                    sunCenterPosition.x,
                    animationSpec = TweenSpec(AnimationDuration)
                )
            }
            launch {
                cloudPosition.animateTo(
                    CloudPresented,
                    animationSpec = TweenSpec(AnimationDuration)
                )
            }
        }

        SubcomposeLayout(modifier.pointerInput(Unit) {
            val decay = splineBasedDecay<Float>(this)

            coroutineScope {
                while (true) {
                    val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                    val velocityTracker = VelocityTracker()
                    sunPositionX.stop()

                    awaitPointerEventScope {
                        drag(pointerId) { change ->
                            val px = change.positionChange().x
                            val py = change.positionChange().y
                            val changeX = sqrt(px.pow(2f) + py.pow(2f)) * px.sign
                            val targetX = (sunPositionX.value + changeX).coerceIn(
                                sunLeftPosition.x,
                                sunRightPosition.x
                            )
                            launch {
                                sunPositionX.snapTo(
                                    targetX
                                )
                            }
                            velocityTracker.addPosition(
                                change.uptimeMillis,
                                change.position
                            )
                        }
                    }

                    // No longer receiving touch events. Prepare the animation.
                    val velocity = velocityTracker.calculateVelocity().x
                    val targetOffsetX = decay.calculateTargetValue(
                        sunPositionX.value,
                        velocity
                    )

                    launch {
                        when {
                            targetOffsetX.absoluteValue >= size.width -> {
                                sunPositionX.animateTo(
                                    targetValue = sunRightPosition.x,
                                    initialVelocity = velocity
                                )
                            }
                            targetOffsetX.absoluteValue <= 0 -> {
                                sunPositionX.animateTo(
                                    targetValue = sunLeftPosition.x,
                                    initialVelocity = velocity,
                                )
                            }
                            else -> {
                                sunPositionX.animateTo(
                                    targetValue = sunCenterPosition.x,
                                    initialVelocity = velocity
                                )
                            }
                        }
                    }
                }
            }
        }) { constraints ->

            val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

            layout(canvasWidth, canvasHeight) {
                val halfWidth = canvasWidth / 2.0f
                val sunGuideline = canvasHeight / 4.0f

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
                    for (i in 0 until 3) {
                        Image(
                            painter = painterResource(R.drawable.ic_cloud),
                            contentDescription = "cloud",
                            modifier = Modifier
                                .requiredWidth(144.dp)
                                .aspectRatio(1.3333f)
                        )
                    }
                }.fastMap { it.measure(looseConstraints) }


                val (p1, p2) = sunCircle.pointsAt(x = sunPositionX.value)
                val sunCenter = if (p1.y < p2.y) p1 else p2
                val sunPosition = sunCenter.minus(Offset(sun.width / 2f, sun.height / 2f))

                sun.place(
                    sunPosition.round(),
                    zIndex = 0.0f
                )

                val cloud0EndPosition = Offset(20f, 20f).round()
                val cloud0StartPosition = Offset(-clouds[0].width.toFloat(), 20f).round()
                val cloud0Position =
                    lerp(cloud0StartPosition, cloud0EndPosition, cloudPosition.value)

                clouds[0].place(cloud0Position, zIndex = 1f)
                clouds[1].place(
                    Offset(
                        halfWidth - clouds[1].width / 8f,
                        sunGuideline - clouds[1].height / 3f
                    ).round(), zIndex = 1f
                )
                clouds[2].place(
                    Offset(canvasWidth - clouds[2].width - 20f, canvasHeight / 2f).round(),
                    zIndex = 1f
                )
            }
        }
    }
}

