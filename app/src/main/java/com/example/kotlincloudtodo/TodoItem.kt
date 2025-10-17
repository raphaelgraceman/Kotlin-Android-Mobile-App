package com.example.kotlincloudtodo

import java.util.UUID

/**
 * Data model representing a single Todo task.
 *
 * @property id Unique identifier (UUID string).
 * @property text The task description.
 * @property isDone Status indicating if task is completed.
 */
data class TodoItem(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isDone: Boolean = false
)