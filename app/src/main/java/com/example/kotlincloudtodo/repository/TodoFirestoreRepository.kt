package com.example.kotlincloudtodo.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import com.example.kotlincloudtodo.model.TodoItem

class TodoFirestoreRepository {

    private val tAG = "TodoFirestoreRepo"

    // Firestore instance and collection reference
    private val firestore = FirebaseFirestore.getInstance()
    private val todoCollection = firestore.collection("todos")

    /**
     * Save a new TodoItem to Firestore.
     * Assigns the generated document ID back to the TodoItem.
     */
    suspend fun saveTodo(todo: TodoItem): Boolean {
        return try {
            val docRef = todoCollection.add(todo).await()
            todo.id = docRef.id
            Log.d(tAG, "Todo saved with ID: ${docRef.id}")
            true
        } catch (e: Exception) {
            Log.e(tAG, "Failed to save todo", e)
            false
        }
    }

    /**
     * Returns a Flow of List<TodoItem> that emits updates in real-time.
     */
    fun getTodos(): Flow<List<TodoItem>> = callbackFlow {
        val listenerRegistration = todoCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(tAG, "Listen failed.", error)
                    close(error)
                    return@addSnapshotListener
                }

                val todos = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject<TodoItem>()?.apply { id = doc.id }
                } ?: emptyList()

                trySend(todos).isSuccess
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }

    /**
     * Update the completion status of a TodoItem.
     */
    suspend fun updateCompletionStatus(todoId: String, isCompleted: Boolean): Boolean {
        return try {
            todoCollection.document(todoId)
                .update("isCompleted", isCompleted)
                .await()
            Log.d(tAG, "Todo $todoId completion updated to $isCompleted")
            true
        } catch (e: Exception) {
            Log.e(tAG, "Failed to update completion status", e)
            false
        }
    }

    /**
     * Delete a TodoItem by its ID.
     */
    suspend fun deleteTodo(todoId: String): Boolean {
        return try {
            todoCollection.document(todoId).delete().await()
            Log.d(tAG, "Todo $todoId deleted")
            true
        } catch (e: Exception) {
            Log.e(tAG, "Failed to delete todo", e)
            false
        }
    }
}
