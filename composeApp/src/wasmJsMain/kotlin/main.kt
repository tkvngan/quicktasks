import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import io.innomission.quicktasks.App
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val root = document.body?.firstElementChild ?: return
    ComposeViewport(root) {
        App()
    }
}
