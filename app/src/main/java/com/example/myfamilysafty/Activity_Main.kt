package com.example.myfamilysafty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Activity_Main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottombar = findViewById<BottomNavigationView>(R.id.bottom_bar)

        bottombar.setOnItemSelectedListener {

            if(it.itemId == R.id.nav_guard)
            {
                inflateFragment(GuardFragment.newInstance())
            } else if(it.itemId == R.id.nav_home)
            {
                inflateFragment(HomeFragment.newInstance())
            } else if(it.itemId == R.id.nav_dashboard)
            {
                inflateFragment(DashboardFragment.newInstance())
            }
            else{
                inflateFragment(ProfileFragment.newInstance())
            }

            true
        }
    }

    private fun inflateFragment(newInstance : Fragment) {
        val transaction  = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,newInstance)
        transaction.commit()
    }


}