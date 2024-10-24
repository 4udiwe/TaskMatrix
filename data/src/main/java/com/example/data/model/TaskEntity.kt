package com.example.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    val id: Int? = null,
    @ColumnInfo
    val title: String = "",
    @ColumnInfo
    val description: String = "",
    @ColumnInfo
    val date: Date? = null,
    @ColumnInfo
    val deadline: Date? = null,
    @ColumnInfo
    val urgent: Boolean = true,
    @ColumnInfo
    val important: Boolean = true
)
