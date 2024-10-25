package com.taskmatrix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.DeleteAllTasksUseCase
import com.example.domain.usecase.DeleteCompletedTasksUseCase
import com.example.domain.usecase.GetAllComletedTasksUseCase
import kotlinx.coroutines.launch

class SettingsViewModel(
    getCompleted: GetAllComletedTasksUseCase,
    private val deleteAll: DeleteAllTasksUseCase,
    private val deleteCompleted: DeleteCompletedTasksUseCase
) : ViewModel() {

    val completedTasks = getCompleted.execute()
    fun deleteAll() = viewModelScope.launch {
        deleteAll.execute()
    }
    fun deleteCompleted() = viewModelScope.launch {
        deleteCompleted.execute()
    }
}