package tw.edu.pu.csim.s1140330.canvas
import androidx.compose.runtime.withFrameNanos
import androidx.compose.runtime.withFrameNanos
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import tw.edu.pu.csim.s1140330.canvas.ui.theme.CanvasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CanvasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CanvasScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CanvasScreen(modifier: Modifier = Modifier) {
    var canvasWidth by remember { mutableStateOf(0f) }
    var canvasHeight by remember { mutableStateOf(0f) }

    var minionsX by remember { mutableStateOf(50f) }
    var minionsY by remember { mutableStateOf(50f) }

    // 新增：控制遊戲是否正在執行的狀態
    var isPlaying by remember { mutableStateOf(false) }

    // 當 isPlaying 為 true 時，LaunchedEffect 才會啟動動畫迴圈
    LaunchedEffect(isPlaying) {
        val speed = 5f
        while (isPlaying) {
            withFrameNanos { _ ->
                minionsX += speed
                minionsY += speed

                // 如果超出畫布邊界，可以選擇讓它重置回左上角（選擇性）
                if (canvasWidth > 0 && minionsX > canvasWidth) {
                    minionsX = -100f // 移出右邊界後從左側重新出現
                }
                if (canvasHeight > 0 && minionsY > canvasHeight) {
                    minionsY = -100f
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val minionsImage: ImageBitmap =
            ImageBitmap.imageResource(id = R.drawable.minions)

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawCircle(
                Color.Magenta,
                100f,
                Offset(300f, 800f)
            )

            drawRect(
                Color.Blue,
                Offset(300f, 200f),
                Size(200f, 200f),
                style = Stroke(width = 10f)
            )

            canvasWidth = size.width
            canvasHeight = size.height

            drawLine(
                Color.Red,
                Offset(200f, 800f),
                Offset(size.width, size.height),
                strokeWidth = 10f
            )

            translate(minionsX, minionsY) {
                drawImage(minionsImage)
            }
        }

        // 畫布上方的文字資訊
        Text(
            text = "畫布寬度 $canvasWidth, 長度 $canvasHeight",
            modifier = Modifier.padding(16.dp)
        )

        // 新增：控制遊戲開始/停止的按鈕（放在畫面下方中央）
        Button(
            onClick = { isPlaying = !isPlaying },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp)
        ) {
            Text(if (isPlaying) "暫停遊戲" else "開始遊戲")
        }
    }
}