package com.example.kotlincloudtodo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlincloudtodo.ui.components.TodoItemRow
import com.example.kotlincloudtodo.ui.viewmodel.TodoViewModel


@Composable
fun TodoListScreen(viewModel: TodoViewModel = viewModel()) {
    val todos by viewModel.todoList.collectAsState()
    val todoList by viewModel.todoList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var newTodoText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = newTodoText,
            onValueChange = { newTodoText = it },
            label = { Text("New task") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (newTodoText.isNotBlank()) {
                    viewModel.addTodo(newTodoText)
                    newTodoText = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Task")
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(alignment = androidx.compose.ui.Alignment.CenterHorizontally))
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(todoList) { todo ->
                TodoItemRow(
                    item = todo,
                    onToggle = { viewModel.toggleTodoCompletion(todo) },
                    onRemove = { viewModel.deleteTodo(todo) }
                )
                HorizontalDivider()
            }
        }
    }
}
