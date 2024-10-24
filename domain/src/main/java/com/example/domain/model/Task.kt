package com.example.domain.model

import java.util.Date

data class Task(
    val id: Int? = null,
    val title: String = "",
    val description: String = "",
    val date: Date? = null,
    val deadline: Date? = null,
    val urgent: Boolean = true,
    val important: Boolean = true
)
