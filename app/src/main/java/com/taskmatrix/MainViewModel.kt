package com.taskmatrix

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Task
import com.example.domain.usecase.AddTaskUseCase
import com.example.domain.usecase.MarkTaskCompletedUseCase
import com.example.domain.usecase.GetAllCurrentTasksUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    private val addTask: AddTaskUseCase,
    private val deleteTask: MarkTaskCompletedUseCase,
    getTasks: GetAllCurrentTasksUseCase
) : ViewModel() {

    val tasks = getTasks.execute()

    fun addTask(newTask: Task) = viewModelScope.launch {
        addTask.execute(newTask = newTask)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        deleteTask.execute(task = task)
    }

}