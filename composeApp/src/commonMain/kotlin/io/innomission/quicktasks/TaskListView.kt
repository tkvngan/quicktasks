@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@file:Suppress("FunctionName")

package io.innomission.quicktasks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.innomission.quicktasks.model.Priority
import io.innomission.quicktasks.model.Task
import io.innomission.quicktasks.model.TaskListViewModel
import io.innomission.quicktasks.ui.Table
import io.innomission.quicktasks.ui.Table.Sorting
import io.innomission.quicktasks.ui.column
import io.innomission.quicktasks.ui.sortable
import io.innomission.quicktasks.ui.text

@Composable
fun TaskListView(
    navController: NavController,
    model: TaskListViewModel,
    darkModeState: MutableState<Boolean>,
    sortingState: MutableState<Sorting>
) {
    val state by model.stateFlow.collectAsState()
    var showMenu by remember { mutableStateOf(false) }

    val topBar: @Composable () -> Unit = {
        TopAppBar(
            title = { Text("Tasks", style = MaterialTheme.typography.titleLarge) },
            actions = {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Settings")
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    CheckboxMenuItem("Dark Mode", darkModeState.value) {
                        darkModeState.value = it
                        showMenu = false
                    }
                    CheckboxMenuItem("Show Completed", state.showCompleted) {
                        model.setShowCompleted(it)
                        showMenu = false
                    }
                }
            },
            colors = topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary
            )
        )
    }

    val addTaskButton: @Composable () -> Unit = {
        FloatingActionButton(onClick = { navController.navigate("task/") }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
        }
    }

    Scaffold(
        topBar = topBar,
        floatingActionButton = addTaskButton
    ) { innerPadding ->
        Table<Task>(
            items = state.tasks.values.toList().filter { state.showCompleted || !it.isCompleted },
            key = Task::id,
            modifier = Modifier.padding(innerPadding).padding(horizontal = 8.dp),
            sortingState.value,
            onSortingChange = {
                sortingState.value = it
            }
        ) {
            setCellDefaults {
                background = Color.Transparent
            }
            setRowDefaults {
                spacer = { Spacer(modifier = Modifier.height(2.dp).fillMaxWidth())}
                padding = PaddingValues(vertical = 0.dp)
            }
            setHeaderRowDefaults {
                background = MaterialTheme.colorScheme.background
            }
            setContentRowDefaults {
                minHeight = 48.dp
                background = CardDefaults.cardColors().containerColor
                onClick = { task -> navController.navigate("task/${task.id}") }
            }
            column("Priority", 16.dp,
                header = "",
                content = { task -> PriorityColorIndicator(task.priority) },
                sortable = sortable(false, Task::priority),
                padding = PaddingValues(vertical = 0.dp)
            )
            column("Name", 0.65f,
                content = { item ->
                    Column {
                        text(Task::name)(item)
                        text(Task::description, style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp))(item)
                    }
                },
                sortable = sortable(true, Task::name)
            )
            column("Due Date", 0.25f,
                sortable = sortable(true, Task::name),
                content = text(Task::dueDate)
            )
            column("Completed", 0.1f,
                header = "",
                sortable = sortable(false, Task::priority),
                content = { task -> TaskListCompletedIndicator(task.isCompleted) }
            )
        }
    }
}

@Composable
fun TaskListCompletedIndicator(completed: Boolean) {
    if (completed) {
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = "Completed",
            modifier = Modifier.size(32.dp).padding(0.dp, 8.dp),
        )
    }
}

@Composable
fun CheckboxMenuItem(text: String, value: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = value,
            onCheckedChange = { onCheckedChange(it) }
        )
        Text(text, modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!value) },
        )
    }
}

@Composable
fun PriorityColorIndicator(priority: Priority, visible: Boolean = true, width: Dp = 4.dp) {
    Spacer(
        modifier = Modifier
            .width(width)
            .fillMaxHeight()
            .background(
                if (visible) {
                    when (priority) {
                        Priority.HIGH -> Color.Red
                        Priority.MEDIUM -> Color.Yellow
                        Priority.LOW -> Color.Green
                    }
                } else Color.Transparent
            )
    )
}
