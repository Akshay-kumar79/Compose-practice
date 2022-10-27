package luvcleanair.the.composepractice.tic_tac_toe

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import luvcleanair.the.composepractice.tic_tac_toe.model.Player

/**
 *  Incomplete
 */
@Composable
fun TicTacToe(
    modifier: Modifier = Modifier
        .fillMaxSize()
        .padding(56.dp)
) {
    var gameBoardSize by remember {
        mutableStateOf(0f)
    }

    var currentPlayer: Player = remember {
        Player.O
    }

    val currentGameState = remember {
        Array<Player>(9) { Player.Non }
    }

    var clickedIndex by remember {
        mutableStateOf(-1)
    }

//    val animatable = remember {
//        Animatable(initialValue = 0f)
//    }
//
//    LaunchedEffect(key1 = clickedIndex){
//        animatable.animateTo(
//            targetValue = 1f,
//            animationSpec = tween(
//                durationMillis = 2000
//            )
//        )
//    }

    Canvas(
        modifier = modifier
            .pointerInput(true) {
                val rectSize = Size(gameBoardSize / 3f, gameBoardSize / 3)

                /**
                 *  1  2  3
                 *  4  5  6
                 *  7  8  9
                 */
                val allRect = listOf(
                    Rect(Offset(0f, 0f), rectSize),
                    Rect(Offset(gameBoardSize / 3f, 0f), rectSize),
                    Rect(Offset(gameBoardSize * 2 / 3f, 0f), rectSize),
                    Rect(Offset(0f, gameBoardSize / 3f), rectSize),
                    Rect(Offset(gameBoardSize / 3f, gameBoardSize / 3f), rectSize),
                    Rect(Offset(gameBoardSize * 2 / 3f, gameBoardSize / 3f), rectSize),
                    Rect(Offset(0f, gameBoardSize * 2 / 3f), rectSize),
                    Rect(Offset(gameBoardSize / 3f, gameBoardSize * 2 / 3f), rectSize),
                    Rect(Offset(gameBoardSize * 2 / 3f, gameBoardSize * 2 / 3f), rectSize),
                )

                detectTapGestures { offset ->
                    allRect.forEachIndexed { index, rect ->
                        if (rect.contains(offset) && currentGameState[index] is Player.Non) {
                            currentGameState[index] = currentPlayer
                            clickedIndex = index
                        }
                    }
                }


            }
    ) {
        gameBoardSize = size.width

        val horizontalLineStart1 = Offset(0f, gameBoardSize / 3f)
        val horizontalLineEnd1 = Offset(gameBoardSize, gameBoardSize / 3f)

        val horizontalLineStart2 = Offset(0f, gameBoardSize * 2 / 3f)
        val horizontalLineEnd2 = Offset(gameBoardSize, gameBoardSize * 2 / 3f)

        val verticalLineStart1 = Offset(gameBoardSize / 3f, 0f)
        val verticalLineEnd1 = Offset(gameBoardSize / 3f, gameBoardSize)

        val verticalLineStart2 = Offset(gameBoardSize * 2 / 3f, 0f)
        val verticalLineEnd2 = Offset(gameBoardSize * 2 / 3f, gameBoardSize)

        drawLine(
            color = Color.Black,
            start = horizontalLineStart1,
            end = horizontalLineEnd1,
            strokeWidth = 20f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color.Black,
            start = horizontalLineStart2,
            end = horizontalLineEnd2,
            strokeWidth = 20f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color.Black,
            start = verticalLineStart1,
            end = verticalLineEnd1,
            strokeWidth = 20f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color.Black,
            start = verticalLineStart2,
            end = verticalLineEnd2,
            strokeWidth = 20f,
            cap = StrokeCap.Round
        )

        if (currentPlayer is Player.X) {
            val offsets = getDrawXOffsets(clickedIndex, gameBoardSize)
            drawX(offsets, this)
            currentPlayer = Player.O
        } else if (currentPlayer is Player.O) {
            val topLeft = getDrawOOffset(clickedIndex, gameBoardSize)
            drawO(topLeft, gameBoardSize, this)
            currentPlayer = Player.X
        }

//        currentGameState.forEachIndexed { index, player ->
//            if (player is Player.X) {
//                val offsets = getDrawXOffsets(index, gameBoardSize)
//                drawX(offsets, this)
//            } else if (player is Player.O) {
//                val topLeft = getDrawOOffset(index, gameBoardSize)
//                drawO(topLeft, gameBoardSize, this)
//            }
//        }
    }
}

private fun getDrawXOffsets(index: Int, gameBoardSize: Float): List<Offset> {
    val startOffset1: Offset
    val endOffset1: Offset
    val startOffset2: Offset
    val endOffset2: Offset

    val pathPadding = (gameBoardSize / 3f - gameBoardSize / 5f) / 2
    when (index) {
        0 -> {
            startOffset1 = Offset(pathPadding, pathPadding)
            endOffset1 = Offset(gameBoardSize / 3f - pathPadding, gameBoardSize / 3f - pathPadding)
            startOffset2 = Offset(gameBoardSize / 3f - pathPadding, pathPadding)
            endOffset2 = Offset(pathPadding, gameBoardSize / 3f - pathPadding)
        }
        1 -> {
            startOffset1 = Offset(gameBoardSize / 3f + pathPadding, pathPadding)
            endOffset1 = Offset(gameBoardSize * 2 / 3f - pathPadding, gameBoardSize / 3f - pathPadding)
            startOffset2 = Offset(gameBoardSize * 2 / 3f - pathPadding, pathPadding)
            endOffset2 = Offset(gameBoardSize / 3f + pathPadding, gameBoardSize / 3f - pathPadding)
        }
        2 -> {
            startOffset1 = Offset(gameBoardSize * 2 / 3f + pathPadding, pathPadding)
            endOffset1 = Offset(gameBoardSize - pathPadding, gameBoardSize / 3f - pathPadding)
            startOffset2 = Offset(gameBoardSize - pathPadding, pathPadding)
            endOffset2 = Offset(gameBoardSize * 2 / 3f + pathPadding, gameBoardSize / 3f - pathPadding)
        }
        3 -> {
            startOffset1 = Offset(pathPadding, gameBoardSize / 3f + pathPadding)
            endOffset1 = Offset(gameBoardSize / 3f - pathPadding, gameBoardSize * 2 / 3f - pathPadding)
            startOffset2 = Offset(gameBoardSize / 3f - pathPadding, gameBoardSize / 3f + pathPadding)
            endOffset2 = Offset(pathPadding, gameBoardSize * 2 / 3f - pathPadding)
        }
        4 -> {
            startOffset1 = Offset(gameBoardSize / 3f + pathPadding, gameBoardSize / 3f + pathPadding)
            endOffset1 = Offset(gameBoardSize * 2 / 3f - pathPadding, gameBoardSize * 2 / 3f - pathPadding)
            startOffset2 = Offset(gameBoardSize * 2 / 3f - pathPadding, gameBoardSize / 3f + pathPadding)
            endOffset2 = Offset(gameBoardSize / 3f + pathPadding, gameBoardSize * 2 / 3f - pathPadding)
        }
        5 -> {
            startOffset1 = Offset(gameBoardSize * 2 / 3f + pathPadding, gameBoardSize / 3f + pathPadding)
            endOffset1 = Offset(gameBoardSize - pathPadding, gameBoardSize * 2 / 3f - pathPadding)
            startOffset2 = Offset(gameBoardSize - pathPadding, gameBoardSize / 3f + pathPadding)
            endOffset2 = Offset(gameBoardSize * 2 / 3f + pathPadding, gameBoardSize * 2 / 3f - pathPadding)
        }
        6 -> {
            startOffset1 = Offset(pathPadding, gameBoardSize * 2 / 3f + pathPadding)
            endOffset1 = Offset(gameBoardSize / 3f - pathPadding, gameBoardSize - pathPadding)
            startOffset2 = Offset(gameBoardSize / 3f - pathPadding, gameBoardSize * 2 / 3f + pathPadding)
            endOffset2 = Offset(pathPadding, gameBoardSize - pathPadding)
        }
        7 -> {
            startOffset1 = Offset(gameBoardSize / 3f + pathPadding, gameBoardSize * 2 / 3f + pathPadding)
            endOffset1 = Offset(gameBoardSize * 2 / 3f - pathPadding, gameBoardSize - pathPadding)
            startOffset2 = Offset(gameBoardSize * 2 / 3f - pathPadding, gameBoardSize * 2 / 3f + pathPadding)
            endOffset2 = Offset(gameBoardSize / 3f + pathPadding, gameBoardSize - pathPadding)
        }
        8 -> {
            startOffset1 = Offset(gameBoardSize * 2 / 3f + pathPadding, gameBoardSize * 2 / 3f + pathPadding)
            endOffset1 = Offset(gameBoardSize - pathPadding, gameBoardSize - pathPadding)
            startOffset2 = Offset(gameBoardSize - pathPadding, gameBoardSize * 2 / 3f + pathPadding)
            endOffset2 = Offset(gameBoardSize * 2 / 3f + pathPadding, gameBoardSize - pathPadding)
        }
        else -> {
            startOffset1 = Offset(gameBoardSize * 100f, gameBoardSize * 100f)
            endOffset1 = Offset(gameBoardSize * 100f, gameBoardSize * 100f)
            startOffset2 = Offset(gameBoardSize * 100f, gameBoardSize * 100f)
            endOffset2 = Offset(gameBoardSize * 100f, gameBoardSize * 100f)
        }
    }

    return listOf(startOffset1, endOffset1, startOffset2, endOffset2)
}

private fun drawX(offsets: List<Offset>, drawScope: DrawScope) {
    val startOffset1 = offsets[0]
    val endOffset1 = offsets[1]
    val startOffset2 = offsets[2]
    val endOffset2 = offsets[3]

    drawScope.drawPath(
        path = Path().apply {
            moveTo(startOffset1.x, startOffset1.y)
            lineTo(endOffset1.x, endOffset1.y)
            moveTo(startOffset2.x, startOffset2.y)
            lineTo(endOffset2.x, endOffset2.y)
        },
        color = Color.Red,
        style = Stroke(width = 20f, cap = StrokeCap.Round)
    )
}

private fun getDrawOOffset(index: Int, gameBoardSize: Float): Offset {
    val pathPadding = (gameBoardSize / 3f - gameBoardSize / 5f) / 2
    val topLeft: Offset = when (index) {
        0 -> Offset(pathPadding, pathPadding)
        1 -> Offset(gameBoardSize / 3f + pathPadding, pathPadding)
        2 -> Offset(gameBoardSize * 2 / 3f + pathPadding, pathPadding)
        3 -> Offset(pathPadding, gameBoardSize / 3f + pathPadding)
        4 -> Offset(gameBoardSize / 3f + pathPadding, gameBoardSize / 3f + pathPadding)
        5 -> Offset(gameBoardSize * 2 / 3f + pathPadding, gameBoardSize / 3f + pathPadding)
        6 -> Offset(pathPadding, gameBoardSize * 2 / 3f + pathPadding)
        7 -> Offset(gameBoardSize / 3f + pathPadding, gameBoardSize * 2 / 3f + pathPadding)
        8 -> Offset(gameBoardSize * 2 / 3f + pathPadding, gameBoardSize * 2 / 3f + pathPadding)
        else -> Offset(gameBoardSize * 100, gameBoardSize * 100)
    }

    return topLeft
}

private fun drawO(topLeft: Offset, gameBoardSize: Float,  drawScope: DrawScope, anim: Float = 1f) {
    drawScope.drawArc(
        color = Color.Green,
        startAngle = 0f,
        sweepAngle = 360f * anim,
        useCenter = true,
        size = Size(gameBoardSize / 5f, gameBoardSize / 5f),
        topLeft = topLeft,
        style = Stroke(20f)
    )
}