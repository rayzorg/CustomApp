package com.example.mainactivity

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mainactivity.db.ArticleDao
import com.example.mainactivity.db.ArticleDatabase
import com.example.mainactivity.models.Article
import com.example.mainactivity.models.Source
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import org.junit.Assert.*



@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var articleDao: ArticleDao
    private lateinit var db: ArticleDatabase
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ArticleDatabase::class.java
        ).allowMainThreadQueries().build()
        articleDao = db.getArticleDao()
    }
    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testAddArticle() = runBlocking {
        val source = Source("source", "source")
        val article = Article(123, "Rayan", "fdgdfgdf", "fdgdgffgdf", "2011", source, "dsfsdfsd", "dfgdfg", "sdsqds")

        articleDao.upsert(article)
        val one= articleDao.getArticles(1)

        assertNotNull(one.size)
    }
    @Test
    @Throws(Exception::class)
    fun testDeleteArticle() = runBlocking {
        val source = Source("source", "source")
        val article = Article(123, "Rayan", "fdgdfgdf", "fdgdgffgdf", "2011", source, "dsfsdfsd", "dfgdfg", "sdsqds")

        articleDao.upsert(article)
        articleDao.deleteArticle(article)
        val one= articleDao.getArticles(1)

        assertEquals(one.size,0)
    }
    @Test
    @Throws(Exception::class)
    fun testAllArticles() = runBlocking {
        val source = Source("source", "source")
        val article = Article(123, "Rayan", "fdgdfgdf", "fdgdgffgdf", "2011", source, "dsfsdfsd", "dfgdfg", "sdsqds")
        val article2 = Article(1234, "Rayanwil", "fdgdfgdf", "fdgdgffgdf", "2011", source, "dsfsdfsd", "dfgdfg", "sdsqds")

        articleDao.upsert(article)
        articleDao.upsert(article2)

        val one= articleDao.getArticles(2)

        assertEquals(one.size,2)
    }

}
