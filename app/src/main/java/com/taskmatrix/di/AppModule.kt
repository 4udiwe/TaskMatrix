package com.taskmatrix.di

import com.taskmatrix.viewmodel.MainViewModel
import com.taskmatrix.viewmodel.SettingsViewModel
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
    viewModel<SettingsViewModel> {
        SettingsViewModel(
            getCompleted = get(),
            deleteAll = get(),
            deleteCompleted = get()
        )
    }
}