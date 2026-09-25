package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var prefsManager: SharedPrefsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefsManager = SharedPrefsManager(this)
        bottomNav = findViewById(R.id.bottom_navigation)

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment())
                R.id.nav_search -> loadFragment(SearchFragment())
                R.id.nav_favorites -> loadFragment(FavoritesFragment())
                R.id.nav_cart -> loadFragment(CartFragment())
                R.id.nav_orders -> loadFragment(OrdersFragment())
            }
            true
        }
        
        updateCartBadge()
    }
    
    fun switchToOrders() {
        bottomNav.selectedItemId = R.id.nav_orders
    }

    fun loadFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    fun updateCartBadge() {
        val count = prefsManager.getCartCount()
        if (count > 0) {
            val badge = bottomNav.getOrCreateBadge(R.id.nav_cart)
            badge.isVisible = true
            badge.number = count
        } else {
            bottomNav.removeBadge(R.id.nav_cart)
        }
    }
}