package com.example.kotlincloudtodo.model

/**
 * Data model representing a single Todo task.
 */

data class TodoItem(
    // Firestore uses 'null' or absence for auto-generated IDs, but
    // we should store it locally once created for updates/deletions.
    var id: String? = null,
    val title: String = "",
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)