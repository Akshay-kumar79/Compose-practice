package luvcleanair.the.composepractice

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.time.LocalTime
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun Watch() {
    var currentTime by remember {
        mutableStateOf(System.currentTimeMillis())
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LaunchedEffect(key1 = currentTime) {
            delay(1000)
            currentTime = System.currentTimeMillis()
        }
        Canvas(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.Center),
        ) {
            currentTime
            drawContext.canvas.nativeCanvas.drawCircle(
                this.center.x,
                this.center.y,
                120.dp.toPx(),
                Paint().apply {
                    style = Paint.Style.FILL
                    color = android.graphics.Color.WHITE
                    setShadowLayer(
                        60f,
                        0f,
                        0f,
                        android.graphics.Color.argb(30, 0, 0, 0)
                    )
                }
            )

            // draw all side lines
            for (i in 1..60) {
                val lineStart = Offset(
                    120.dp.toPx() * cos(i * 6 * (PI / 180).toFloat()) + center.x,
                    120.dp.toPx() * sin(i * 6 * (PI / 180).toFloat()) + center.y,
                )
                val lineEnd = if (i % 5 == 0) {
                    Offset(
                        (120 - 24).dp.toPx() * cos(i * 6 * (PI / 180).toFloat()) + center.x,
                        (120 - 24).dp.toPx() * sin(i * 6 * (PI / 180).toFloat()) + center.y,
                    )
                } else {
                    Offset(
                        (120 - 18).dp.toPx() * cos(i * 6 * (PI / 180).toFloat()) + center.x,
                        (120 - 18).dp.toPx() * sin(i * 6 * (PI / 180).toFloat()) + center.y,
                    )
                }

                drawLine(
                    color = if (i % 5 == 0) Color.Black else Color.DarkGray,
                    start = lineStart,
                    end = lineEnd,
                    strokeWidth = if (i % 5 == 0) 4f else 2f,
                    cap = StrokeCap.Round
                )
            }

            val time = LocalTime.now()

            val hourStart = center
            val hourEnd = Offset(
                72.dp.toPx() * cos(((time.hour + time.minute/60f + time.second/3600f) * 30 - 90) * (PI / 180).toFloat()) + center.x,
                72.dp.toPx() * sin(((time.hour + time.minute/60f + time.second/3600f) * 30 - 90) * (PI / 180).toFloat()) + center.y,
            )

            val minuteStart = center
            val minuteEnd = Offset(
                92.dp.toPx() * cos(((time.minute + time.second/60f) * 6 - 90) * (PI / 180).toFloat()) + center.x,
                92.dp.toPx() * sin(((time.minute + time.second/60f) * 6 - 90) * (PI / 180).toFloat()) + center.y,
            )

            val secondStart = center
            val secondEnd = Offset(
                96.dp.toPx() * cos((time.second * 6 - 90) * (PI / 180).toFloat()) + center.x,
                96.dp.toPx() * sin((time.second * 6 - 90) * (PI / 180).toFloat()) + center.y,
            )

            drawLine(
                color = Color.Red,
                start = secondStart,
                end = secondEnd,
                strokeWidth = 8f,
                cap = StrokeCap.Round
            )

            drawLine(
                color = Color.Black,
                start = minuteStart,
                end = minuteEnd,
                strokeWidth = 12f,
                cap = StrokeCap.Round
            )

            drawLine(
                color = Color.Black,
                start = hourStart,
                end = hourEnd,
                strokeWidth = 16f,
                cap = StrokeCap.Round
            )
        }
    }

}