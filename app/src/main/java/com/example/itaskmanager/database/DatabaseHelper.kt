package com.example.itaskmanager.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf
import com.example.itaskmanager.model.TaskModel

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object{
        private const val DB_NAME = "iTask.db"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "TaskList"
        private const val ID = "id"
        private const val TASK_NAME = "Task"
        private const val TASK_DES = "description"
        private const val TASK_PRIORITY = "priority"
        private const val TASK_DEADLINE = "deadline"

    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $TASK_NAME TEXT, $TASK_DES TEXT, $TASK_PRIORITY TEXT, $TASK_DEADLINE TEXT)"
        p0?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        p0?.execSQL(DROP_TABLE)
        onCreate(p0)
    }

    @SuppressLint("Range")
    fun getAllTasks(): List<TaskModel>{
        val taskList = ArrayList<TaskModel>()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    val tasks = TaskModel()
                    tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    tasks.name = cursor.getString(cursor.getColumnIndex(TASK_NAME))
                    tasks.description = cursor.getString(cursor.getColumnIndex(TASK_DES))
                    tasks.priority = cursor.getString(cursor.getColumnIndex(TASK_PRIORITY))
                    tasks.deadline = cursor.getString(cursor.getColumnIndex(TASK_DEADLINE))
                    taskList.add(tasks)
                }while(cursor.moveToNext())
            }
        }
        cursor.close()
        return taskList
    }

    //insert
    fun addTask(tasks : TaskModel) : Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(TASK_NAME, tasks.name)
        values.put(TASK_DES, tasks.description)
        values.put(TASK_PRIORITY, tasks.priority)
        values.put(TASK_DEADLINE, tasks.deadline)
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    //select the data of particular id
    @SuppressLint("Range")
    fun getTask(_id: Int) : TaskModel{
        val tasks = TaskModel()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.moveToFirst()
        tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
        tasks.name = cursor.getString(cursor.getColumnIndex(TASK_NAME))
        tasks.description = cursor.getString(cursor.getColumnIndex(TASK_DES))
        tasks.priority = cursor.getString(cursor.getColumnIndex(TASK_PRIORITY))
        tasks.deadline = cursor.getString(cursor.getColumnIndex(TASK_DEADLINE))
        cursor.close()
        return tasks
    }

    fun deleteTask(_id: Int) : Boolean{
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun updateTask(tasks: TaskModel): Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(TASK_NAME, tasks.name)
        values.put(TASK_DES, tasks.description)
        values.put(TASK_PRIORITY, tasks.priority)
        values.put(TASK_DEADLINE, tasks.deadline)
        val _success = db.update(TABLE_NAME, values, ID + "=?", arrayOf(tasks.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }
}