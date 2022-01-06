package com.example.mainactivity.newsui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mainactivity.MessagesActivity
import com.example.mainactivity.R
import com.example.mainactivity.db.ArticleDatabase
import com.example.mainactivity.repository.NewsRepository
import com.example.mainactivity.views.NewsViewModel
import kotlinx.android.synthetic.main.activity_news.*
class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val toolbar: Toolbar = findViewById(R.id.toolbar_news)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Nieuws"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@NewsActivity, MessagesActivity::class.java)
            startActivity(intent)
            finish()
        }

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
    }
}
