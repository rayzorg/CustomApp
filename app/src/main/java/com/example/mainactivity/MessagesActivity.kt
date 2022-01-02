package com.example.mainactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TableLayout
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_messages.*
import androidx.annotation.NonNull

import androidx.lifecycle.Lifecycle

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mainactivity.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import android.R





class MessagesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.mainactivity.R.layout.activity_messages)

      //  setSupportActionBar(toolbar_main)
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
                    tab.text="Search"
                }
                2->{
                    tab.text="Settings"
                }
            }
        }.attach()

       verifyUserLoggedIn()
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
            com.example.mainactivity.R.id.menu_new_message->{

            }
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