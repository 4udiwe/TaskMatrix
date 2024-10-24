package com.taskmatrix

import androidx.lifecycle.ViewModel
import com.example.domain.usecase.AddTaskUseCase
import com.example.domain.usecase.DeleteTaskUseCase
import com.example.domain.usecase.GetAllTasksUseCase

class MainViewModel(
    private val addTask: AddTaskUseCase,
    private val deleteTask: DeleteTaskUseCase,
    private val getTasks: GetAllTasksUseCase
) : ViewModel() {

    val tasks = getTasks.execute()

}