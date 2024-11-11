package com.example.todoapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ItemTaskBinding

class TaskAdapter(
    private var tasks: List<Task>,
    private val listener: TaskItemListener
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    interface TaskItemListener {
        fun onTaskCompleted(task: Task)
        fun onTaskDelete(task: Task)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)

        // Set Complete button click listener
        holder.binding.btnMarkComplete.setOnClickListener {
            if (!task.isCompleted) {  // Trigger only if the task is not completed
                listener.onTaskCompleted(task)
            }
        }

        // Set long-click listener for deleting a task
        holder.binding.root.setOnLongClickListener {
            listener.onTaskDelete(task)
            true
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(updatedTasks: List<Task>) {
        tasks = updatedTasks
        notifyDataSetChanged()
    }

    class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            // Bind task data to UI components
            binding.taskTitle.text = task.title
            binding.taskDescription.text = task.descreption ?: "No description"
            binding.dueDate.text = "Due: ${task.dueDate ?: "None"}"
            binding.btnMarkComplete.isChecked = task.isCompleted
        }
    }
}
