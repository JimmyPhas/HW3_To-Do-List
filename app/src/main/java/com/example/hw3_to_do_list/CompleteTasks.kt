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

class CompleteTasks : AppCompatActivity() {

    private val Tag = "CompleteActivity"
    private val TASK_FILE = "MyTasks"
    private val completedatalist = mutableListOf<TaskClass>()
    private val completestringlist = mutableListOf<String>()
    lateinit var myAdapter: ArrayAdapter<String>
    private val myIntent = Intent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed)

        val sharedPreferences = getSharedPreferences(TASK_FILE, MODE_PRIVATE)
        val Cdatatasks = sharedPreferences.getString("CompletedData", "")?:""

        if (Cdatatasks.isNotEmpty()) {
            completedatalist.clear()
            completestringlist.clear()
            val gson = Gson()
            val sType = object : TypeToken<List<TaskClass>>() {}.type
            val savedTaskDataList = gson.fromJson<List<TaskClass>>(Cdatatasks, sType)

            for (T in savedTaskDataList) {
                completedatalist.add(T)
                completestringlist.add(T.task)
            }
        }

        itemlist.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemIdAtPosition(position).toString()

            val detailIntent = Intent(this, Details::class.java)
            detailIntent.putExtra("taskdata", completedatalist[selectedItem.toInt()])
            startActivity(detailIntent)
        }


        itemlist.setOnItemLongClickListener {parent, view, position, id ->
            val selectedItem = parent.getItemIdAtPosition(position).toString()
            val messagetask = completestringlist[selectedItem.toInt()]
            val toast = Toast.makeText(this, "$messagetask Deleted", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP, 10, 150)
            toast.show()
            completedatalist.removeAt(position)
            completestringlist.removeAt(position)
            save()
            myAdapter.notifyDataSetChanged()
            return@setOnItemLongClickListener true
        }

        myAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, completestringlist)
        itemlist.adapter = myAdapter
    }

    fun back(view: View) {
        finish()
    }

    fun save(){
        val sharedPreferences = getSharedPreferences(TASK_FILE, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()

        val completeDataListJson = gson.toJson(completedatalist)
        editor.putString("CompletedData", completeDataListJson)
        editor.apply()

    }

}
