package com.example.mainactivity

import androidx.test.rule.ActivityTestRule
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
    var refUsers: DatabaseReference? = null

    @Mock
    private lateinit var firebaseAuth: FirebaseAuth
    @Mock
    private lateinit var authResultTask: Task<AuthResult>
    @Mock
    private lateinit var mockResult: AuthResult
    @Mock
    private lateinit var mockFirebaseUser: FirebaseUser
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
                email.setText("thor@thor.com")
                pass.setText("123456")
                refUsers = FirebaseDatabase.getInstance("https://chatappcustomandroid-default-rtdb.europe-west1.firebasedatabase.app/").reference.child("users").child("ZtpdrC8KOlWrExMi3TrlYVcU7Af2")

                val mockUser = User("ZtpdrC8KOlWrExMi3TrlYVcU7Af2", "voldemort", "fsdfdsf", "voldemort")
                MockitoAnnotations.initMocks(this)

                `when`(firebaseAuth.signInWithEmailAndPassword(email.text.toString(), pass.text.toString()))
                    .thenReturn(authResultTask)

                `when`(mockResult.user)
                    .thenReturn(mockFirebaseUser)

                refUsers!!.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val user: User? = snapshot.getValue(User::class.java)
                            assertEquals(user?.uid, mockUser.uid)
                        }
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
                refUsers =
                    FirebaseDatabase.getInstance("https://chatappcustomandroid-default-rtdb.europe-west1.firebasedatabase.app/").reference.child(
                        "users"
                    ).child("ZtpdrC8KOlWrExMi3TrlYVcU7Af2")

                val mockUser = User("pdrC8KOlWrExMi3TrlYVcU7Af2", "voldemort", "fsdfdsf", "voldemort")
                MockitoAnnotations.initMocks(this)

                `when`(
                    firebaseAuth.signInWithEmailAndPassword(
                        email.text.toString(),
                        pass.text.toString()
                    )
                )
                    .thenReturn(authResultTask)

                `when`(mockResult.user)
                    .thenReturn(mockFirebaseUser)

                refUsers!!.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val user: User? = snapshot.getValue(User::class.java)
                            assertNotEquals(user?.uid, mockUser.uid)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        )
    }
}
