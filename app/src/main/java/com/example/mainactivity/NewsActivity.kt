package com.example.mainactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar

class NewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newsactivity)


        val toolbar: Toolbar =findViewById(R.id.toolbar_news)
        setSupportActionBar(toolbar)
        supportActionBar!!.title="Nieuws"
    }
}