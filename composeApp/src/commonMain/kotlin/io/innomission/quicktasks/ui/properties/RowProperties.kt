package io.innomission.quicktasks.ui.properties

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

data class RowProperties(
    val modifier: Modifier? = null,
    val horizontalArrangement: Arrangement.Horizontal? = null,
    val verticalAlignment: Alignment.Vertical? = null,
    val content: (@Composable RowScope.() -> Unit)? = null
)

data class ColumnProperties(
    val modifier: Modifier? = null,
    val verticalArrangement: Arrangement.Vertical? = null,
    val horizontalAlignment: Alignment.Horizontal? = null,
    val content: (@Composable ColumnScope.() -> Unit)? = null
)

data class TextProperties (
    val text: AnnotatedString? = null,
    val modifier: Modifier? = null,
    val color: Color? = null,
    val fontSize: TextUnit? = null,
    val fontStyle: FontStyle? = null,
    val fontWeight: FontWeight? = null,
    val fontFamily: FontFamily? = null,
    val letterSpacing: TextUnit? = null,
    val textDecoration: TextDecoration? = null,
    val textAlign: TextAlign? = null,
    val lineHeight: TextUnit? = null,
    val overflow: TextOverflow? = null,
    val softWrap: Boolean? = null,
    val maxLines: Int? = null,
    val minLines: Int? = null,
    val inlineContent: Map<String, InlineTextContent>? = null,
    val onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    val style: TextStyle? = null
)

data class BoxProperties(
    val modifier: Modifier? = null,
    val contentAlignment: Alignment? = null,
    val propagateMinConstraints: Boolean? = null,
    val content: (@Composable BoxScope.() -> Unit)? = null
)
