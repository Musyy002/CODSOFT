package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        var btn = findViewById<Button>(R.id.planbtn)

        btn.setOnClickListener({
            startActivity(Intent(this,MainUserInterface::class.java))
        })
    }
}