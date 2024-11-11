package com.example.todoapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("TaskPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveTask(task: Task) {
        val tasks = getTasks().toMutableList()
        tasks.add(task)
        saveTasks(tasks)
    }

    fun getTasks(): List<Task> {
        val tasksJson = sharedPreferences.getString("tasks", null) ?: return emptyList()
        val type = object : TypeToken<List<Task>>() {}.type
        return gson.fromJson(tasksJson, type)
    }

    fun saveTasks(tasks: List<Task>) {
        val tasksJson = gson.toJson(tasks)
        sharedPreferences.edit().putString("tasks", tasksJson).apply()
    }

    fun getNextTaskId(): Int {
        val tasks = getTasks()
        return if (tasks.isEmpty()) 1 else tasks.maxOf { it.id } + 1
    }

    fun updateTask(task: Task) {
        val tasks = getTasks().toMutableList()
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
            saveTasks(tasks)
        }
    }

    fun deleteTask(task: Task) {
        val tasks = getTasks().toMutableList()
        tasks.removeAll { it.id == task.id }
        saveTasks(tasks)
    }
}
