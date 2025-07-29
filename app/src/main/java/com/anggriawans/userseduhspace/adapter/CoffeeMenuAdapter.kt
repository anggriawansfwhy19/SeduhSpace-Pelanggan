package com.anggriawans.userseduhspace.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anggriawans.userseduhspace.R
import com.anggriawans.userseduhspace.model.MenuItem
import com.anggriawans.userseduhspace.DetailsActivity  // Tambahkan import untuk DetailsActivity
import com.bumptech.glide.Glide

class CoffeeMenuAdapter(private val menuItems: List<MenuItem>, private val context: Context) :
    RecyclerView.Adapter<CoffeeMenuAdapter.CoffeeMenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeMenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return CoffeeMenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoffeeMenuViewHolder, position: Int) {
        val menuItem = menuItems[position]

        holder.menuNameTextView.text = menuItem.coffeeName
        holder.menuPriceTextView.text = "Rp. ${menuItem.coffeePrice}"
        Glide.with(context).load(menuItem.coffeeImage).into(holder.menuImageView)

        // Tambahkan listener untuk membuka DetailsActivity
        holder.itemView.setOnClickListener {
            openDetailsActivity(position)
        }
    }

    override fun getItemCount(): Int = menuItems.size

    class CoffeeMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val menuNameTextView: TextView = itemView.findViewById(R.id.menuName)
        val menuPriceTextView: TextView = itemView.findViewById(R.id.menuPriceSurvey)
        val menuImageView: ImageView = itemView.findViewById(R.id.menuImageSurvey)
    }

    private fun openDetailsActivity(position: Int) {
        val menuItem = menuItems[position]

        // Intent untuk membuka DetailsActivity dan mengirimkan data
        val intent = Intent(context, DetailsActivity::class.java).apply {
            putExtra("MenuItemName", menuItem.coffeeName)
            putExtra("MenuItemImage", menuItem.coffeeImage)
            putExtra("MenuItemDescription", menuItem.coffeeDescription)
            putExtra("MenuItemIngredients", menuItem.coffeeIngredients)
            putExtra("MenuItemPrice", menuItem.coffeePrice)
        }

        context.startActivity(intent)
    }
}
