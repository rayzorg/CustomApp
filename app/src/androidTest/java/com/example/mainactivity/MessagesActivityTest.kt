package com.example.mainactivity

import androidx.test.rule.ActivityTestRule
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
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

