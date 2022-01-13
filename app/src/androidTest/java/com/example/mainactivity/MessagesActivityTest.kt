package com.example.mainactivity

import android.content.Intent
import android.widget.EditText
import androidx.core.content.ContextCompat.startActivity
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.mainactivity.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.lang.Exception

@RunWith(JUnit4::class)
class MessagesActivityTest{
    var messagesActivity: MessagesActivity? = null
    @Mock
    private var firebaseAuth: FirebaseAuth? = null
    @Mock
    private var fireDatabase: FirebaseDatabase? = null
    @Mock
    private lateinit var authResultTask: Task<AuthResult>
    @Mock
    private lateinit var authResultTaskLogin: Task<AuthResult>


    @get:Rule var activityScenarioRule = activityScenarioRule<MessagesActivity>()
    @get:Rule
    val mActivityTestRule: ActivityTestRule<MessagesActivity> = ActivityTestRule(
        MessagesActivity::class.java
    )
    @Before
    @Throws(Exception::class)
    fun setUp() {
        messagesActivity = mActivityTestRule.activity
    }
    @Test
    fun testUserLogin() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(
            Runnable {
                val mockUser = User("hvhdftrdfdgdfgfdgdg", "easy", "fsdfdsf", "easy")
                val uid = firebaseAuth?.uid
                val ref = fireDatabase?.getReference("/users/$uid")

                MockitoAnnotations.initMocks(this)
                Mockito.`when`(
                    firebaseAuth?.createUserWithEmailAndPassword("email@email.com", "123456"))
                    .thenReturn(authResultTask)
                authResultTask.addOnCompleteListener {
                    ref?.setValue(mockUser)
                }
                Mockito.`when`(
                    firebaseAuth?.signInWithEmailAndPassword(
                        "email@email.com", "123456"
                    )
                )
                    .thenReturn(authResultTaskLogin)

                ref?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user: User? = snapshot.getValue(User::class.java)
                        assertEquals(user?.uid, mockUser.uid)
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        )
    }
}
