package com.example.domain.repository

import com.example.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun addTask(newTask: Task)
    suspend fun markCompleted(task: Task)
    fun getAllCurrentTasks() : Flow<List<Task>>
    fun getAllCompletedTasks() : Flow<List<Task>>
    suspend fun deleteAllTasks()
    suspend fun deleteCompletedTasks()
}