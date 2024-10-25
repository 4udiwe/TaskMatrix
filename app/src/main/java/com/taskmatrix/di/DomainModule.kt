package com.taskmatrix.di

import com.example.domain.usecase.AddTaskUseCase
import com.example.domain.usecase.DeleteAllTasksUseCase
import com.example.domain.usecase.DeleteCompletedTasksUseCase
import com.example.domain.usecase.GetAllComletedTasksUseCase
import com.example.domain.usecase.MarkTaskCompletedUseCase
import com.example.domain.usecase.GetAllCurrentTasksUseCase
import org.koin.dsl.module

val domainModule = module{
    factory<AddTaskUseCase>{
        AddTaskUseCase(repository = get())
    }
    factory<MarkTaskCompletedUseCase>{
        MarkTaskCompletedUseCase(repository = get())
    }
    factory<DeleteAllTasksUseCase>{
        DeleteAllTasksUseCase(repository = get())
    }
    factory<GetAllComletedTasksUseCase>{
        GetAllComletedTasksUseCase(repository = get())
    }
    factory<DeleteCompletedTasksUseCase>{
        DeleteCompletedTasksUseCase(repository = get())
    }
    factory<GetAllCurrentTasksUseCase>{
        GetAllCurrentTasksUseCase(repository = get())
    }

}