package com.example.itaskmanager.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.itaskmanager.MainAddTask
import com.example.itaskmanager.R
import com.example.itaskmanager.model.TaskModel

class TaskAdapter(taskList: List<TaskModel>, internal var context: Context)
    :RecyclerView.Adapter<TaskAdapter.TaskViewHolder>()
{
    internal var taskList : List<TaskModel> = ArrayList()
    init {
        this.taskList = taskList
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var name : TextView = view.findViewById(R.id.taskName)
        var description: TextView = view.findViewById(R.id.description)
        var priority:  TextView = view.findViewById(R.id.priority)
        var deadline: TextView = view.findViewById(R.id.deadline)
        var editBtn:  Button = view.findViewById(R.id.editBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_main_task_viewer, parent,false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val tasks = taskList[position]
        holder.name.text = tasks.name
        holder.description.text = tasks.description
        holder.priority.text = tasks.priority
        holder.deadline.text = tasks.deadline

        holder.editBtn.setOnClickListener{
            val i = Intent(context, MainAddTask::class.java)
            i.putExtra("Mode", "E")
            i.putExtra("id", tasks.id)
            context.startActivity(i)
        }
    }
}