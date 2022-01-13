package com.example.mainactivity

import androidx.test.rule.ActivityTestRule
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.junit.Rule
import java.lang.Exception
import android.widget.EditText
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.example.mainactivity.models.User
import com.google.firebase.database.*
import org.junit.Assert.*
import org.mockito.Mockito.`when`

class LoginActivityTest {
    var loginActivity: LoginActivity? = null
    @Mock
    private var firebaseAuth: FirebaseAuth? = null
    @Mock
    private var fireDatabase: FirebaseDatabase? = null
    @Mock
    private lateinit var authResultTask: Task<AuthResult>
    @Mock
    private lateinit var authResultTaskLogin: Task<AuthResult>
    @get:Rule
    public val mActivityTestRule: ActivityTestRule<LoginActivity> = ActivityTestRule(
        LoginActivity::class.java
    )

    @Before
    @Throws(Exception::class)
    fun setUp() {
        loginActivity = mActivityTestRule.activity
    }

    @Test
    fun testEmailPasswordNotEmpty() {
        getInstrumentation().runOnMainSync(
            Runnable {
                val email = loginActivity!!.findViewById<EditText>(R.id.emailLogin)
                val pass = loginActivity!!.findViewById<EditText>(R.id.passwordLogin)
                email.setText("Email@email.com")
                pass.setText("123456")
                assertEquals(email.text.toString(), "Email@email.com")
                assertEquals(pass.text.toString(), "123456")
            }
        )
    }
    @Test
    fun testEmailPasswordEmpty() {
        getInstrumentation().runOnMainSync(
            Runnable {
                val email = loginActivity!!.findViewById<EditText>(R.id.emailLogin)
                val pass = loginActivity!!.findViewById<EditText>(R.id.passwordLogin)
                assertEquals(email.text.toString(), "")
                assertEquals(pass.text.toString(), "")
            }
        )
    }
    @Test
    fun testUserLogin() {
        getInstrumentation().runOnMainSync(
            Runnable {
                val email = loginActivity!!.findViewById<EditText>(R.id.emailLogin)
                val pass = loginActivity!!.findViewById<EditText>(R.id.passwordLogin)
                email.setText("Email@email.com")
                pass.setText("123456")

                val mockUser = User("hvhdftrdfdgdfgfdgdg", "easy", "fsdfdsf", "easy")
                val uid = firebaseAuth?.uid
                val ref = fireDatabase?.getReference("/users/$uid")

                MockitoAnnotations.initMocks(this)
                `when`(
                    firebaseAuth?.createUserWithEmailAndPassword(email.text.toString(), pass.text.toString())
                )
                    .thenReturn(authResultTask)
                authResultTask.addOnCompleteListener {
                    ref?.setValue(mockUser)
                }
                `when`(firebaseAuth?.signInWithEmailAndPassword(email.text.toString(), pass.text.toString()))
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
    @Test
    fun testUserNotExist() {
        getInstrumentation().runOnMainSync(
            Runnable {
                val email = loginActivity!!.findViewById<EditText>(R.id.emailLogin)
                val pass = loginActivity!!.findViewById<EditText>(R.id.passwordLogin)
                email.setText("wade@thor.com")
                pass.setText("123456")

                val mockUser = User("hvhdgdg", "easy", "fsdfdsf", "easy")
                val uid = firebaseAuth?.uid
                val ref = fireDatabase?.getReference("/users/$uid")

                MockitoAnnotations.initMocks(this)
                `when`(firebaseAuth?.signInWithEmailAndPassword(email.text.toString(), pass.text.toString()))
                    .thenReturn(authResultTaskLogin)

                ref?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val user: User? = snapshot.getValue(User::class.java)
                            assertNotEquals(user?.username, mockUser.username)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        )
    }
}
