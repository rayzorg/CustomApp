package com.example.mainactivity.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mainactivity.ChatsFragment
import com.example.mainactivity.SearchFragment
import com.example.mainactivity.SettingsFragment

class ViewPagerAdapter(fragmentManager:FragmentManager,lifecycle:Lifecycle)
    :FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
       return when (position) {
            0 -> {
                ChatsFragment()
            }
            1 -> {
                SearchFragment()
            }
            2 -> {
                SettingsFragment()
            }
            else->{
                Fragment()
            }
        }
    }
}
