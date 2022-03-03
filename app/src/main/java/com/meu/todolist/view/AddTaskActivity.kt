package com.meu.todolist.view

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.meu.todolist.databinding.ActivityAddTaskBinding
import com.meu.todolist.model.Task
import com.meu.todolist.model.TaskDataSource
import com.meu.todolist.viewmodel.format
import com.meu.todolist.viewmodel.text
import java.util.*

open class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.inputTitle.text = it.title
                binding.inputDescription.text = it.description.toString()
                binding.inputData.text = it.date
                binding.inputHora.text = it.hour
            }
        }

        insertListeners()
    }

    private fun insertListeners(){
       binding.inputData.editText?.setOnClickListener {
           val datePicker = MaterialDatePicker.Builder.datePicker().build()
           val default = TimeZone.getDefault()
           datePicker.addOnPositiveButtonClickListener {
               val offset = default.getOffset(Date().time) * -1
               binding.inputData.text = Date(it + offset).format()
           }
           datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
       }

        binding.inputHora.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val hour = if(timePicker.hour in 0..9)"0${timePicker.hour}" else timePicker.hour
                val minute = if(timePicker.minute in 0..9)"0${timePicker.minute}" else timePicker.minute
                binding.inputHora.text = "$hour:$minute"
            }
            timePicker.show(supportFragmentManager, null)
        }

        binding.btCriar.setOnClickListener {
            //Add Tarefa
            val task = Task (
                    title = binding.inputTitle.text,
                    description = binding.inputDescription.text,
                    date = binding.inputData.text,
                    hour = binding.inputHora.text,
                    id = intent.getIntExtra(TASK_ID, 0)
                    )
            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    companion object{
        const val TASK_ID = "task_id"
    }

}