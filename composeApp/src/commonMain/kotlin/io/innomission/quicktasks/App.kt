@file:Suppress("FunctionName")

package io.innomission.quicktasks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.remember
import io.innomission.quicktasks.model.TaskListViewModel
import io.innomission.quicktasks.theme.AppTheme
import io.innomission.quicktasks.ui.Table

@Composable
fun App() {
    val navigator = rememberNavController()
    val model by remember { derivedStateOf { TaskListViewModel() } }
    val darkModeState = remember { mutableStateOf(false) }
    val sortingState = remember { mutableStateOf<Table.Sorting>(Table.SortBy(1, Table.SortOrder.Ascending)) }

    AppTheme(isDarkMode = darkModeState.value) {
        NavHost(navigator, startDestination = "tasks", modifier = Modifier.fillMaxSize()) {
            composable("tasks") {
                TaskListView(navigator, model, darkModeState, sortingState)
            }
            composable("task/{id}") {
                TaskView(navigator, model, taskId = it.arguments?.getString("id") ?: "")
            }
        }
    }
}

/*
                Table<Task>(state.tasks.values.toList(), Task::id, modifier = Modifier.padding(8.dp)) {
                    column("ID", 0.1f, content = text(Task::id), sortable = sortable(true, Task::id))
                    column("Name", 0.5f, content = text(Task::name), sortable = sortable(true, Task::name))
                    column("Due Date", 0.25f, content = text(Task::dueDate), sortable = sortable(true, Task::name))
                    column("Completed", 0.1f,
                        header = "Done",
                        content = switch(Task::isCompleted, modifier = Modifier.scale(0.5f)),
                        clickable = clickable { task ->
                            model.updateTask(task.copy(isCompleted = !task.isCompleted))
                        },
                        sortable = sortable<Task, Boolean, Boolean>(true, Task::isCompleted),
                        alignment = Alignment.TopCenter
                    )
                    setContentDefaults(
                        clickable = clickable { println("Clicked on $it") },
                        horizontalGridLine = GridLine(thickness = 1.dp)
                    )
                    setHeaderDefaults(
                        horizontalGridLine = GridLine(thickness = 2.dp),
                    )
                    sortBy(3, Table.SortOrder.Ascending)
                }

 */
