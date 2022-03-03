package com.meu.todolist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.get
import com.meu.todolist.R
import com.meu.todolist.databinding.ActivityMainBinding
import com.meu.todolist.model.TaskDataSource

open class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvTask.adapter = adapter

        updateList()
        insertListeners()
    }

    var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == RESULT_OK){
                binding.rvTask.adapter = adapter
                updateList()
            }
        }

    private fun  updateList(){
        val list = TaskDataSource.getList()
        binding.includedEmpty.emptyState.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE

        adapter.submitList(list)
    }

    private fun insertListeners() {
        binding.btAdd.setOnClickListener {
            resultLauncher.launch(Intent(this, AddTaskActivity::class.java))
        }

        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            resultLauncher.launch(intent)
        }

        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
    }
}

