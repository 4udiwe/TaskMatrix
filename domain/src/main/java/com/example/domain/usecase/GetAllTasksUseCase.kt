package com.example.domain.usecase

import com.example.domain.repository.TaskRepository

class GetAllTasksUseCase(
    private val repository: TaskRepository
) {
    fun execute() = repository.getAllTasks()
}