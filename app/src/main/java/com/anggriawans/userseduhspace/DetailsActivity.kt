package com.anggriawans.userseduhspace

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.anggriawans.userseduhspace.databinding.ActivityDetailsBinding
import com.anggriawans.userseduhspace.model.CartItems
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailsBinding
    private var coffeeName: String? = null
    private var coffeeImage: String? = null
    private var coffeeDescription: String? = null
    private var coffeeIngredients: String? = null
    private var coffeePrice: String? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        coffeeName = intent.getStringExtra("MenuItemName")
        coffeeDescription = intent.getStringExtra("MenuItemDescription")
        coffeeIngredients =  intent.getStringExtra("MenuItemIngredients")
        coffeePrice = intent.getStringExtra("MenuItemPrice")
        coffeeImage = intent.getStringExtra("MenuItemImage")

        with(binding){
            detailCoffeeName.text =coffeeName
            detailDescriptionTv.text = coffeeDescription
            detailIngredientsTv.text = coffeeIngredients
            Glide.with(this@DetailsActivity).load(Uri.parse(coffeeImage)).into(detailCoffeeImage)

        }

        binding.imageButton.setOnClickListener {
            finish()
        }
        binding.addItemButton.setOnClickListener {
            addItemToCart()
        }
    }

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid?:""

        // Create a cart items object
        val cartItem = CartItems(coffeeName.toString(), coffeePrice.toString(), coffeeDescription.toString(), coffeeImage.toString(), 1)

        //save data to cart item to firebase database
        database.child("user").child(userId).child("CartItems").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this, "Items added into cart Successfully😁", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Item Not Added😢", Toast.LENGTH_SHORT).show()
        }
    }
}