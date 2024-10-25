package com.example.domain.usecase

import com.example.domain.repository.TaskRepository

class GetAllComletedTasksUseCase (
    private val repository: TaskRepository
){
    fun execute() = repository.getAllCompletedTasks()
}