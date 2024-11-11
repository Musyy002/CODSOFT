package com.example.todoapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import android.widget.Toast
import java.util.Calendar

class AddTaskFragment : Fragment() {

    private lateinit var taskManager: TaskManager

    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskManager = TaskManager(requireContext())

        var btnSaveTask = view.findViewById<Button>(R.id.btnSaveTask)
        var etdate = view.findViewById<EditText>(R.id.etDueDate)

        etdate.setOnClickListener({
            val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val dateTime = calendar.time
                etdate.setText(dateTime.toString()) // Format as needed
            }
            DatePickerDialog(requireContext(), dateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        })

        // Handle Save Task button click
        btnSaveTask.setOnClickListener {
            var etTaskTitle = view.findViewById<EditText>(R.id.etTaskTitle)
            val title = etTaskTitle.text.toString()
            var etTaskDescription = view.findViewById<EditText>(R.id.etTaskDescription)
            val description = etTaskDescription.text.toString()
            var spPriority = view.findViewById<Spinner>(R.id.spPriority)
            val priority = spPriority.selectedItemPosition
            var etDueDate = view.findViewById<EditText>(R.id.etDueDate)
            val dueDate = etDueDate.text.toString()

            if (title.isNotEmpty()) {
                val task = Task(
                    id = taskManager.getNextTaskId(),
                    title = title,
                    descreption = description,
                    priority = priority,
                    dueDate = dueDate
                )
                taskManager.saveTask(task)

                Toast.makeText(context, "Task added!", Toast.LENGTH_SHORT).show()
                etTaskTitle.text.clear()
                etTaskDescription.text.clear()
                etDueDate.text.clear()
                startActivity(Intent(requireContext(),Confirmation::class.java))
            } else {
                Toast.makeText(context, "Please enter a task title.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
