@file:OptIn(ExperimentalFoundationApi::class)
@file:Suppress("FunctionName")

package io.innomission.quicktasks.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import io.innomission.quicktasks.ui.Table.CellDefaults
import io.innomission.quicktasks.ui.Table.Clickable
import io.innomission.quicktasks.ui.Table.Column
import io.innomission.quicktasks.ui.Table.Measurement
import io.innomission.quicktasks.ui.Table.RowDefaults
import io.innomission.quicktasks.ui.Table.SortOrder
import io.innomission.quicktasks.ui.Table.Sortable
import io.innomission.quicktasks.ui.Table.Sorting
import kotlin.collections.*

interface Table<T> {

    sealed interface Measurement {
        data class Fraction(val value: Float) : Measurement
        data class Fixed(val value: Dp) : Measurement
    }

    data class Clickable<T>(
        val enabled: Boolean = true,
        val onClick: (T) -> Unit
    )

    data class Sortable<T>(
        val enabled: Boolean = true,
        val comparator: Comparator<T>
    )

    data class Column<T>(
        val index: Int,
        val name: String,
        val width: Measurement,
        val visible: Boolean,
        val alignment: Alignment?,
        val padding: PaddingValues?,
        val background: Color?,
        val textStyle: TextStyle?,
        val clickable: Clickable<T>?,
        val sortable: Sortable<T>?,
        val header: @Composable () -> Unit,
        val content: @Composable (T) -> Unit
    )

    val cellDefaults: CellDefaults

    val rowDefaults: RowDefaults<Any>

    val headerRowDefaults: RowDefaults<Unit>

    val contentRowDefaults: RowDefaults<T>

    val columns: List<Column<T>>

    val sorting: Sorting

    fun column(
        name: String,
        width: Measurement,
        visible: Boolean = true,
        alignment: Alignment? = null,
        padding: PaddingValues? = null,
        background: Color? = null,
        textStyle: TextStyle? = null,
        clickable: Clickable<T>? = null,
        sortable: Sortable<T>? = null,
        header: @Composable () -> Unit,
        content: @Composable (T) -> Unit
    ): Column<T>

    data class CellDefaults(
        var alignment: Alignment,
        var padding: PaddingValues,
        var background: Color,
        var textStyle: TextStyle,
        var spacer: @Composable (() -> Unit)? = null
    )

    data class RowDefaults<T>(
        val cell: CellDefaults,
        var alignment: Alignment.Vertical,
        var padding: PaddingValues,
        var background: Color,
        var minHeight: Dp? = null,
        var maxHeight: Dp? = null,
        var spacer: (@Composable () -> Unit)? = null,
        var onClick: ((T) -> Unit)? = null
    )

    @Composable
    fun setCellDefaults(action: @Composable CellDefaults.() -> Unit = {})

    @Composable
    fun setRowDefaults(action: @Composable RowDefaults<Any>.() -> Unit = {})

    @Composable
    fun setHeaderRowDefaults(action: @Composable RowDefaults<Unit>.() -> Unit = {})

    @Composable
    fun setContentRowDefaults(action: @Composable RowDefaults<T>.() -> Unit = {})

    enum class SortOrder {
        Ascending, Descending
    }

    sealed interface Sorting

    data class SortBy(val index: Int, val order: SortOrder) : Sorting

    object Unsorted : Sorting

}

@Composable
fun <T> Table(
    items: List<T>,
    key: ((T) -> Any)? = null,
    modifier: Modifier = Modifier,
    sorting: Sorting = Table.Unsorted,
    onSortingChange: ((Sorting) -> Unit)? = null,
    content: @Composable Table<T>.() -> Unit
) {
    val builder = TableBuilder<T>(sorting, onSortingChange)
    builder.content()
    builder.compose(items, key, modifier)
}
