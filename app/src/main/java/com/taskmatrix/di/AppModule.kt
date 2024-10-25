package com.taskmatrix.di

import com.taskmatrix.viewmodel.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<MainViewModel> {
        MainViewModel(
            addTask = get(),
            markCompleted = get(),
            getTasks = get()
        )
    }
}