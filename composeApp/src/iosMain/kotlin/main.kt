import androidx.compose.ui.window.ComposeUIViewController
import io.innomission.quicktasks.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
