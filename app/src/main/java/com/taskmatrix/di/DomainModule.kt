package com.taskmatrix.di

import com.example.domain.usecase.AddTaskUseCase
import com.example.domain.usecase.DeleteTaskUseCase
import com.example.domain.usecase.GetAllTasksUseCase
import org.koin.dsl.module

val domainModule = module{
    factory<AddTaskUseCase>{
        AddTaskUseCase(repository = get())
    }
    factory<DeleteTaskUseCase>{
        DeleteTaskUseCase(repository = get())
    }
    factory<GetAllTasksUseCase>{
        GetAllTasksUseCase(repository = get())
    }
}