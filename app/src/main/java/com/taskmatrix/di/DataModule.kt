package com.taskmatrix.di

import androidx.room.Room
import com.example.data.DateConverter
import com.example.data.TasksDataBase
import com.example.data.dao.TaskDao
import com.example.data.repository.TaskRepositoryImpl
import com.example.domain.repository.TaskRepository
import org.koin.dsl.module

val dataModule = module {

    single<TasksDataBase>{
        Room.databaseBuilder(
            context = get(),
            klass = TasksDataBase::class.java,
            name = "tasks.db"
        )
            .addTypeConverter(DateConverter())
            .build()
    }
    single<TaskDao> {
        val db = get<TasksDataBase>()
        db.taskDao()
    }
    single<TaskRepository> {
        TaskRepositoryImpl(taskDao = get())
    }
}