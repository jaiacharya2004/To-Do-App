package com.example.todo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: ToDoViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("To Do")
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onAddClick()
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn {
                items(viewModel.tasks.filter { !it.completed }) { task ->
                    TaskItem(task = task, onTaskClick = { viewModel.toggleTaskCompletion(task) })
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Completed", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(viewModel.tasks.filter { it.completed }) { task ->
                    TaskItem(task = task, onTaskClick = { viewModel.toggleTaskCompletion(task) })
                }
            }
        }
        if (viewModel.showDialog.value) {
            AddTaskDialog(
                onDismiss = { viewModel.dismissDialog() },
                onAddTask = { task, dueDate, remindMe ->
                    viewModel.addTask(task, dueDate, remindMe)
                }
            )
        }
    }
}

@Composable
fun TaskItem(task: Task, onTaskClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Checkbox(
            checked = task.completed,
            onCheckedChange = { onTaskClick() }
        )
        Text(
            text = task.description,
            style = if (task.completed) {
                MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.LineThrough)
            } else {
                MaterialTheme.typography.bodyMedium
            }
        )
    }
}

@Composable
fun AddTaskDialog(onDismiss: () -> Unit, onAddTask: (String, Date?, Boolean) -> Unit) {
    var task by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf<Date?>(null) }
    var remindMe by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onAddTask(task, dueDate, remindMe) }) {
                Text("Add Task")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text("Add a Task")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = task,
                    onValueChange = { task = it },
                    label = { Text("Task") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Due Date: ")
                    // Replace with actual date picker
                    TextButton(onClick = { /* Show date picker */ }) {
                        Text(dueDate?.toString() ?: "Select Date")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Remind me: ")
                    Switch(
                        checked = remindMe,
                        onCheckedChange = { remindMe = it }
                    )
                }
            }
        }
    )
}
