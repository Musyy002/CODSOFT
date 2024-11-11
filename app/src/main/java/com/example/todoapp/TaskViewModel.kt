package com.example.todoapp

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("TaskPreferences", Context.MODE_PRIVATE)
    private val gson = Gson()

    var activeTasks: MutableList<Task> = mutableListOf()
    var completedTasks: MutableList<Task> = mutableListOf()

    init {
        loadTasks()
    }

    // Load tasks from SharedPreferences
    private fun loadTasks() {
        val activeTasksJson = sharedPreferences.getString("activeTasks", "")
        val completedTasksJson = sharedPreferences.getString("completedTasks", "")

        if (!activeTasksJson.isNullOrEmpty()) {
            val type = object : TypeToken<List<Task>>() {}.type
            activeTasks = gson.fromJson(activeTasksJson, type)
        }

        if (!completedTasksJson.isNullOrEmpty()) {
            val type = object : TypeToken<List<Task>>() {}.type
            completedTasks = gson.fromJson(completedTasksJson, type)
        }
    }

    // Save tasks to SharedPreferences
    private fun saveTasks() {
        val editor = sharedPreferences.edit()

        val activeTasksJson = gson.toJson(activeTasks)
        val completedTasksJson = gson.toJson(completedTasks)

        editor.putString("activeTasks", activeTasksJson)
        editor.putString("completedTasks", completedTasksJson)
        editor.apply()
    }

    // Add a new task
    fun addTask(task: Task) {
        activeTasks.add(task)
        saveTasks()
    }

    // Complete a task (move from active to completed)
    fun completeTask(task: Task) {
        activeTasks.remove(task)
        task.isCompleted = true
        completedTasks.add(task)
        saveTasks()
    }

    // Delete a task from completed
    fun deleteCompletedTask(task: Task) {
        completedTasks.remove(task)
        saveTasks()
    }
}
