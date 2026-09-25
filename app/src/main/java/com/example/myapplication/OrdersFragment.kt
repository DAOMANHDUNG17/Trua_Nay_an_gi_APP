package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class OrdersFragment : Fragment() {
    private lateinit var prefsManager: SharedPrefsManager
    private lateinit var rvOrders: RecyclerView
    private lateinit var tvEmptyOrders: TextView
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_orders, container, false)
        prefsManager = SharedPrefsManager(requireContext())

        rvOrders = view.findViewById(R.id.rvOrders)
        tvEmptyOrders = view.findViewById(R.id.tvEmptyOrders)

        val orders = prefsManager.getOrders()

        orderAdapter = OrderAdapter(orders)
        rvOrders.layoutManager = LinearLayoutManager(context)
        rvOrders.adapter = orderAdapter

        if (orders.isEmpty()) {
            tvEmptyOrders.visibility = View.VISIBLE
            rvOrders.visibility = View.GONE
        } else {
            tvEmptyOrders.visibility = View.GONE
            rvOrders.visibility = View.VISIBLE
        }

        return view
    }
}