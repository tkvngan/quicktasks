package io.innomission.quicktasks.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import io.innomission.quicktasks.ui.Table.Clickable
import io.innomission.quicktasks.ui.Table.Measurement
import io.innomission.quicktasks.ui.Table.Measurement.Fixed
import io.innomission.quicktasks.ui.Table.Measurement.Fraction
import io.innomission.quicktasks.ui.Table.Sortable
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.roundToLong

fun <T> Table<T>.fixed(value: Float) = Measurement.Fixed(value.dp)
fun <T> Table<T>.fixed(value: Dp) = Measurement.Fixed(value)
fun <T> Table<T>.fraction(value: Float) = Measurement.Fraction(value)

fun <T> Table<T>.clickable(
    enabled: Boolean = true,
    onClick: (T) -> Unit
): Clickable<T> = Clickable(enabled, onClick)

fun <T> Table<T>.sortable(
    enabled: Boolean = true,
    comparator: Comparator<T>
) = Sortable<T>(enabled, comparator)

fun <T, U : Comparable<U>, V : U?> Table<T>.sortable(
    enabled: Boolean = true,
    comparable: (T) -> V
) = Sortable<T>(enabled) { item1: T, item2: T ->
    val value1: V = comparable(item1)
    val value2: V = comparable(item2)
    when {
        value1 != null && value2 != null -> (value1 as U).compareTo(value2 as U)
        value1 != null -> 1
        value2 != null -> -1
        else -> 0
    }
}

fun <T> Table<T>.column(
    name: String,
    width: Measurement,
    visible: Boolean = true,
    alignment: Alignment? = null,
    padding: PaddingValues? = null,
    background: Color? = null,
    textStyle: TextStyle? = null,
    clickable: Clickable<T>? = null,
    sortable: Sortable<T>? = null,
    header: String = name,
    content: @Composable (T) -> Unit
) {
    column(name, width, visible, alignment, padding, background, textStyle, clickable, sortable, header = header(header), content)
}

fun <T> Table<T>.column(
    name: String,
    width: Float,
    visible: Boolean = true,
    alignment: Alignment? = null,
    padding: PaddingValues? = null,
    background: Color? = null,
    textStyle: TextStyle? = null,
    clickable: Clickable<T>? = null,
    sortable: Sortable<T>? = null,
    header: String = name,
    content: @Composable (T) -> Unit
) {
    column(name, Fraction(width), visible, alignment, padding, background, textStyle, clickable, sortable, header = header(header), content)
}

fun <T> Table<T>.column(
    name: String,
    width: Dp,
    visible: Boolean = true,
    alignment: Alignment? = null,
    padding: PaddingValues? = null,
    background: Color? = null,
    textStyle: TextStyle? = null,
    clickable: Clickable<T>? = null,
    sortable: Sortable<T>? = null,
    header: String = name,
    content: @Composable (T) -> Unit
) {
    column(name, Fixed(width), visible, alignment, padding, background, textStyle, clickable, sortable, header = header(header), content)
}

fun <T, V> Table<T>.content(
    transform: (T) -> V,
    content: @Composable (T, V) -> Unit
): @Composable (T) -> Unit = { item ->
    val value = transform(item)
    if (value != null) {
        content(item, value)
    }
}

fun <T> Table<T>.text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle? = null,
): @Composable () -> Unit = {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = style ?: contentRowDefaults.cell.textStyle,
    )
}

fun <T> Table<T>.header(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle? = null,
): @Composable () -> Unit = {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = style ?: headerRowDefaults.cell.textStyle,
    )
}

fun <T, V> Table<T>.text(
    transform: (T) -> V,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle? = null,
): @Composable (T) -> Unit = content(transform) { item, value ->
    if (value != null) {
        Text(
            text = value.toString(),
            modifier = modifier,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = style ?: contentRowDefaults.cell.textStyle,
        )
    }
}

fun <T, V : Number?> Table<T>.text(
    transform: (T) -> V,
    precision: Int,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle? = null,
): @Composable (T) -> Unit = content(transform) { item, value ->
    if (value != null) {
        Text(
            text = value.format(precision),
            modifier = modifier,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = style ?: contentRowDefaults.cell.textStyle,
        )
    }
}

fun <T, V : Boolean?> Table<T>.checkbox(
    transform: (T) -> V,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors? = null,
    interactionSource: MutableInteractionSource? = null
): @Composable (T) -> Unit = content(transform) { item, value ->
    if (value != null) {
        Checkbox(
            checked = value,
            onCheckedChange = onCheckedChange,
            modifier = modifier,
            enabled = enabled,
            colors = colors ?: CheckboxDefaults.colors(),
            interactionSource = interactionSource
        )
    }
}

fun <T, V : Boolean?> Table<T>.radio(
    transform: (T) -> V,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: RadioButtonColors? = null,
    interactionSource: MutableInteractionSource? = null
): @Composable (T) -> Unit = content(transform) { item, value ->
    if (value != null) {
        RadioButton(
            selected = value,
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            colors = colors ?: RadioButtonDefaults.colors(),
            interactionSource = interactionSource
        )
    }
}

fun <T, V : Boolean?> Table<T>.switch(
    transform: (T) -> V,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier,
    thumbContent: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    colors: SwitchColors? = null,
    interactionSource: MutableInteractionSource? = null
): @Composable (T) -> Unit = content(transform) { item, value ->
    if (value != null) {
        Switch(
            checked = value,
            onCheckedChange = onCheckedChange,
            modifier = modifier,
            thumbContent = thumbContent,
            enabled = enabled,
            colors = colors ?: SwitchDefaults.colors(),
            interactionSource = interactionSource
        )
    }
}

fun <T> Table<T>.icon(
    imageVector: ImageVector,
    contentDescription: String? = null,
    modifier: Modifier,
    tint: Color? = null,
): @Composable () -> Unit = {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription ?: "",
        modifier = modifier,
        tint = tint ?: LocalContentColor.current,
    )
}

fun <T> Table<T>.icon(
    transform: (T) -> ImageVector?,
    contentDescription: String? = null,
    modifier: Modifier,
    tint: Color? = null,
): @Composable (T) -> Unit = { item ->
    val image = transform(item)
    if (image != null) {
        Icon(
            imageVector = image,
            contentDescription = contentDescription ?: "",
            modifier = modifier,
            tint = tint ?: LocalContentColor.current,
        )
    }
}

internal fun Number.format(precision: Int): String {
    val precision = max(0, precision)
    var factor = 10.0.pow(precision).toLong()
    val double = this.toDouble()
    val sign = if (double < 0) "-" else ""
    val digits = (double * factor).roundToLong().absoluteValue.toString().let {
        "0".repeat(max(0, precision - it.length + 1)) + it
    }
    return sign + if (precision > 0) {
        digits.dropLast(precision) + "." + digits.takeLast(precision)
    } else digits
}
