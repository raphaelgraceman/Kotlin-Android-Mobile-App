package com.example.kotlincloudtodo

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

/**
 * Main screen composable showing the Todo list with add, toggle, and delete functionality.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen() {
    // Observable list of TodoItems as mutable state
    val todoItems = remember {
        mutableStateListOf(
            TodoItem(text = "Learn Kotlin"),
            TodoItem(text = "Build ToDo App", isDone = true),
            TodoItem(text = "Go to the gym")
        )
    }

    // Controls visibility of Add Task dialog
    var showAddDialog by remember { mutableStateOf(false) }
    // Holds current input text for new task
    var newTaskText by remember { mutableStateOf("") }

    // Dialog for adding a new task
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(text = "Add New Task") },
            text = {
                TextField(
                    value = newTaskText,
                    onValueChange = { newTaskText = it },
                    label = { Text("Task name") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Add new task if text is not blank
                        if (newTaskText.isNotBlank()) {
                            todoItems.add(TodoItem(text = newTaskText.trim()))
                            newTaskText = ""
                            showAddDialog = false
                        }
                    },
                    enabled = newTaskText.isNotBlank()
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAddDialog = false
                        newTaskText = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // Scaffold provides basic app layout: top bar and FAB
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Compose ToDo List") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        // LazyColumn efficiently displays a scrollable list of TodoItems
        LazyColumn(
            contentPadding = paddingValues
        ) {
            items(
                items = todoItems,
                key = { it.id }
            ) { item ->
                TodoItemRow(
                    item = item,
                    onToggle = { id ->
                        // Toggle the isDone state of the clicked task
                        val index = todoItems.indexOfFirst { it.id == id }
                        if (index != -1) {
                            todoItems[index] = todoItems[index].copy(isDone = !todoItems[index].isDone)
                        }
                    },
                    onRemove = { id ->
                        // Remove the task by ID
                        todoItems.removeAll { it.id == id }
                    }
                )
                HorizontalDivider()
            }
        }
    }
}