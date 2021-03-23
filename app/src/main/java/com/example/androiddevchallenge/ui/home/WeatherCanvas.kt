package com.example.androiddevchallenge.ui.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.round
import androidx.compose.ui.util.fastMap
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.model.WeatherState
import com.example.androiddevchallenge.util.Circle
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sqrt

const val AnimationDuration = 2000
const val CloudHidden = 0f
const val CloudPresented = 1f

@Composable
fun WeatherCanvas(modifier: Modifier = Modifier, state: WeatherState) {
    val sunSize = 96.dp
    val sunSizePx = with(LocalDensity.current) { sunSize.toPx() }
    val cloudWidth = 128.dp
    val cloudAspectRatio = 1.3333f
    val cloudWidthPx = with(LocalDensity.current) { cloudWidth.toPx() }
    val cloudHeightPx = cloudWidthPx / cloudAspectRatio

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

        val cloudDescriptions = remember(canvasWidth, canvasHeight) {
            listOf(
                CloudPositionDescription(
                    shown = Offset(20f, 20f),
                    hidden = Offset(-cloudWidthPx, 20f)
                ),
                CloudPositionDescription(
                    shown = Offset(
                        canvasWidth / 2f,
                        guideline - cloudHeightPx / 3f
                    ), hidden = Offset(canvasWidth.toFloat(), guideline - cloudHeightPx / 3f)
                ),
                CloudPositionDescription(
                    shown = Offset(canvasWidth - cloudWidthPx - 20f, canvasHeight / 2f),
                    hidden = Offset(canvasWidth.toFloat(), canvasHeight / 2f)
                ),
                CloudPositionDescription(
                    shown = Offset(100.0f, canvasHeight * 0.8f - cloudHeightPx),
                    hidden = Offset(-cloudWidthPx, canvasHeight * 0.8f - cloudHeightPx)
                )
            )
        }

        LaunchedEffect(state) {
            launch {
                sunPositionX.animateTo(
                    sunCenterPosition.x,
                    animationSpec = TweenSpec(AnimationDuration)
                )
            }
            launch {
                cloudPosition.animateTo(
                    if(state.hasClouds) CloudPresented else CloudHidden,
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
                val sun = subcompose("sun") {
                    Image(
                        painter = painterResource(R.drawable.ic_sun),
                        contentDescription = "sun",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .requiredWidth(sunSize)
                            .aspectRatio(1.0f)
                    )
                }.fastMap { it.measure(looseConstraints) }.first()

                val clouds = subcompose("cloud-left") {
                    for (i in cloudDescriptions.indices) {
                        Image(
                            painter = painterResource(R.drawable.ic_cloud),
                            contentDescription = "cloud",
                            modifier = Modifier
                                .requiredWidth(cloudWidth)
                                .aspectRatio(cloudAspectRatio)
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

                for (i in cloudDescriptions.indices) {
                    val position = cloudDescriptions[i].lerp(cloudPosition.value)
                    clouds[i].place(position, zIndex = 1f)
                }
            }
        }
    }
}

data class CloudPositionDescription(val hidden: Offset, val shown: Offset) {
    fun lerp(fraction: Float): IntOffset = lerp(hidden.round(), shown.round(), fraction)
}