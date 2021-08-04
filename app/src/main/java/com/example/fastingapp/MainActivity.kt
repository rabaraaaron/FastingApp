package com.example.fastingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nav1 = findViewById<Button>(R.id.fastmenubutton)
        val intent = Intent(this, FastActivity::class.java)

        nav1.setOnClickListener{
            startActivity(intent)
        }
    }


}