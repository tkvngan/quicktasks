@file:OptIn(ExperimentalUuidApi::class)

package io.innomission.quicktasks.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class SortBy(
    val column: Column,
    val order: Order
) {
    enum class Column(val comparator: Comparator<Task>){
        Name(compareBy<Task>(Task::name)),
        DueDate(compareBy(Task::dueDate)),
        Priority(compareBy(Task::priority))
    }

    enum class Order {
        Ascending, Descending
    }

    val comparator: Comparator<Task>; get() {
        return when (order) {
            Order.Ascending -> column.comparator
            Order.Descending -> column.comparator.reversed()
        }
    }
}

class TaskListViewModel {

    val uuid: String = Uuid.random().toString()

    data class State(
        val tasks: Map<String, Task> = emptyMap(),
        val showCompleted: Boolean = false,
        val sortBy: SortBy? = null
    )

    private val mutableStateFlow = MutableStateFlow(State(tasks = TASK_LIST.associateBy { it.id }))

    private var state: State; get() = mutableStateFlow.value; set(value) { mutableStateFlow.value = value }

    val stateFlow = mutableStateFlow.asStateFlow()

    fun addTask(task: Task) {
        state = state.copy(tasks = state.tasks + (task.id to task))
    }

    fun removeTask(id: String) {
        state = state.copy(tasks = state.tasks - id)
    }

    fun updateTask(task: Task) {
        state = state.copy(tasks = state.tasks + (task.id to task))
    }

    fun setShowCompleted(showCompleted: Boolean) {
        state = state.copy(showCompleted = showCompleted)
    }

    fun setSortBy(sortBy: SortBy?) {
        state = state.copy(sortBy = sortBy)
    }
}
