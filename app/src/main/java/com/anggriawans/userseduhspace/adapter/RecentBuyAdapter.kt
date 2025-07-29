package com.anggriawans.userseduhspace.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anggriawans.userseduhspace.databinding.RecentBuyItemBinding
import com.bumptech.glide.Glide

class RecentBuyAdapter(
    private var context: Context,
    private var coffeeNameList: ArrayList<String>,
    private var coffeeImageList: ArrayList<String>,
    private var coffeePriceList: ArrayList<String>,
    private var coffeeQuantityList: ArrayList<Int>,

    ) : RecyclerView.Adapter<RecentBuyAdapter.RecentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        val binding =
            RecentBuyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentViewHolder(binding)
    }

    override fun getItemCount(): Int = coffeeNameList.size

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class RecentViewHolder(private val binding: RecentBuyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

            binding.apply {
                coffeeName.text = coffeeNameList[position]
                coffeePrice.text = coffeePriceList[position]
                coffeeQuantity.text = coffeeQuantityList[position].toString()
                val uriString = coffeeImageList[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(coffeeImage)
            }
        }

    }
}