package com.example.kotlincloudtodo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

/**
 * Composable representing a single row in the Todo list.
 *
 * Displays a checkbox for completion, the task text, and a delete button.
 *
 * @param item The TodoItem to display.
 * @param onToggle Callback when checkbox or row is clicked, toggles completion status.
 * @param onRemove Callback when delete button is clicked, removes the item.
 */
@Composable
fun TodoItemRow(
    item: TodoItem,
    onToggle: (String) -> Unit,
    onRemove: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle(item.id) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox for task completion status
        Checkbox(
            checked = item.isDone,
            onCheckedChange = { onToggle(item.id) }
        )

        // Task description text with strikethrough if completed
        Text(
            text = item.text,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            textDecoration = if (item.isDone) TextDecoration.LineThrough else null,
            color = if (item.isDone) Color.Gray else Color.Unspecified
        )

        // Delete icon button to remove task
        IconButton(onClick = { onRemove(item.id) }) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete task"
            )
        }
    }
}