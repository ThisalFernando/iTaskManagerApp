package com.example.itaskmanager

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itaskmanager.adapter.TaskAdapter
import com.example.itaskmanager.database.DatabaseHelper
import com.example.itaskmanager.model.TaskModel

class MainTaskView : AppCompatActivity() {

    lateinit var task_recycler: RecyclerView
    lateinit var addBtn: Button
    var taskAdapter : TaskAdapter ?= null
    var dbHandler : DatabaseHelper ?= null
    var taskList : List<TaskModel> = ArrayList<TaskModel>()
    var linearLayoutManager : LinearLayoutManager ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_task_view)

        task_recycler = findViewById(R.id.taskRecyclerView)
        addBtn = findViewById(R.id.addTaskBtn)

        dbHandler = DatabaseHelper(this)
        fetchList()

        addBtn.setOnClickListener{
            val i = Intent(applicationContext,MainAddTask::class.java)
            startActivity(i)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchList(){
        taskList = dbHandler!!.getAllTasks()
        taskAdapter = TaskAdapter(taskList,applicationContext)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        task_recycler.layoutManager = linearLayoutManager
        task_recycler.adapter = taskAdapter
        taskAdapter?.notifyDataSetChanged()
    }
}