package com.example.domain.usecase

import com.example.domain.repository.TaskRepository

class GetAllCurrentTasksUseCase(
    private val repository: TaskRepository
) {
    fun execute() = repository.getAllCurrentTasks()
}