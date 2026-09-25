package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderAdapter(private val orderList: List<Order>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOrderId: TextView = itemView.findViewById(R.id.tvOrderId)
        val tvOrderDate: TextView = itemView.findViewById(R.id.tvOrderDate)
        val tvOrderItems: TextView = itemView.findViewById(R.id.tvOrderItems)
        val tvOrderTotal: TextView = itemView.findViewById(R.id.tvOrderTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.tvOrderId.text = order.id
        
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        holder.tvOrderDate.text = sdf.format(Date(order.timestamp))
        
        val itemsSummary = order.items.joinToString(separator = "\n") { "${it.quantity}x ${it.dish.name}" }
        holder.tvOrderItems.text = itemsSummary
        
        holder.tvOrderTotal.text = "${order.totalPrice} đ"
    }

    override fun getItemCount(): Int = orderList.size
}