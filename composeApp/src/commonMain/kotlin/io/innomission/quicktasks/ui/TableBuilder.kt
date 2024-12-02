@file:OptIn(ExperimentalFoundationApi::class, ExperimentalUuidApi::class)
package io.innomission.quicktasks.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import compose.icons.CssGgIcons
import compose.icons.cssggicons.ArrowDown
import compose.icons.cssggicons.ArrowUp
import io.innomission.quicktasks.ui.Table.CellDefaults
import io.innomission.quicktasks.ui.Table.Clickable
import io.innomission.quicktasks.ui.Table.Column
import io.innomission.quicktasks.ui.Table.Measurement
import io.innomission.quicktasks.ui.Table.Measurement.Fixed
import io.innomission.quicktasks.ui.Table.Measurement.Fraction
import io.innomission.quicktasks.ui.Table.RowDefaults
import io.innomission.quicktasks.ui.Table.SortBy
import io.innomission.quicktasks.ui.Table.SortOrder.Ascending
import io.innomission.quicktasks.ui.Table.SortOrder.Descending
import io.innomission.quicktasks.ui.Table.Sortable
import io.innomission.quicktasks.ui.Table.Sorting
import io.innomission.quicktasks.ui.Table.Unsorted
import kotlin.collections.*
import kotlin.uuid.ExperimentalUuidApi

internal class TableBuilder<T>(
    override val sorting: Sorting = Unsorted,
    val onSortingChange: ((Sorting) -> Unit)? = null,
) : Table<T>{

    override lateinit var cellDefaults: CellDefaults
    override lateinit var rowDefaults: RowDefaults<Any>
    override lateinit var contentRowDefaults: RowDefaults<T>
    override lateinit var headerRowDefaults: RowDefaults<Unit>

    @Composable
    override fun setCellDefaults(action: @Composable (CellDefaults.() -> Unit)) {
        if (!::cellDefaults.isInitialized) {
            cellDefaults = CellDefaults(
                alignment = Alignment.TopStart,
                padding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                background = LocalContentColor.current,
                textStyle = LocalTextStyle.current
            )
        }
        cellDefaults.action()
    }

    @Composable
    override fun setRowDefaults(action: @Composable (RowDefaults<Any>.() -> Unit)) {
        if (!::rowDefaults.isInitialized) {
            setCellDefaults()
            rowDefaults = RowDefaults<Any>(
                cell = cellDefaults.copy(),
                alignment = Alignment.Top,
                padding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
                background = LocalContentColor.current,
            )
        }
        rowDefaults.action()
    }

    @Composable
    override fun setHeaderRowDefaults(action: @Composable (RowDefaults<Unit>.() -> Unit)) {
        if (!::headerRowDefaults.isInitialized) {
            setRowDefaults()
            headerRowDefaults = RowDefaults<Unit>(
                cell = rowDefaults.cell.copy(
                    textStyle = rowDefaults.cell.textStyle.copy(fontWeight = FontWeight.Bold)),
                alignment = rowDefaults.alignment,
                padding = rowDefaults.padding,
                background = rowDefaults.background,
                minHeight = rowDefaults.minHeight,
                maxHeight = rowDefaults.maxHeight,
                spacer = rowDefaults.spacer,
            )
        }
        headerRowDefaults.action()
    }

    @Composable
    override fun setContentRowDefaults(action: @Composable (RowDefaults<T>.() -> Unit)) {
        if (!::contentRowDefaults.isInitialized) {
            setRowDefaults()
            contentRowDefaults = RowDefaults<T>(
                cell = rowDefaults.cell.copy(),
                alignment = rowDefaults.alignment,
                padding = rowDefaults.padding,
                background = rowDefaults.background,
                minHeight = rowDefaults.minHeight,
                maxHeight = rowDefaults.maxHeight,
                spacer = rowDefaults.spacer,
            )
        }
        contentRowDefaults.action()
    }

    @Composable
    internal fun initDefaults() {
        setCellDefaults()
        setRowDefaults()
        setHeaderRowDefaults()
        setContentRowDefaults()
    }

    override var columns: List<Column<T>> = emptyList<Column<T>>()

    override fun column(
        name: String,
        width: Measurement,
        visible: Boolean,
        alignment: Alignment?,
        padding: PaddingValues?,
        background: Color?,
        textStyle: TextStyle?,
        clickable: Clickable<T>?,
        sortable: Sortable<T>?,
        header: @Composable () -> Unit,
        content: @Composable (T) -> Unit
    ): Column<T> {
        val column = Column<T>(
            columns.size, name, width,
            visible, alignment, padding, background, textStyle, clickable, sortable, header, content)
        columns += column
        return column
    }

    private fun List<T>.sort(): List<T> {
        return when (val sorting = sorting) {
            is SortBy -> {
                val column = columns[sorting.index]
                val sortable = column.sortable
                if (sortable?.enabled == true) {
                    when (sorting.order) {
                        Ascending -> sortedWith(sortable.comparator)
                        Descending -> sortedWith(sortable.comparator.reversed())
                    }
                } else this
            }
            is Unsorted -> this
        }
    }

    @Composable
    internal fun compose(
        items: List<T>,
        key: ((T) -> Any)? = null,
        modifier: Modifier = Modifier,
    ) {
        initDefaults()
        Row(modifier = Modifier.then(modifier)) {
            LazyColumn(
                modifier = Modifier.background(Color.Transparent),
                verticalArrangement = Arrangement.spacedBy(0.dp),
                userScrollEnabled = true,
            ) {
                stickyHeader {
                    composeHeaderRow()
                }
                items(items.sort(), key = key) { item ->
                    composeContentRow(item)
                }
            }
        }
    }

    @Composable
    private fun LazyItemScope.composeHeaderRow() = composeRow(
        defaults = headerRowDefaults,
        onClick = headerRowDefaults.onClick?.install(Unit),
        cellContent = { column ->
            val sorting = sorting
            val onClick = {
                if (onSortingChange != null) {
                    val newSorting = when (sorting) {
                        is Unsorted -> SortBy(column.index, Ascending)
                        is SortBy -> if (sorting.index == column.index) {
                            when (sorting.order) {
                                Ascending -> sorting.copy(order = Descending)
                                Descending -> Unsorted
                            }
                        } else {
                            SortBy(column.index, Ascending)
                        }
                    }
                    onSortingChange?.invoke(newSorting)
                }
            }
            val isSortableColumn = column.sortable?.enabled == true && onSortingChange != null
            val isSortingColumn = isSortableColumn && sorting is SortBy && sorting.index == column.index
            Row(
                modifier = Modifier.applyIf(isSortableColumn) { clickable(onClick = onClick)},
                content = {
                    column.header()
                    if (isSortingColumn) {
                        when (sorting.order) {
                            Ascending -> Icon(CssGgIcons.ArrowUp, contentDescription = "Sort Ascending")
                            Descending -> Icon(CssGgIcons.ArrowDown, contentDescription = "Sort Descending")
                        }
                    }
                }
            )
        }
    )

    @Composable
    private fun LazyItemScope.composeContentRow(item: T) = composeRow(
        defaults = contentRowDefaults,
        onClick = contentRowDefaults.onClick?.install(item),
        cellContent = { column ->
            Box(
                modifier = Modifier
                    .applyIf(column.clickable?.enabled == true) { clickable { column.clickable?.onClick(item) } }
                    .align(column.alignment ?: Alignment.TopStart),
                content = { column.content(item) }
            )
        }
    )

    fun <U> ((U) -> Unit).install(item: U): () -> Unit {
        return { this(item) }
    }

    @Composable
    private fun <U> LazyItemScope.composeRow(
        defaults: RowDefaults<U>,
        onClick: (() -> Unit)?,
        cellContent: @Composable BoxScope.(Column<T>) -> Unit,
    ) = Column {
        Row(
            modifier = Modifier
                .applyIfNotNull(onClick) { clickable { it.invoke() } }
                .applyIf(defaults.minHeight != null || defaults.maxHeight != null) {
                    heightIn(defaults.minHeight ?: Dp.Unspecified, defaults.maxHeight ?: Dp.Unspecified) }
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
                .background(defaults.background)
                .padding(defaults.padding),
            verticalAlignment = defaults.alignment,
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            val lastColumn = columns.findLast { it.visible }
            for (column in columns.filter { it.visible }) {
                composeCell(
                    column = column,
                    cellWidth = column.width.takeIf { column !== lastColumn },
                    cellDefaults = defaults.cell,
                    cellContent = { cellContent(column) }
                )
                if (column !== lastColumn) {
                    defaults.cell.spacer?.invoke()
                }
            }
        }
        defaults.spacer?.invoke()
    }

    @Composable
    private fun LazyItemScope.composeCell(
        column: Column<T>,
        cellWidth: Measurement?,
        cellDefaults: CellDefaults,
        cellContent: @Composable BoxScope.() -> Unit
    ) {
        val padding = column.padding ?: cellDefaults.padding
        val background = column.background ?: cellDefaults.background
        val alignment = column.alignment ?: cellDefaults.alignment
        Box(modifier = Modifier
            .run {
                when (cellWidth) {
                    is Fraction -> fillParentMaxWidth(cellWidth.value)
                    is Fixed -> width(cellWidth.value)
                    null -> fillMaxWidth()
                }
            }
            .fillMaxHeight()
            .background(background)
            .padding(padding)
            ,
            contentAlignment = alignment,
            content = {
                Box(
                    modifier = Modifier.align(alignment),
                    content = cellContent
                )
            }
        )
    }
}

internal inline fun <T : Any> Modifier.applyIfNotNull(value: T?, action: Modifier.(T) -> Modifier): Modifier {
    return if (value != null) {
        this.action(value)
    } else this
}

internal inline fun Modifier.applyIf(condition: Boolean, action: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        this.action()
    } else this
}
