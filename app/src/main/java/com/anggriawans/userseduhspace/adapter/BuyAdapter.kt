package com.anggriawans.userseduhspace.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anggriawans.userseduhspace.databinding.BuyItemBinding
import com.bumptech.glide.Glide

class BuyAdapter(
    private val buyAgainCoffeeName: MutableList<String>,
    private val buyAgainCoffeePrice: MutableList<String>,
    private val buyAgainCoffeeImage: MutableList<String>,
    private val requireContext: Context

) : RecyclerView.Adapter<BuyAdapter.BuyAgainViewHolder>() {

    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
        // Mengikat data ke tampilan
        holder.bind(
            buyAgainCoffeeName[position],
            buyAgainCoffeePrice[position],
            buyAgainCoffeeImage[position]
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding = BuyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BuyAgainViewHolder(binding)
    }

    override fun getItemCount(): Int = buyAgainCoffeeName.size

    inner class BuyAgainViewHolder(private val binding: BuyItemBinding) : RecyclerView.ViewHolder(binding.root) {
        // Metode untuk mengikat data ke tampilan
        fun bind(coffeeName: String, coffeePrice: String, coffeeImage: String) {
            binding.apply {
                buyCoffeeName.text = coffeeName
                buyCoffeePrice.text = coffeePrice
                val uriString = coffeeImage
                val uri = Uri.parse(uriString)
                Glide.with(requireContext).load(uri).into(binding.buyImage)
                // Implementasikan aksi tombol jika diperlukan
            }
        }
    }
}
