package com.anggriawans.userseduhspace.Fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.anggriawans.userseduhspace.RecentOrderItemsActivity
import com.anggriawans.userseduhspace.adapter.BuyAdapter
import com.anggriawans.userseduhspace.databinding.FragmentHistoryBinding
import com.anggriawans.userseduhspace.model.OrderDetails
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAdapter: BuyAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOrderItem: MutableList<OrderDetails> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        // Retrieve and display the user order history
        retrieveBuyHistory()

        // recent buy button on click
        binding.recentBuyItem.setOnClickListener {
            seeItemsRecentBuy()
        }
        binding.receivedButton.setOnClickListener {
            // Beri feedback visual saat diklik (opsional)
            it.isPressed = true
            Handler(Looper.getMainLooper()).postDelayed({ it.isPressed = false }, 200)

            // Lakukan aksi (contoh: update status di Firebase)
            updateOrderStatus()
        }
        return binding.root
    }

    private fun updateOrderStatus() {
        val itemPushKey = listOfOrderItem[0].itemPushKey
        val completeOrderReference = database.reference.child("CompletedOrder").child(itemPushKey!!)
        completeOrderReference.child("paymentReceived").setValue(true)
    }

    private fun seeItemsRecentBuy() {
        val intent = Intent(requireContext(), RecentOrderItemsActivity::class.java)
        intent.putExtra("RecentBuyOrderItemList", ArrayList(listOfOrderItem))
        startActivity(intent)
    }


    private fun retrieveBuyHistory() {
        binding.recentBuyItem.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid ?: ""

        val buyItemReferenece: DatabaseReference =
            database.reference.child("user").child(userId).child("BuyHistory")
        val shortingQuery = buyItemReferenece.orderByChild("currentTime")

        shortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children) {
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItem.add(it)
                    }
                }
                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty()) {
                    setDataInRecentBuyItem()
                    setPreviousBuyItemsRecyclerView()
                }
            }

            private fun setDataInRecentBuyItem() {
                binding.recentBuyItem.visibility = View.VISIBLE
                val recentOrderItem = listOfOrderItem.firstOrNull()
                recentOrderItem?.let {
                    with(binding) {
                        buyCoffeeName.text = it.coffeeNames?.firstOrNull() ?: ""
                        buyCoffeePrice.text = it.coffeePrices?.firstOrNull() ?: ""
                        val image = it.coffeeImages?.firstOrNull() ?: ""
                        val uri = Uri.parse(image)
                        Glide.with(requireContext()).load(uri).into(buyImage)

                        val isOrderIsAccepted = listOfOrderItem[0].orderAccepted
                        Log.d("TAG", "setDataInRecentBuyItem: $isOrderIsAccepted")
                        if (isOrderIsAccepted){
                            orderStatus.background.setTint(Color.GREEN)
                            receivedButton.visibility = View.VISIBLE
                        }
                    }
                }
            }

            private fun setPreviousBuyItemsRecyclerView() {
                val buyAgainCoffeeName = mutableListOf<String>()
                val buyAgainCoffeePrice = mutableListOf<String>()
                val buyAgainCoffeeImage = mutableListOf<String>()

                for (i in 1 until listOfOrderItem.size) {
                    val currentOrderItem = listOfOrderItem[i]
                    buyAgainCoffeeName.add(currentOrderItem.coffeeNames?.firstOrNull() ?: "")
                    buyAgainCoffeePrice.add(currentOrderItem.coffeePrices?.firstOrNull() ?: "")
                    buyAgainCoffeeImage.add(currentOrderItem.coffeeImages?.firstOrNull() ?: "")
                }

                val rv = binding.historyRecyclerView
                rv.layoutManager = LinearLayoutManager(requireContext())
                buyAdapter = BuyAdapter(
                    buyAgainCoffeeName,
                    buyAgainCoffeePrice,
                    buyAgainCoffeeImage,
                    requireContext()
                )
                rv.adapter = buyAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })
    }
}
