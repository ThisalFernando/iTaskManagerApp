package com.example.itaskmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.itaskmanager.database.DatabaseHelper
import com.example.itaskmanager.model.TaskModel

class MainAddTask : AppCompatActivity() {

    lateinit var saveBtn : Button
    lateinit var delBtn : Button
    lateinit var editN : EditText
    lateinit var  editD : EditText
    lateinit var editP : EditText
    lateinit var editDead : EditText
    var dbHandler : DatabaseHelper ?= null
    var isEditMode : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_add_task)

        saveBtn = findViewById(R.id.addTaskBtn)
        delBtn = findViewById(R.id.delTaskBtn)
        editN = findViewById(R.id.editTask)
        editD = findViewById(R.id.editDescription)
        editP = findViewById(R.id.editPriority)
        editDead = findViewById(R.id.editdeadline)

        dbHandler = DatabaseHelper(this)

        if(intent != null && intent.getStringExtra("Mode") == "E"){
            //update task
            isEditMode = true
            saveBtn.text = "Update Task"
            delBtn.visibility = View.VISIBLE
            val tasks : TaskModel = dbHandler!!.getTask(intent.getIntExtra("id", 0))
            editN.setText(tasks.name)
            editD.setText(tasks.description)
            editP.setText(tasks.priority)
            editDead.setText(tasks.deadline)
        }
        else{
            //insert new data
            isEditMode = false
            saveBtn.text = "Create Task"
            delBtn.visibility = View.GONE
        }

        saveBtn.setOnClickListener{
            var success : Boolean = false
            val tasks : TaskModel = TaskModel()
            if(isEditMode){
                //Update
                tasks.id = intent.getIntExtra("id", 0)
                tasks.name = editN.text.toString()
                tasks.description = editD.text.toString()
                tasks.priority = editP.text.toString()
                tasks.deadline = editDead.text.toString()

                success = dbHandler?.updateTask(tasks) as Boolean
            }
            else{
                //Insert
                tasks.name = editN.text.toString()
                tasks.description = editD.text.toString()
                tasks.priority = editP.text.toString()
                tasks.deadline = editDead.text.toString()

                success = dbHandler?.addTask(tasks) as Boolean
            }
            if(success){
                val i = Intent(applicationContext, MainTaskView::class.java)
                startActivity(i)
                finish()
            }
            else{
                Toast.makeText(applicationContext, "Something went wrong!", Toast.LENGTH_LONG).show()
            }
        }

        delBtn.setOnClickListener{
            val dialog = AlertDialog.Builder(this).setTitle("iTask is asking:").setMessage("Do you want to delete the task?")
                .setPositiveButton("Yes", {dialog, i ->
                    val success = dbHandler?.deleteTask(intent.getIntExtra("id", 0)) as Boolean
                    if(success){
                        finish()
                        dialog.dismiss()
                    }
                })
                .setNegativeButton("No", {dialog, i ->
                    dialog.dismiss()
                })
            dialog.show()
        }
    }
}