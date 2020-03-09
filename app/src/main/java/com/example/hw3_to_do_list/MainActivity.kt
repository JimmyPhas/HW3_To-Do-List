package com.example.hw3_to_do_list

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val Tag = "MainActivity"
    private val TASK_FILE = "MyTasks"
    private val REQUEST_CODE = 1337
    private val taskdatalist = mutableListOf<TaskClass>()
    private val taskstring = mutableListOf<String>()
    private val completedatalist = mutableListOf<TaskClass>()
    lateinit var myAdapter: ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences(TASK_FILE, MODE_PRIVATE)
        val taskData = sharedPreferences.getString("TaskData", "")?:""
        val completeData = sharedPreferences.getString("CompletedData", "")?:""

        if (taskData.isNotEmpty()) {
            taskdatalist.clear()
            taskstring.clear()
            val gson = Gson()
            val sType = object : TypeToken<List<TaskClass>>() {}.type
            val savedTaskList = gson.fromJson<List<TaskClass>>(taskData, sType)

            for (T in savedTaskList) {
                taskdatalist.add(T)
                taskstring.add(T.task)
            }
        }
        if (completeData.isNotEmpty()) {
            completedatalist.clear()
            val gson = Gson()
            val sType = object : TypeToken<List<TaskClass>>() {}.type
            val savedCompleteList = gson.fromJson<List<TaskClass>>(completeData, sType)

            for (C in savedCompleteList) {
                completedatalist.add(C)
            }
        }

        itemlist.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemIdAtPosition(position).toString()

            val detailIntent = Intent(this, Details::class.java)
            detailIntent.putExtra("taskdata", taskdatalist[selectedItem.toInt()])
            startActivity(detailIntent)
        }

        itemlist.setOnItemLongClickListener {parent, view, position, id ->
            val selectedItem = parent.getItemIdAtPosition(position).toString()
            val messagetask = taskstring[selectedItem.toInt()]
            var toast = Toast.makeText(this, "$messagetask Completed", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP, 10, 150)
            toast.show()

            val completeCheck = sharedPreferences.getString("CompletedData", "")?:""
            if (completeCheck.isNotEmpty()) {
                completedatalist.clear()
                val gson = Gson()
                val sType = object : TypeToken<List<TaskClass>>() {}.type
                val savedCompleteList = gson.fromJson<List<TaskClass>>(completeCheck, sType)

                for (C in savedCompleteList) {
                    completedatalist.add(C)
                }
            }

            completedatalist.add(taskdatalist[selectedItem.toInt()])
            taskdatalist.removeAt(position)
            taskstring.removeAt(position)
            save()
            myAdapter.notifyDataSetChanged()
            if (taskstring.isEmpty() == true) {
                toast = Toast.makeText(this, "All tasks completed", Toast.LENGTH_LONG)
                toast.show()
            }
            return@setOnItemLongClickListener true
        }


        myAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, taskstring)
        itemlist.adapter = myAdapter

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            val sharedPreferences = getSharedPreferences(TASK_FILE, MODE_PRIVATE)
            val taskdata = sharedPreferences.getString("TaskData", "")?:""
            val completeData = sharedPreferences.getString("CompletedData", "")?:""

            if (taskdata.isNotEmpty()) {
                taskdatalist.clear()
                taskstring.clear()
                val gson = Gson()
                val sType = object : TypeToken<List<TaskClass>>() {}.type
                val savedTaskList = gson.fromJson<List<TaskClass>>(taskdata, sType)

                for (T in savedTaskList) {
                    taskdatalist.add(T)
                    taskstring.add(T.task)
                }
            }
            if (completeData.isNotEmpty()) {
                completedatalist.clear()
                val gson = Gson()
                val sType = object : TypeToken<List<TaskClass>>() {}.type
                val savedCompleteList = gson.fromJson<List<TaskClass>>(completeData, sType)

                for (T in savedCompleteList) {
                    completedatalist.add(T)
                }
            }

            itemlist.adapter = myAdapter
            myAdapter.notifyDataSetChanged()
        }
    }

    fun viewcomplete(view:View) {
        val completeIntent = Intent(this, CompleteTasks::class.java)
        startActivityForResult(completeIntent, REQUEST_CODE)
    }

    fun additem(view:View) {
        val editIntent = Intent(this, EditItem::class.java)
        startActivityForResult(editIntent, REQUEST_CODE)

    }

    fun save(){
        val sharedPreferences = getSharedPreferences(TASK_FILE, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()

        val taskDataListJson = gson.toJson(taskdatalist)
        val completeDataListJson = gson.toJson(completedatalist)

        editor.putString("TaskData", taskDataListJson)
        editor.putString("CompletedData", completeDataListJson)
        editor.apply()

    }

}
