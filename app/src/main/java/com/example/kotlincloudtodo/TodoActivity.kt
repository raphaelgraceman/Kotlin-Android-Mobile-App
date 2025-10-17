package com.example.kotlincloudtodo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.util.Log

import com.example.kotlincloudtodo.ui.viewmodel.TodoViewModel

// NOTE: You would need to set up a proper ViewModelFactory
// or use a DI framework like Hilt/Koin for a production app.

class TodoActivity : AppCompatActivity() {

    // Initialize the ViewModel
    private val viewModel: TodoViewModel by viewModels()

    // Placeholders for your UI views
    private lateinit var etTodoInput: EditText
    private lateinit var btnAddTodo: Button
    // Assuming you have an Adapter setup for your RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo) // Your main layout

        // Initialize views (e.g., findViewById or view binding)
        // etTodoInput = findViewById(R.id.et_todo_input)
        // btnAddTodo = findViewById(R.id.btn_add_todo)

        setupInputListener()
        observeViewModel()
    }

    /**
     * Sets up the listener for the Add button to trigger the Firestore save operation.
     */
    private fun setupInputListener() {
        btnAddTodo.setOnClickListener {
            val title = etTodoInput.text.toString().trim()
            if (title.isNotEmpty()) {
                viewModel.addTodo(title) // Calls the ViewModel function to save to Firestore
                etTodoInput.text.clear() // Clear the input field
            }
        }
    }

    /**
     * Observes the real-time list from the ViewModel and updates the UI (RecyclerView Adapter).
     */
    private fun observeViewModel() {
        // Use lifecycleScope to collect the StateFlow safely
        lifecycleScope.launch {
            viewModel.todoList.collect { todos ->
                Log.d("TodoActivity", "Received ${todos.size} updates from Firestore.")
                // 1. Update your RecyclerView Adapter here
                // myTodoAdapter.submitList(todos)

                // 2. You can also log the data for cross-checking:
                todos.forEach { Log.v("TodoActivity", "Item: ${it.title} (ID: ${it.id})") }
            }
        }

        // Optionally observe loading state to show a spinner
        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                // Show/hide a progress bar based on this value
                // progressBar.isVisible = isLoading
            }
        }
    }

    // You would also need functions in your RecyclerView Adapter
    // to handle clicks and call viewModel.toggleTodoCompletion(item)
}
