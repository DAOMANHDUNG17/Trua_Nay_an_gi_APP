package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartFragment : Fragment() {
    private lateinit var prefsManager: SharedPrefsManager
    private lateinit var cartAdapter: CartAdapter
    private lateinit var rvCart: RecyclerView
    private lateinit var tvEmptyCart: TextView
    private lateinit var tvCartTotal: TextView
    private lateinit var btnCheckout: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        prefsManager = SharedPrefsManager(requireContext())

        rvCart = view.findViewById(R.id.rvCart)
        tvEmptyCart = view.findViewById(R.id.tvEmptyCart)
        tvCartTotal = view.findViewById(R.id.tvCartTotal)
        btnCheckout = view.findViewById(R.id.btnCheckout)

        setupRecyclerView()
        
        btnCheckout.setOnClickListener {
            val items = prefsManager.getCartItems()
            if (items.isNotEmpty()) {
                var total = 0
                for (item in items) {
                    total += item.dish.price * item.quantity
                }
                
                val order = Order(
                    id = "ORD-${System.currentTimeMillis()}",
                    timestamp = System.currentTimeMillis(),
                    items = items,
                    totalPrice = total
                )
                
                prefsManager.addOrder(order)
                prefsManager.clearCart()
                updateUI()
                Toast.makeText(context, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show()
                (activity as? MainActivity)?.switchToOrders()
            } else {
                Toast.makeText(context, "Giỏ hàng đang trống", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            cartList = prefsManager.getCartItems(),
            onQuantityChanged = { item, newQuantity ->
                if (newQuantity > 0) {
                    prefsManager.updateCartQuantity(item.dish.id, newQuantity)
                } else {
                    prefsManager.removeFromCart(item.dish.id)
                }
                updateUI()
            },
            onDelete = { item ->
                prefsManager.removeFromCart(item.dish.id)
                updateUI()
            }
        )
        rvCart.layoutManager = LinearLayoutManager(context)
        rvCart.adapter = cartAdapter
        updateUI()
    }

    private fun updateUI() {
        val items = prefsManager.getCartItems()
        cartAdapter.updateData(items)

        if (items.isEmpty()) {
            tvEmptyCart.visibility = View.VISIBLE
            rvCart.visibility = View.GONE
        } else {
            tvEmptyCart.visibility = View.GONE
            rvCart.visibility = View.VISIBLE
        }

        var total = 0
        for (item in items) {
            total += item.dish.price * item.quantity
        }
        tvCartTotal.text = "$total đ"
        
        // Update Cart Badge in MainActivity if needed
        (activity as? MainActivity)?.updateCartBadge()
    }
}