package com.example.domain.repository

import com.example.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun addTask(newTask: Task)
    suspend fun deleteTask(task: Task)
    fun getAllTasks() : Flow<List<Task>>
}