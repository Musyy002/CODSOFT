package com.example.todoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CompletedTasksFragment : Fragment() {

    private lateinit var taskManager: TaskManager
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var recyclerViewCompletedTasks: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_completed_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewCompletedTasks = view.findViewById(R.id.recyclerViewCompletedTasks)
        taskManager = TaskManager(requireContext())
        val tasks = taskManager.getTasks().filter { it.isCompleted }

        taskAdapter = TaskAdapter(tasks, object : TaskAdapter.TaskItemListener {
            override fun onTaskCompleted(task: Task) {
                // no action for completed tasks
            }

            override fun onTaskDelete(task: Task) {
                taskManager.deleteTask(task)
                taskAdapter.updateTasks(taskManager.getTasks().filter { it.isCompleted })
            }
        })

        recyclerViewCompletedTasks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
        }
    }
}
