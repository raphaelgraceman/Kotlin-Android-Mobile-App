package com.example.kotlincloudtodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kotlincloudtodo.ui.theme.KotlinCloudTodoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinCloudTodoTheme {
                //Launch the main TodoListScreen Composable
                TodoListScreen()
            }
        }
    }
}
