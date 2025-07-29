package com.anggriawans.userseduhspace.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.anggriawans.userseduhspace.DineInActivity
import com.anggriawans.userseduhspace.PayOutActivity
import com.anggriawans.userseduhspace.adapter.CartAdapter
import com.anggriawans.userseduhspace.databinding.FragmentCartBinding
import com.anggriawans.userseduhspace.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartFragment : Fragment(), CartAdapter.OnQuantityChangeListener {

    private lateinit var binding: FragmentCartBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userId: String
    private lateinit var cartAdapter: CartAdapter

    // Data lists
    private lateinit var coffeeNames: MutableList<String>
    private lateinit var coffeePrices: MutableList<String>
    private lateinit var coffeeDescriptions: MutableList<String>
    private lateinit var coffeeImagesUri: MutableList<String>
    private lateinit var coffeeIngredients: MutableList<String>
    private lateinit var coffeeQuantities: MutableList<Int>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""

        initializeLists()
        retrieveCartItems()

        binding.processedButton.setOnClickListener {
            showOrderTypeDialog()
        }

        return binding.root
    }

    private fun initializeLists() {
        coffeeNames = mutableListOf()
        coffeePrices = mutableListOf()
        coffeeDescriptions = mutableListOf()
        coffeeImagesUri = mutableListOf()
        coffeeIngredients = mutableListOf()
        coffeeQuantities = mutableListOf()
    }

    private fun retrieveCartItems() {
        val cartReference = database.reference.child("user").child(userId).child("CartItems")

        cartReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                clearLists()

                for (cartSnapshot in snapshot.children) {
                    val cartItem = cartSnapshot.getValue(CartItems::class.java)
                    cartItem?.let {
                        coffeeNames.add(it.coffeeName ?: "")
                        coffeePrices.add(it.coffeePrice ?: "")
                        coffeeDescriptions.add(it.coffeeDescription ?: "")
                        coffeeImagesUri.add(it.coffeeImage ?: "")
                        coffeeIngredients.add(it.coffeeIngredient ?: "")
                        coffeeQuantities.add(it.coffeeQuantity ?: 1)
                    }
                }

                setupRecyclerView()
                updateTotalItems()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load cart: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun clearLists() {
        coffeeNames.clear()
        coffeePrices.clear()
        coffeeDescriptions.clear()
        coffeeImagesUri.clear()
        coffeeIngredients.clear()
        coffeeQuantities.clear()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            requireContext(),
            coffeeNames,
            coffeePrices,
            coffeeImagesUri,
            coffeeDescriptions,
            coffeeQuantities,
            coffeeIngredients,
            this
        )

        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    override fun onQuantityChanged(newQuantities: List<Int>) {
        coffeeQuantities.clear()
        coffeeQuantities.addAll(newQuantities)
        updateTotalItems()
    }

    private fun updateTotalItems() {
        val totalItems = coffeeQuantities.sum()
        binding.totalItemsText.text = totalItems.toString()
    }

    private fun showOrderTypeDialog() {
        if (coffeeNames.isEmpty()) {
            Toast.makeText(context, "Keranjang belanja kosong", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Pilih Jenis Pesanan")
            .setItems(arrayOf("Dine-In", "Delivery")) { _, which ->
                when (which) {
                    0 -> proceedToOrder("Dine-In")
                    1 -> proceedToOrder("Delivery")
                }
            }
            .show()
    }

    private fun proceedToOrder(orderType: String) {
        val intent = when (orderType) {
            "Dine-In" -> Intent(requireContext(), DineInActivity::class.java)
            "Delivery" -> Intent(requireContext(), PayOutActivity::class.java)
            else -> return
        }

        intent.apply {
            putExtra("CoffeeItemName", ArrayList(coffeeNames))
            putExtra("CoffeeItemPrice", ArrayList(coffeePrices))
            putExtra("CoffeeItemDescription", ArrayList(coffeeDescriptions))
            putExtra("CoffeeItemImage", ArrayList(coffeeImagesUri))
            putExtra("CoffeeItemIngredient", ArrayList(coffeeIngredients))
            putExtra("CoffeeItemQuantities", ArrayList(cartAdapter.getUpdateItemsQuantities()))
        }

        startActivity(intent)
    }
}