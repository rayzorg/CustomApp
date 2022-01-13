package com.example.mainactivity

import android.widget.EditText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.mainactivity.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import java.lang.Exception
import org.junit.Assert.*
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class RegisterActivityTest {

    var registerActivity: RegisterActivity? = null

    @Mock
    private lateinit var firebaseAuth: FirebaseAuth
    @Mock
    private  var fireDatabase: FirebaseDatabase? = null
    @Mock
    private lateinit var authResultTask: Task<AuthResult>
    @get:Rule
    public val mActivityTestRule: ActivityTestRule<RegisterActivity> = ActivityTestRule(
        RegisterActivity::class.java
    )

    @Before
    @Throws(Exception::class)
    fun setUp() {
        registerActivity = mActivityTestRule.activity
    }
    @Test
    fun testEmailPasswordNotEmpty() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(
            Runnable {
                val email = registerActivity!!.findViewById<EditText>(R.id.emailRegister)
                val pass = registerActivity!!.findViewById<EditText>(R.id.passwordRegister)
                email.setText("Email@email.com")
                pass.setText("123456")
                Assert.assertEquals(email.text.toString(), "Email@email.com")
                Assert.assertEquals(pass.text.toString(), "123456")
            }
        )
    }
    @Test
    fun testEmailPasswordEmpty() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(
            Runnable {
                val email = registerActivity!!.findViewById<EditText>(R.id.emailRegister)
                val pass = registerActivity!!.findViewById<EditText>(R.id.passwordRegister)
                Assert.assertEquals(email.text.toString(), "")
                Assert.assertEquals(pass.text.toString(), "")
            }
        )
    }
    @Test
    fun testUserCreated() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(
            Runnable {
                val email = registerActivity!!.findViewById<EditText>(R.id.emailRegister)
                val pass = registerActivity!!.findViewById<EditText>(R.id.passwordRegister)
                val username=registerActivity!!.findViewById<EditText>(R.id.usernameRegister)
                email.setText("Email@email.com")
                pass.setText("123456")
                username.setText("easy")
                val mockUser = User("hvhdftrdfdgdfgfdgdg", username.text.toString(), "fsdfdsf", username.text.toString())
                val uid = FirebaseAuth.getInstance().uid
                val ref = fireDatabase?.getReference("/users/$uid")
                MockitoAnnotations.initMocks(this)
                Mockito.`when`(
                    firebaseAuth.createUserWithEmailAndPassword(email.text.toString(), pass.text.toString()))
                    .thenReturn(authResultTask)
                authResultTask.addOnCompleteListener {
                    ref?.setValue(mockUser)
                }
                ref?.addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user: User? = snapshot.getValue(User::class.java)
                        assertEquals(user?.username,mockUser.username)
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        )
    }
    @Test
    fun testUserNotCreated() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(
            Runnable {
                val email = registerActivity!!.findViewById<EditText>(R.id.emailRegister)
                val pass = registerActivity!!.findViewById<EditText>(R.id.passwordRegister)
                email.setText("Email")
                pass.setText("123456")
                val uid = FirebaseAuth.getInstance().uid
                val ref = fireDatabase?.getReference("/users/$uid")
                MockitoAnnotations.initMocks(this)
                Mockito.`when`(
                    firebaseAuth.createUserWithEmailAndPassword(email.text.toString(), pass.text.toString()))
                    .thenReturn(authResultTask)
                authResultTask.addOnCompleteListener {
                    if (!it.isSuccessful) {
                        assertNull(ref)
                        return@addOnCompleteListener

                    }
                }
            }
        )
    }
}
