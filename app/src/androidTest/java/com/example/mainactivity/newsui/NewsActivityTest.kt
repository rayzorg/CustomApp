package com.example.mainactivity.newsui

import android.content.res.Resources
import android.view.Menu
import androidx.test.annotation.UiThreadTest
import androidx.test.filters.SmallTest
import androidx.test.rule.ActivityTestRule
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class NewsActivityTest {
    private var newsActivity: NewsActivity? = null
    @get:Rule
    val mActivityTestRule: ActivityTestRule<NewsActivity> = ActivityTestRule(
        NewsActivity::class.java
    )
    private val menuContentItemIds = intArrayOf(
       com.example.mainactivity.R.id.breakingNewsFragment , com.example.mainactivity.R.id.savedNewsFragment, com.example.mainactivity.R.id.searchNewsFragment
    )
    private var menuStringContent: Map<Int, String>? = null

    private var bottomNavigation: BottomNavigationView? = null


    @Before
    @Throws(Exception::class)
    fun setUp() {
        newsActivity = mActivityTestRule.activity
        val activity: NewsActivity = mActivityTestRule.activity
        activity.setTheme(com.example.mainactivity.R.style.Theme_MaterialComponents_Light)
        bottomNavigation = activity.findViewById(com.example.mainactivity.R.id.bottomNavigationView)
        val res: Resources = activity.resources
        menuStringContent = HashMap(menuContentItemIds.size)
        (menuStringContent as HashMap<Int, String>)[com.example.mainactivity.R.id.breakingNewsFragment] =
            res.getString(com.example.mainactivity.R.string.breaking_news)
        (menuStringContent as HashMap<Int, String>)[com.example.mainactivity.R.id.savedNewsFragment] =
            res.getString(com.example.mainactivity.R.string.opgeslagen)
        (menuStringContent as HashMap<Int, String>)[com.example.mainactivity.R.id.searchNewsFragment] = res.getString(com.example.mainactivity.R.string.zoeken)
    }

    @UiThreadTest
    @Test
    @SmallTest
    fun testAddItemsWithoutMenuInflation() {
        val navigation = BottomNavigationView(mActivityTestRule.activity)
       // mActivityTestRule.activity.setContentView(navigation)
        navigation.menu.add("Item1")
        navigation.menu.add("Item2")
        assertEquals(2, navigation.menu.size())
        navigation.menu.removeItem(0)
        navigation.menu.removeItem(0)
        assertEquals(0, navigation.menu.size())
    }
    @Test
    @SmallTest
    fun testBasics() {
        // Check the contents of the Menu object
        val menu: Menu = bottomNavigation!!.menu
        Assert.assertNotNull("Menu should not be null", menu)
        assertEquals(
            "Should have matching number of items",
            menuContentItemIds.size,
            menu.size()
        )
        /* for ((index,value) in MENU_CONTENT_ITEM_IDS.withIndex() ) {
             val currItem: MenuItem = menu[index]
             assertEquals("ID for Item #$index", index, value)
         }*/
    }
}
