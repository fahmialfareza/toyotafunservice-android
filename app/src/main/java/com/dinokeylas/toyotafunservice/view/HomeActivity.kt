package com.dinokeylas.toyotafunservice.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.dinokeylas.toyotafunservice.ChatBotFragment
import com.dinokeylas.toyotafunservice.ChatBotFragment2
import com.dinokeylas.toyotafunservice.NotificationFragment
import com.dinokeylas.toyotafunservice.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val menu: Menu = findViewById<BottomNavigationView>(R.id.bottom_navigation).menu
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        //initialize fragment to show first
        selectedMenu(menu.getItem(0))

        bottomNavigation.setOnNavigationItemSelectedListener {
            item: MenuItem -> selectedMenu(item)
            false
        }

    }

    private fun selectedMenu(menuItem: MenuItem){
        menuItem.isChecked = true
        when(menuItem.itemId){
            R.id.navigation_home -> showFragment(HomeFragment.newInstance())
//            R.id.navigation_notification -> showFragment(NotificationFragment.newInstance())
            R.id.navigation_chatbot -> showFragment(ChatBotFragment2.newInstance())
            R.id.navigation_profile -> showFragment(ProfileFragment.newInstance())
        }
    }

    private fun showFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }

}
