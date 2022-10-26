package luvcleanair.the.composepractice.weight_picker

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.core.graphics.withRotation
import luvcleanair.the.composepractice.weight_picker.model.LineType
import luvcleanair.the.composepractice.weight_picker.model.ScaleStyle
import kotlin.math.*

@Composable
fun Scale(
    modifier: Modifier = Modifier,
    style: ScaleStyle = ScaleStyle(),
    minWeight: Int = 20,
    maxWeight: Int = 250,
    initialWeight: Int = 80,
    onWeightChange: (weight: Int) -> Unit
) {
    val radius = style.radius
    val scaleWidth = style.scaleWidth

    var center by remember {
        mutableStateOf(Offset.Zero)
    }
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    var angle by remember {
        mutableStateOf(0f)
    }
    var dragStartedAngle by remember {
        mutableStateOf(0f)
    }
    var oldAngle by remember {
        mutableStateOf(angle)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp)
                .align(Alignment.BottomCenter)
                .pointerInput(true) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            dragStartedAngle = -atan2(
                                circleCenter.x - offset.x,
                                circleCenter.y - offset.y
                            ) * (180f / PI).toFloat()
                        },
                        onDragEnd = {
                            oldAngle = angle
                        }
                    ) { change, dragAmount ->
                        val touchAngle = -atan2(
                            circleCenter.x - change.position.x,
                            circleCenter.y - change.position.y
                        ) * (180f / PI).toFloat()

                        val newAngle = oldAngle + (touchAngle - dragStartedAngle)
                        angle = newAngle.coerceIn(
                            minimumValue = initialWeight - maxWeight.toFloat(),
                            maximumValue = initialWeight - minWeight.toFloat()
                        )
                        onWeightChange((initialWeight - angle).roundToInt())
                    }
                }
        ) {
            center = this.center
            circleCenter = Offset(center.x, scaleWidth.toPx() / 2f + radius.toPx())

            val outerRadius = radius.toPx() + scaleWidth.toPx() / 2f
            val innerRadius = radius.toPx() - scaleWidth.toPx() / 2f

            this.drawContext.canvas.nativeCanvas.apply {
                drawCircle(
                    circleCenter.x,
                    circleCenter.y,
                    radius.toPx(),
                    Paint().apply {
                        strokeWidth = scaleWidth.toPx()
                        color = Color.WHITE
                        setStyle(Paint.Style.STROKE)
                        setShadowLayer(
                            60f,
                            0f,
                            0f,
                            Color.argb(50, 0, 0, 0)
                        )

                    }
                )
            }

            // draw lines
            for (i in minWeight..maxWeight) {
                val angleInRad = (i - initialWeight + angle - 90) * (PI / 180f).toFloat()

                val lineType = when {
                    i % 10 == 0 -> LineType.TenStep
                    i % 5 == 0 -> LineType.FiveStep
                    else -> LineType.Normal
                }

                val lineLength = when (lineType) {
                    LineType.Normal -> style.normalLineLength.toPx()
                    LineType.FiveStep -> style.fiveStepLineLength.toPx()
                    LineType.TenStep -> style.tenStepLineLength.toPx()
                }

                val lineColor = when (lineType) {
                    LineType.Normal -> style.normalLineColor
                    LineType.FiveStep -> style.fiveStepLineColor
                    LineType.TenStep -> style.tenStepLineColor
                }

                val lineStart = Offset(
                    (outerRadius - lineLength) * cos(angleInRad) + circleCenter.x,
                    (outerRadius - lineLength) * sin(angleInRad) + circleCenter.y
                )

                val lineEnd = Offset(
                    outerRadius * cos(angleInRad) + circleCenter.x,
                    outerRadius * sin(angleInRad) + circleCenter.y
                )

                drawLine(
                    color = lineColor,
                    start = lineStart,
                    end = lineEnd,
                    strokeWidth = 1.dp.toPx()
                )

                if (lineType == LineType.TenStep) {
                    drawContext.canvas.nativeCanvas.apply {
                        val textRadius = outerRadius - lineLength - 5.dp.toPx() - style.textSize.toPx()
                        val x = textRadius * cos(angleInRad) + circleCenter.x
                        val y = textRadius * sin(angleInRad) + circleCenter.y

                        withRotation(
                            degrees = angleInRad * (180 / PI).toFloat() + 90f,
                            pivotX = x,
                            pivotY = y
                        ) {
                            drawText(
                                abs(i).toString(),
                                x,
                                y,
                                Paint().apply {
                                    textSize = style.textSize.toPx()
                                    textAlign = Paint.Align.CENTER
                                }
                            )
                        }
                    }
                }

                // draw middle indicator
                val middleTop = Offset(
                    circleCenter.x,
                    circleCenter.y - innerRadius - style.scaleIndicatorLength.toPx()
                )
                val bottomLeft = Offset(
                    circleCenter.x - 4f,
                    circleCenter.y - innerRadius
                )
                val bottomRight = Offset(
                    circleCenter.x + 4f,
                    circleCenter.y - innerRadius
                )

                val indicatorPath = Path().apply {
                    moveTo(middleTop.x, middleTop.y)
                    lineTo(bottomLeft.x, bottomLeft.y)
                    lineTo(bottomRight.x, bottomRight.y)
                    close()
                }

                drawPath(
                    indicatorPath,
                    color = style.scaleIndicatorColor
                )

            }
        }
    }

}



