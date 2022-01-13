package com.example.mainactivity

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mainactivity.db.ArticleDao
import com.example.mainactivity.db.ArticleDatabase
import com.example.mainactivity.models.Article
import com.example.mainactivity.models.Source
import com.example.mainactivity.repository.NewsRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.runner.RunWith
import java.io.IOException
import org.hamcrest.collection.IsCollectionWithSize.hasSize


@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var articleDao: ArticleDao
    private lateinit var db: ArticleDatabase
   // private lateinit var repo: NewsRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ArticleDatabase::class.java).build()
        articleDao = db.getArticleDao()
    }
    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
    @Test
    @Throws(Exception::class)
     fun writeArticleAndReadInList() = runBlocking{
        val source=Source("source","source")
        val article = Article(123, "Rayan", "fdgdfgdf", "fdgdgffgdf", "2011", source, "dsfsdfsd","dfgdfg" ,"sdsqds")
        articleDao.upsert(article)


    }


}