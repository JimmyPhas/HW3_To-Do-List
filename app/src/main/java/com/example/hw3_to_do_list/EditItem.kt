package com.example.hw3_to_do_list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_edit_item.*

class EditItem : AppCompatActivity() {

    private val Tag = "EditActivity"
    private val myIntent = Intent()
    private val FILE_NAME = "MyTasks"
    private val taskdatalist = mutableListOf<TaskClass>()
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        val taskdata = sharedPreferences.getString("TaskData", "")?:""

        if (taskdata.isNotEmpty()) {
            val gson = Gson()
            val sType = object : TypeToken<List<TaskClass>>() {}.type
            val savedTaskList = gson.fromJson<List<TaskClass>>(taskdata, sType)

            for (task in savedTaskList) {
                taskdatalist.add(task)
            }
        }

        clear()
    }

    fun saveback(view:View) {
        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        if (iteminput.text.toString() == "")
            message.text = "Task cannot be blank."
        else {
            val newTask = TaskClass(task = iteminput.text.toString(), details = detailinput.text.toString())
            taskdatalist.add(newTask)
            val taskDataListJson = gson.toJson(taskdatalist)
            editor.putString("TaskData", taskDataListJson)
            editor.apply()

            myIntent.putExtra("TaskData", newTask)
            setResult(Activity.RESULT_OK, myIntent)
            finish()
        }
    }

    fun back(view:View) {
        finish()
    }

    fun saveanother(view: View) {
        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        if (iteminput.text.toString() == "")
            message.text = "Task cannot be blank."
        else {
            val newTask = TaskClass(task = iteminput.text.toString(), details = detailinput.text.toString())
            taskdatalist.add(newTask)
            val taskDataListJson = gson.toJson(taskdatalist)
            editor.putString("TaskData", taskDataListJson)
            editor.apply()

            myIntent.putExtra("TaskData", newTask)
            clear()
            setResult(Activity.RESULT_OK, myIntent)
        }

    }

    fun clear() {
        iteminput.text.clear()
        detailinput.text.clear()
        message.text = ""
    }
}
