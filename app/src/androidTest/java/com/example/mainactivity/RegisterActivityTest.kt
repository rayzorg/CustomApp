package com.example.mainactivity

import android.widget.EditText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import java.lang.Exception
import org.junit.Assert.*
@RunWith(JUnit4::class)
class RegisterActivityTest {

    var registerActivity: RegisterActivity? = null

    @Mock
    private lateinit var firebaseAuth: FirebaseAuth
    @Mock
    private lateinit var authResultTask: Task<AuthResult>
    @Mock
    private lateinit var mockResult: AuthResult
    @Mock
    private lateinit var mockFirebaseUser: FirebaseUser
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
}
