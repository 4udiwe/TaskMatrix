package com.example.domain.usecase

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository

class AddTaskUseCase(
    private val repository: TaskRepository
) {
    suspend fun execute(newTask: Task) = repository.addTask(newTask)
}