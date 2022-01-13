package com.example.mainactivity.newsui

import androidx.test.rule.ActivityTestRule
import com.example.mainactivity.MessagesActivity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.Exception

@RunWith(JUnit4::class)
class NewsActivityTest{
    var newsActivity: NewsActivity? = null
    @get:Rule
    val mActivityTestRule: ActivityTestRule<NewsActivity> = ActivityTestRule(
        NewsActivity::class.java
    )
    @Before
    @Throws(Exception::class)
    fun setUp() {
        newsActivity = mActivityTestRule.activity
    }
}
