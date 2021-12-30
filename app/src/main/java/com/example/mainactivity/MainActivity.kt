package com.example.mainactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonRegister.setOnClickListener {
            val email=emailRegister.text.toString()
            val password=passwordRegister.text.toString()

            Log.d("MainActivity","email is"+email)
            Log.d("MainActivity","password is"+password)
        }

        alreadyAccounttextView.setOnClickListener {
            Log.d("MainActivity","try to show login")
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}