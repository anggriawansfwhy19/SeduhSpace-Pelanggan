package com.anggriawans.userseduhspace

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.anggriawans.userseduhspace.adapter.CoffeeMenuAdapter
import com.anggriawans.userseduhspace.databinding.ActivitySurveyBinding
import com.anggriawans.userseduhspace.model.MenuItem
import com.google.firebase.database.*
import kotlin.random.Random

class SurveyActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySurveyBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        val surveyResult = intent.getBundleExtra("surveys")

        binding.backButton.setOnClickListener {
            finish()
        }
        binding.progressBarActivitySurvey.visibility = View.VISIBLE


        Handler().postDelayed({
            retrieveRandomCoffeeMenus()
            binding.progressBarActivitySurvey.visibility = View.INVISIBLE
        }, 3000) // Delay 3 detik sebelum mengambil menu

    }

    private fun retrieveRandomCoffeeMenus() {
        database = FirebaseDatabase.getInstance()
        val coffeeRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        coffeeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (coffeeSnapshot in snapshot.children) {
                    val menuItem = coffeeSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }

                // Randomize items more efficiently
                val randomIndices = List(5) { Random.nextInt(0, menuItems.size) }
                val subsetMenuItem = randomIndices.map { menuItems[it] }

                setSurveysItemsAdapter(subsetMenuItem)
                binding.progressBarActivitySurvey.visibility = View.INVISIBLE
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error here
                Log.e("SurveyActivity", "Error retrieving menu items:", error.toException())
                // Show an error message to the user
            }
        })
    }

    private fun randomSurveysItems() {
        val index = menuItems.indices.toList().shuffled()
        val numItemToShow = 5
        val subsetMenuItem = index.take(numItemToShow).map { menuItems[it] }

        // Pass 'this' as the context
        setSurveysItemsAdapter(subsetMenuItem)
    }

    private fun setSurveysItemsAdapter(subsetMenuItem: List<MenuItem>) {
        val adapter = CoffeeMenuAdapter(subsetMenuItem, this)
        binding.surveyRv.layoutManager = LinearLayoutManager(this)
        binding.surveyRv.adapter = adapter
    }

}


