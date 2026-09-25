package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class DishAdapter(
    private var dishList: List<Dish>,
    private val onItemClick: (Dish) -> Unit
) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    fun filterList(filteredList: List<Dish>) {
        dishList = filteredList
        notifyDataSetChanged()
    }

    class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvDishName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvDishPrice)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDishDesc)
        val ivImage: ImageView = itemView.findViewById(R.id.ivDishImage)
        val btnFavorite: ImageView = itemView.findViewById(R.id.btnFavorite)
        val btnAddToCart: ImageView = itemView.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dish, parent, false)
        return DishViewHolder(view)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val dish = dishList[position]
        holder.tvName.text = dish.name
        holder.tvPrice.text = "${dish.price} đ"
        holder.tvDesc.text = dish.description
        
        Glide.with(holder.itemView.context)
            .load(dish.imageUrl)
            .centerCrop()
            .placeholder(android.R.color.darker_gray)
            .error(android.R.color.darker_gray)
            .into(holder.ivImage)
        
        val context = holder.itemView.context
        val prefsManager = SharedPrefsManager(context)
        
        // Setup Favorite button
        val isFav = prefsManager.isFavorite(dish.id)
        if (isFav) {
            holder.btnFavorite.setImageResource(android.R.drawable.star_on)
            holder.btnFavorite.setColorFilter(android.graphics.Color.parseColor("#FFC107"))
        } else {
            holder.btnFavorite.setImageResource(android.R.drawable.star_off)
            holder.btnFavorite.setColorFilter(android.graphics.Color.parseColor("#E0E0E0"))
        }
        
        holder.btnFavorite.setOnClickListener {
            val updatedFav = prefsManager.toggleFavorite(dish.id)
            if (updatedFav) {
                holder.btnFavorite.setImageResource(android.R.drawable.star_on)
                holder.btnFavorite.setColorFilter(android.graphics.Color.parseColor("#FFC107"))
            } else {
                holder.btnFavorite.setImageResource(android.R.drawable.star_off)
                holder.btnFavorite.setColorFilter(android.graphics.Color.parseColor("#E0E0E0"))
            }
            val msg = if (updatedFav) "Đã thêm vào yêu thích" else "Đã bỏ yêu thích"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
        
        holder.btnAddToCart.setOnClickListener {
            prefsManager.addToCart(dish.id, 1)
            Toast.makeText(context, "Đã thêm 1 ${dish.name} vào giỏ", Toast.LENGTH_SHORT).show()
            (context as? MainActivity)?.updateCartBadge()
        }

        holder.itemView.setOnClickListener { onItemClick(dish) }
    }

    override fun getItemCount(): Int = dishList.size
}