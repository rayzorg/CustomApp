package com.example.mainactivity

import android.content.res.Resources
import android.view.Menu
import androidx.test.annotation.UiThreadTest
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.mainactivity.models.User
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.collections.HashMap
import kotlin.collections.Map
import kotlin.collections.set

@RunWith(JUnit4::class)
class MessagesActivityTest {
    private var messagesActivity: MessagesActivity? = null
    @Mock
    private var firebaseAuth: FirebaseAuth? = null
    @Mock
    private var fireDatabase: FirebaseDatabase? = null
    @Mock
    private lateinit var authResultTask: Task<AuthResult>
    @Mock
    private lateinit var authResultTaskLogin: Task<AuthResult>
    private val menuContentItemIds = intArrayOf(
        R.id.breakingNewsFragment, R.id.savedNewsFragment, R.id.searchNewsFragment
    )
    private var menuStringContent: Map<Int, String>? = null

    private var bottomNavigation: BottomNavigationView? = null

    @get:Rule
    val mActivityTestRule: ActivityTestRule<MessagesActivity> = ActivityTestRule(
        MessagesActivity::class.java
    )
    @Before
    @Throws(Exception::class)
    fun setUp() {
        messagesActivity = mActivityTestRule.activity
        val activity: MessagesActivity = mActivityTestRule.activity
        activity.setTheme(R.style.Theme_MaterialComponents_Light)
        bottomNavigation = activity.findViewById(R.id.bottomNavigationViewChat)
        val res: Resources = activity.resources
        menuStringContent = HashMap(menuContentItemIds.size)
        (menuStringContent as HashMap<Int, String>)[R.id.chatsFragment2] =
            res.getString(R.string.chats)
        (menuStringContent as HashMap<Int, String>)[R.id.searchFragment4] =
            res.getString(R.string.zoeken)
        (menuStringContent as HashMap<Int, String>)[R.id.searchNewsFragment] =
            res.getString(R.string.settings)
    }
    @Test
    fun testUserLogin() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val mockUser = User("hvhdftrdfdgdfgfdgdg", "easy", "fsdfdsf", "easy")
            val uid = firebaseAuth?.uid
            val ref = fireDatabase?.getReference("/users/$uid")

            MockitoAnnotations.initMocks(this)
            Mockito.`when`(
                firebaseAuth?.createUserWithEmailAndPassword("email@email.com", "123456")
            )
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
        assertNotNull("Menu should not be null", menu)
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
