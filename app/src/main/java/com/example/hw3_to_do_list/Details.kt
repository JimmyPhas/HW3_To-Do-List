package com.example.hw3_to_do_list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_details.*

class Details : AppCompatActivity() {

    private val Tag = "DetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val (taskName, taskDetail ) = intent.getSerializableExtra("taskdata") as TaskClass
        taskitem.text = taskName
        taskdetails.text = taskDetail
    }

    fun back(view:View) {
        finish()
    }
}
