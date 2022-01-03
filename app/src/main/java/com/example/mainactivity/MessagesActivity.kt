package com.example.mainactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import com.example.mainactivity.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import android.R
import android.graphics.ImageDecoder
import androidx.core.net.toUri
import com.example.mainactivity.models.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_messages.*


class MessagesActivity : AppCompatActivity() {
    companion object {
        var currentUser:User? = null
    }

    var refUsers:DatabaseReference?=null
    var firebaseUser:FirebaseUser? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.mainactivity.R.layout.activity_messages)

        fetchCurrentUser()

        firebaseUser=FirebaseAuth.getInstance().currentUser
        refUsers=FirebaseDatabase.getInstance("https://chatappcustomandroid-default-rtdb.europe-west1.firebasedatabase.app/").reference.child("users").child(firebaseUser!!.uid)

        val toolbar:Toolbar=findViewById(com.example.mainactivity.R.id.toolbar_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.title=""


        val tabLayout=findViewById<TabLayout>(com.example.mainactivity.R.id.tab_layout)
        val viewPager2=findViewById<ViewPager2>(com.example.mainactivity.R.id.view_page)
        val adapter=ViewPagerAdapter(supportFragmentManager,lifecycle)

        viewPager2.adapter=adapter

        TabLayoutMediator(tabLayout,viewPager2){tab,position->
            when(position){
                0->{
                    tab.text="Chats"
                }
                1->{
                    tab.text="Zoeken"
                }
                2->{
                    tab.text="Instellingen"
                }
            }
        }.attach()

        verifyUserLoggedIn()

        refUsers!!.addValueEventListener((object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    val user:User?=snapshot.getValue(User::class.java)


                    Picasso.with(this@MessagesActivity).load(user!!.profileImageUrl).into(profile_image)
                }
            }

            override fun onCancelled(error: DatabaseError) {
               
            }

        }))


        profile_image.setOnClickListener {
            tab_layout.getTabAt(2)?.select()

        }

    }

    private fun fetchCurrentUser() {
        val uid=FirebaseAuth.getInstance().uid
        val ref=FirebaseDatabase.getInstance("https://chatappcustomandroid-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/users/${uid}")

        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                currentUser=p0.getValue(User::class.java)

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
    private fun verifyUserLoggedIn(){
       val uid = FirebaseAuth.getInstance().uid
       if (uid == null) {
           val intent = Intent(this, RegisterActivity::class.java)
           intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
           startActivity(intent)
       }
   }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){

            com.example.mainactivity.R.id.menu_sign_out->{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

       menuInflater.inflate(com.example.mainactivity.R.menu.nav_menu,menu)
        return true
    }


}