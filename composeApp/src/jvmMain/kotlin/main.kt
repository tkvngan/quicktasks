import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension
import io.innomission.quicktasks.App

const val WINDOW_WIDTH = 448 + 48
const val WINDOW_HEIGHT = 998 + 48

fun main() = application {
    Window(
        title = "QuickTasks",
        state = rememberWindowState(width = WINDOW_WIDTH.dp, height = WINDOW_HEIGHT.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(WINDOW_WIDTH, WINDOW_HEIGHT / 2)
        App()
    }
}
