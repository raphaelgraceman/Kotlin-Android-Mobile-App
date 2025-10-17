package com.example.kotlincloudtodo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlincloudtodo.model.TodoItem
import com.example.kotlincloudtodo.repository.TodoFirestoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TodoViewModel(
    private val repository: TodoFirestoreRepository = TodoFirestoreRepository()
) : ViewModel() {

    private val _todoList = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoList: StateFlow<List<TodoItem>> = _todoList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        observeTodos()
    }

    private fun observeTodos() {
        repository.getTodos()
            .onEach { todos ->
                _todoList.value = todos
            }
            .launchIn(viewModelScope)
    }

    fun addTodo(title: String) {
        if (title.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            val newTodo = TodoItem(title = title.trim())
            val success = repository.saveTodo(newTodo)
            if (!success) Log.e("TodoViewModel", "Failed to save todo: $title")
            _isLoading.value = false
        }
    }

    fun toggleTodoCompletion(todo: TodoItem) {
        val id = todo.id ?: run {
            Log.e("TodoViewModel", "Todo ID null, can't toggle completion")
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            val success = repository.updateCompletionStatus(id, !todo.isCompleted)
            if (!success) Log.e("TodoViewModel", "Failed to update completion for $id")
            _isLoading.value = false
        }
    }

    fun deleteTodo(todo: TodoItem) {
        val id = todo.id ?: run {
            Log.e("TodoViewModel", "Todo ID null, can't delete")
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            val success = repository.deleteTodo(id)
            if (!success) Log.e("TodoViewModel", "Failed to delete todo $id")
            _isLoading.value = false
        }
    }
}
