package com.example.todo

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import java.util.Date

data class Task(
    val description: String,
    val dueDate: Date?,
    val remindMe: Boolean,
    var completed: Boolean = false
)

class ToDoViewModel : ViewModel() {
    val showDialog = mutableStateOf(false)
    val tasks: SnapshotStateList<Task> = mutableStateListOf()

    fun onAddClick() {
        showDialog.value = true
    }

    fun addTask(description: String, dueDate: Date?, remindMe: Boolean) {
        tasks.add(Task(description, dueDate, remindMe))
        showDialog.value = false
    }

    fun dismissDialog() {
        showDialog.value = false
    }

    fun toggleTaskCompletion(task: Task) {
        task.completed = !task.completed
    }
}
