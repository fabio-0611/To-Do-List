package com.fabiolima.todolist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns._ID
import android.view.View
import android.util.Log
import com.fabiolima.todolist.databinding.ActivityMainBinding
import com.fabiolima.todolist.datasource.TaskDataSource
import com.fabiolima.todolist.model.Task
import com.fabiolima.todolist.ui.AddTaskActivity
import com.fabiolima.todolist.ui.TaskListAdapter

class MainActivity : AppCompatActivity() {
    private val it: Task
        get() {

        }
    private lateinit var binding: ActivityMainBinding
     val adapter()

    private fun lazy(function: () -> TaskListAdapter): Any {
        adapter by lazy { TaskListAdapter() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter
        updateList()

        insertListeners()
        //DATA STORE
        //ROOM
    }

    private fun insertListeners() {
        binding.fab.setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
        }

        adapter.listenerEdit{
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID,"$_ID")
            startActivityForResult(intent, CREATE_NEW_TASK)
        }

        adapter.listenerDelete {
            TaskDataSource.deleteTask(task = it)
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) updateList()
    }

    private fun updateList() {
        val list = TaskDataSource.getList()
        binding.includeEmpty.emptyState.visibility = if (list.isEmpty()) View.VISIBLE
        else View.GONE

        adapter.submitList(list)
    }

    companion object {
        private const val CREATE_NEW_TASK = 1000
    }

}