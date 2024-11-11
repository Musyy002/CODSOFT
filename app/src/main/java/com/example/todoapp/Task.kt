package com.example.todoapp

data class Task(
    var id: Int,
    var title: String,
    var descreption: String?,
    var priority: Int,
    var dueDate: String?,
    var isCompleted: Boolean = false

)
