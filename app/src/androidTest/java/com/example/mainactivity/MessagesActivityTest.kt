package com.example.mainactivity

import androidx.test.rule.ActivityTestRule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.Exception

@RunWith(JUnit4::class)
class MessagesActivityTest
var messagesActivity: MessagesActivity? = null

@get:Rule
val mActivityTestRule: ActivityTestRule<MessagesActivity> = ActivityTestRule(
    MessagesActivity::class.java
)
@Before
@Throws(Exception::class)
fun setUp() {
    messagesActivity = mActivityTestRule.activity
}
