package com.gatech.asl_dictionary

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ccg.slrcore.common.Thresholder
import com.ccg.slrcore.engine.SimpleExecutionEngine
import com.ccg.slrcore.system.NoTrigger
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import com.ccg.slrcore.common.*
import com.ccg.slrcore.preview.*

@Composable
fun Sign() {
    val mpResult = currResult.collectAsState().value
    val interaction = remember { MutableInteractionSource() }
    val isCameraVisible = interaction.collectIsPressedAsState().value

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {},
                interactionSource = interaction
            ) {
                Text("Start Camera")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isCameraVisible) {
                SLREngine.poll()
                Canvas(modifier = Modifier.fillMaxSize()) {
                    if (mpResult.image != Empties.EMPTY_BITMAP) {
                        val img = mpResult.getBitmap(size)
                        HandPreviewPainter(
                            ComposeCanvasPainterInterface(this),
                            PainterMode.IMAGE_AND_SKELETON
                        ).paint(
                            img,
                            mpResult.result,
                            img.width.toFloat(),
                            img.height.toFloat()
                        )
                    }
                }
                } else {
                SLREngine.buffer.triggerCallbacks() // Trigger processing
                SLREngine.pause()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignPreview() {
    Sign()
}