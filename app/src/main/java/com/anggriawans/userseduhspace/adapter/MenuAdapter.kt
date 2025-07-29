package com.anggriawans.userseduhspace.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anggriawans.userseduhspace.DetailsActivity
import com.anggriawans.userseduhspace.databinding.MenuItemBinding
import com.anggriawans.userseduhspace.model.MenuItem
import com.bumptech.glide.Glide

class MenuAdapter(
    private val menuItems : List<MenuItem>,
    private val requireContext: Context

) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuItems.size

    inner class MenuViewHolder(private val binding: MenuItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailsActivity(position)
                }
            }
        }

        private fun openDetailsActivity(position: Int) {
            val menuItem = menuItems[position]

            // a Intent to open details activity and pass data
            val intent = Intent(requireContext, DetailsActivity::class.java).apply {
                putExtra("MenuItemName", menuItem.coffeeName)
                putExtra("MenuItemImage", menuItem.coffeeImage)
                putExtra("MenuItemDescription", menuItem.coffeeDescription)
                putExtra("MenuItemIngredients", menuItem.coffeeIngredients)
                putExtra("MenuItemPrice", menuItem.coffeePrice)
            }

            requireContext.startActivity(intent)
        }
        // set data recyclerview items name, price, image
        fun bind(position: Int) {
            val menuItem = menuItems[position]
            binding.apply {
                menuCoffeeName.text = menuItem.coffeeName
                menuPrice.text = menuItem.coffeePrice
                val uri = Uri.parse(menuItem.coffeeImage)
                Glide.with(requireContext).load(uri).into(menuImage)
            }
        }
    }

}



