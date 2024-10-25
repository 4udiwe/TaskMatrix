package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.dao.TaskDao
import com.example.data.model.TaskEntity

@Database(
    version = 2,
    entities = [TaskEntity::class]
)
@TypeConverters(DateConverter::class)
abstract class TasksDataBase : RoomDatabase(){
    abstract fun taskDao() : TaskDao
}