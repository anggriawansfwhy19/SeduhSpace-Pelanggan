package com.anggriawans.userseduhspace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.anggriawans.userseduhspace.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize navigation
        val navController = findNavController(R.id.fragmentContainerView)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Setup navigation safely
        try {
            bottomNav.setupWithNavController(navController)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

        // Handle notification button click
        binding.notificationButton.setOnClickListener {
            if (!isFinishing && !isDestroyed) {
                val bottomSheetDialog = NotificationBottomFragment()
                bottomSheetDialog.show(supportFragmentManager, "NotificationBottomFragment")
            }
        }
    }
}