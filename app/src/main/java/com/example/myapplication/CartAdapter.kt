package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class CartAdapter(
    private var cartList: List<CartItem>,
    private val onQuantityChanged: (CartItem, Int) -> Unit,
    private val onDelete: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    fun updateData(newList: List<CartItem>) {
        cartList = newList
        notifyDataSetChanged()
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvDishName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvDishPrice)
        val ivImage: ImageView = itemView.findViewById(R.id.ivDishImage)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val btnMinus: ImageButton = itemView.findViewById(R.id.btnMinus)
        val btnPlus: ImageButton = itemView.findViewById(R.id.btnPlus)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartList[position]
        holder.tvName.text = item.dish.name
        holder.tvPrice.text = "${item.dish.price} đ"
        holder.tvQuantity.text = item.quantity.toString()
        
        Glide.with(holder.itemView.context)
            .load(item.dish.imageUrl)
            .transform(CenterCrop(), RoundedCorners(16))
            .placeholder(android.R.color.darker_gray)
            .into(holder.ivImage)

        holder.btnMinus.setOnClickListener {
            onQuantityChanged(item, item.quantity - 1)
        }
        
        holder.btnPlus.setOnClickListener {
            onQuantityChanged(item, item.quantity + 1)
        }
        
        holder.btnDelete.setOnClickListener {
            onDelete(item)
        }
    }

    override fun getItemCount(): Int = cartList.size
}