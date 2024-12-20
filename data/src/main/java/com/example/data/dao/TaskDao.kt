package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: TaskEntity)
    @Query("SELECT * FROM tasks WHERE isCompleted == 0")
    fun allCurrent() : Flow<List<TaskEntity>>
    @Query("SELECT * FROM tasks WHERE isCompleted == 1")
    fun allCompleted() : Flow<List<TaskEntity>>
    @Query("DELETE FROM tasks")
    suspend fun deleteAll()
    @Query("DELETE FROM tasks WHERE isCompleted == 1")
    suspend fun deleteCompleted()
}