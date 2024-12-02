@file:OptIn(ExperimentalUuidApi::class)
@file:Suppress("FunctionName")

package io.innomission.quicktasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.innomission.quicktasks.model.Priority
import io.innomission.quicktasks.model.Task
import io.innomission.quicktasks.model.TaskListViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

val defaultTimeZone = TimeZone.currentSystemDefault()

fun LocalDate.toEpochMillis(timeZone: TimeZone? = null): Long {
    return this.atStartOfDayIn(timeZone ?: defaultTimeZone).toEpochMilliseconds()
}

fun LocalDate.Companion.fromEpochMillis(millis: Long, timeZone: TimeZone? = null): LocalDate {
    return Instant.fromEpochMilliseconds(millis).toLocalDateTime(timeZone ?: defaultTimeZone).date
}

private fun fromDateString(dateStr: String?, timeZone: TimeZone? = null): Long? {
    if (dateStr != null) {
        return LocalDate.Formats.ISO.parseOrNull(dateStr)?.toEpochMillis(timeZone)
    }
    return null
}

private fun toDateString(dateMillis: Long?, timeZone: TimeZone? = null): String? {
    if (dateMillis != null) {
        return LocalDate.Formats.ISO.format(LocalDate.fromEpochMillis(dateMillis, timeZone))
    }
    return null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskView(
    navigator: NavController,
    model: TaskListViewModel,
    taskId: String
) {
    val isNewTask = taskId.isBlank()
    val state = model.stateFlow.collectAsState()
    var task by remember {
        mutableStateOf(if (isNewTask) Task(id = Uuid.random().toString()) else state.value.tasks[taskId]!!)
    }
    var taskChanged by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showAlertDeleteDialog by remember { mutableStateOf(false) }
    var showAlertExitDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = task.dueDate?.let { fromDateString(it) })

    @Composable
    fun PriorityRadioButton(priority: Priority, color: Color, text: String) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(IntrinsicSize.Max).padding(horizontal = 8.dp)
        ) {
            val onClick = { task = task.copy(priority = priority); taskChanged = true }
            PriorityColorIndicator(priority, task.priority == priority)
            RadioButton(
                selected = task.priority == priority,
                onClick = onClick,
            )
            Text(text, modifier = Modifier.clickable(onClick = onClick))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isNewTask) "New Task" else "Task", style = MaterialTheme.typography.titleLarge)
                },
                actions = {
                    IconButton(
                        onClick = {
                            showAlertDeleteDialog = true
                        },
                        enabled = !isNewTask
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                    IconButton(onClick = {
                        if (taskChanged) {
                            showAlertExitDialog = true
                        } else {
                            navigator.popBackStack()
                        }
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel")
                    }
                    IconButton(enabled = taskChanged, onClick = {
                        if (isNewTask) {
                            model.addTask(task)
                        } else {
                            model.updateTask(task)
                        }
                        navigator.popBackStack()
                    }) {
                        Icon(Icons.Default.Done, contentDescription = "Save")
                    }
                },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding), verticalArrangement = Arrangement.Top) {
            OutlinedTextField(
                value = task.name,
                onValueChange = {
                    task = task.copy(name = it)
                    taskChanged = true
                },
                label = { Text("Name:") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            OutlinedTextField(
                value = task.description,
                onValueChange = {
                    task = task.copy(description = it)
                    taskChanged = true
                },
                label = { Text("Description:") },
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.25f).padding(8.dp)
            )
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Completed:")
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = {
                        task = task.copy(isCompleted = it)
                        taskChanged = true
                    },
                )
            }
            Text("Priority:", Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            PriorityRadioButton(Priority.HIGH, Color.Red, "High")
            PriorityRadioButton(Priority.MEDIUM, Color.Yellow, "Medium")
            PriorityRadioButton(Priority.LOW, Color.Green, "Low")

            OutlinedTextField(
                value = task.dueDate ?: "",
                readOnly = true,
                onValueChange = {
                    task = task.copy(dueDate = it)
                    taskChanged = true
                },
                label = { Text("Due Date:") },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                supportingText = { Text("YYYY-MM-DD") },
                trailingIcon = {
                    IconButton(onClick = {
                        showDatePicker = true
                    }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                    }
                },
            )
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        Button(onClick = {
                            task = task.copy(
                                dueDate = datePickerState.selectedDateMillis?.let {
                                    toDateString(it, TimeZone.UTC)
                                }
                            )
                            taskChanged = true
                            showDatePicker = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDatePicker = false }) {
                            Text("Cancel")
                        }
                    }
                ) { DatePicker(state = datePickerState) }
            }
            if (showAlertDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showAlertDeleteDialog = false },
                    title = { Text("Delete Task") },
                    text = { Text("Are you sure you want to delete this task?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                model.removeTask(task.id)
                                showAlertDeleteDialog = false
                                navigator.popBackStack()
                            }
                        ) { Text("Yes") }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showAlertDeleteDialog = false }
                        ) { Text("No") }
                    }
                )
            }
            if (showAlertExitDialog) {
                AlertDialog(
                    onDismissRequest = { showAlertExitDialog = false },
                    title = { Text("Exit Task") },
                    text = { Text("Are you sure you want to exit without saving changes?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                showAlertExitDialog = false
                                navigator.popBackStack()
                            }
                        ) { Text("Yes") }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showAlertExitDialog = false }
                        ) { Text("No") }
                    }
                )
            }
        }
    }
}
