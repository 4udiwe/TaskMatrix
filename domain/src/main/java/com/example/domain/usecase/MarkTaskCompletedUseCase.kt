package com.example.domain.usecase

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository

class MarkTaskCompletedUseCase(
    private val repository: TaskRepository
) {
    suspend fun execute(task: Task) = repository.markCompleted(task)
}