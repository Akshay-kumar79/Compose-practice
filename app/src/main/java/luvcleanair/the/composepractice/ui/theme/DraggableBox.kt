package luvcleanair.the.composepractice.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun DraggableBox() {
    var offset by remember {
        mutableStateOf(Offset(0f, 0f))
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
                }
                .size(100.dp)
                .background(Color.Green)
                .pointerInput(true) {
                    detectDragGestures { change, dragAmount ->
                        offset = Offset(
                            (change.position).x,
                            (change.position).y
                        )
                    }
                }
        )
    }
}