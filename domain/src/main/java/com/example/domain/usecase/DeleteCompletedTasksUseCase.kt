package com.example.domain.usecase

import com.example.domain.repository.TaskRepository

class DeleteCompletedTasksUseCase(
    private val repository: TaskRepository
) {
    suspend fun execute() = repository.deleteCompletedTasks()
}