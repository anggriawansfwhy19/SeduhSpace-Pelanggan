package com.anggriawans.userseduhspace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.anggriawans.userseduhspace.adapter.RecentBuyAdapter
import com.anggriawans.userseduhspace.databinding.ActivityRecentOrderItemsBinding
import com.anggriawans.userseduhspace.model.OrderDetails

class RecentOrderItemsActivity : AppCompatActivity() {

    private val binding: ActivityRecentOrderItemsBinding by lazy {
        ActivityRecentOrderItemsBinding.inflate(layoutInflater)
    }
    private lateinit var allCoffeeNames: ArrayList<String>
    private lateinit var allCoffeeImages: ArrayList<String>
    private lateinit var allCoffeePrices: ArrayList<String>
    private lateinit var allCoffeeQuantities: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }
        val recentBuyOrderItemList = intent.getSerializableExtra("RecentBuyOrderItemList") as? ArrayList<OrderDetails>

        recentBuyOrderItemList?.let { orderDetails ->
            if (orderDetails.isNotEmpty()) {
                val recentOrderItem = orderDetails[0]

                allCoffeeNames = recentOrderItem.coffeeNames as ArrayList<String>
                allCoffeeImages = recentOrderItem.coffeeImages as ArrayList<String>
                allCoffeePrices = recentOrderItem.coffeePrices as ArrayList<String>
                allCoffeeQuantities = recentOrderItem.coffeeQuantities as ArrayList<Int>
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        val rv = binding.rvRecentBuy
        rv.layoutManager = LinearLayoutManager(this)
        val adapter = RecentBuyAdapter(this, allCoffeeNames, allCoffeeImages, allCoffeePrices, allCoffeeQuantities)
        rv.adapter = adapter
    }
}