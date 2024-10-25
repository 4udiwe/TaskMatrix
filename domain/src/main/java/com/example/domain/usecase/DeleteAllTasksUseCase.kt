package com.example.domain.usecase

import com.example.domain.repository.TaskRepository

class DeleteAllTasksUseCase(
    private val repository: TaskRepository
) {
    suspend fun execute() = repository.deleteAllTasks()
}