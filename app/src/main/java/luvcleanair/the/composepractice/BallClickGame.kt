package luvcleanair.the.composepractice

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random

@Preview
@Composable
fun BallClickGame() {
    var points by remember {
        mutableStateOf(0)
    }
    var isTimerRunning by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Points: $points")
            Button(
                onClick = {
                    isTimerRunning = !isTimerRunning
                    points = 0
                }
            ) {
                Text(text = if (isTimerRunning) "Reset" else "start")
            }
            CountdownTimer(isTimerRunning = isTimerRunning) {
                isTimerRunning = false
                points = 0
            }
        }
        BallClicker(
            enabled = isTimerRunning
        ) {
            points++
        }
    }
}

@Composable
private fun CountdownTimer(
    time: Int = 30000,
    isTimerRunning: Boolean,
    onCountdownFinish: () -> Unit
) {
    var currentTime by remember {
        mutableStateOf(time)
    }

    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if (!isTimerRunning){
            currentTime = time
            return@LaunchedEffect
        }

        if (currentTime > 0) {
            delay(1000)
            currentTime -= 1000
        } else {
            onCountdownFinish()
        }
    }

    Text(text = "${currentTime / 1000}")
}

@Composable
private fun BallClicker(
    radius: Float = 100f,
    circleColor: Color = Color.Red,
    enabled: Boolean = false,
    onCircleClick: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {

        var ballPosition by remember {
            mutableStateOf(randomOffset(radius = radius, width = constraints.maxWidth, height = constraints.maxHeight))
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(enabled) {
                    if (!enabled)
                        return@pointerInput

                    detectTapGestures {
                        val distance = sqrt(
                            (it.x - ballPosition.x).pow(2)
                                    + (it.y - ballPosition.y).pow(2)
                        )

                        if (distance <= radius) {
                            ballPosition = randomOffset(
                                radius = radius,
                                width = constraints.maxWidth,
                                height = constraints.maxHeight
                            )
                            onCircleClick()
                        }
                    }
                }
        ) {
            drawCircle(
                radius = radius,
                color = circleColor,
                center = ballPosition
            )
        }
    }
}

private fun randomOffset(radius: Float, width: Int, height: Int): Offset {
    return Offset(
        Random.nextInt(radius.roundToInt(), width - radius.roundToInt()).toFloat(),
        Random.nextInt(radius.roundToInt(), height - radius.roundToInt()).toFloat()
    )
}
