package com.example.data.repository

import com.example.data.dao.TaskDao
import com.example.data.model.TaskEntity
import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(
    private val taskDao: TaskDao
) : TaskRepository {
    override suspend fun addTask(newTask: Task) {
        val newTaskEntity = TaskEntity(
            newTask.id,
            newTask.title,
            newTask.description,
            newTask.date,
            newTask.deadline,
            newTask.urgent,
            newTask.important,
            newTask.isCompleted
        )
        taskDao.addTask(newTaskEntity)
    }

    override suspend fun markCompleted(task: Task) {
        val newTaskEntity = TaskEntity(
            task.id,
            task.title,
            task.description,
            task.date,
            task.deadline,
            task.urgent,
            task.important,
            isCompleted = true
        )
        taskDao.addTask(newTaskEntity)
    }

    override fun getAllCurrentTasks(): Flow<List<Task>> {
        return taskDao.allCurrent().map { list ->
            list.map { taskEntity ->
                taskEntity.mapToDomain()
            }
        }
    }

    override fun getAllCompletedTasks(): Flow<List<Task>> {
        return taskDao.allCompleted().map { list ->
            list.map { taskEntity ->
                taskEntity.mapToDomain()
            }
        }
    }

    override suspend fun deleteAllTasks() {
        taskDao.deleteAll()
    }

    override suspend fun deleteCompletedTasks() {
        taskDao.deleteCompleted()
    }
}