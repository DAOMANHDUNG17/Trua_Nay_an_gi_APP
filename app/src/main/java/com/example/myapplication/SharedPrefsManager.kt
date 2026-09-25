package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

class SharedPrefsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("FoodAppPrefs", Context.MODE_PRIVATE)

    // Favorites
    fun getFavorites(): List<String> {
        val favString = prefs.getString("favorites", "[]") ?: "[]"
        val jsonArray = JSONArray(favString)
        val list = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getString(i))
        }
        return list
    }

    fun toggleFavorite(dishId: String): Boolean {
        val favs = getFavorites().toMutableList()
        val isFav = if (favs.contains(dishId)) {
            favs.remove(dishId)
            false
        } else {
            favs.add(dishId)
            true
        }
        prefs.edit().putString("favorites", JSONArray(favs).toString()).apply()
        return isFav
    }

    fun isFavorite(dishId: String): Boolean {
        return getFavorites().contains(dishId)
    }

    // Cart
    fun getCartItems(): List<CartItem> {
        val cartString = prefs.getString("cart", "[]") ?: "[]"
        val jsonArray = JSONArray(cartString)
        val list = mutableListOf<CartItem>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val dishId = obj.getString("dishId")
            val quantity = obj.getInt("quantity")
            val dish = DataProvider.getDishById(dishId)
            if (dish != null) {
                list.add(CartItem(dish, quantity))
            }
        }
        return list
    }

    fun addToCart(dishId: String, quantity: Int) {
        val cart = getCartItems().toMutableList()
        val existing = cart.find { it.dish.id == dishId }
        if (existing != null) {
            existing.quantity += quantity
        } else {
            val dish = DataProvider.getDishById(dishId)
            if (dish != null) {
                cart.add(CartItem(dish, quantity))
            }
        }
        saveCart(cart)
    }
    
    fun updateCartQuantity(dishId: String, quantity: Int) {
        val cart = getCartItems().toMutableList()
        val existing = cart.find { it.dish.id == dishId }
        if (existing != null) {
            existing.quantity = quantity
            if (existing.quantity <= 0) {
                cart.remove(existing)
            }
        }
        saveCart(cart)
    }

    fun removeFromCart(dishId: String) {
        val cart = getCartItems().toMutableList()
        cart.removeAll { it.dish.id == dishId }
        saveCart(cart)
    }

    private fun saveCart(cart: List<CartItem>) {
        val jsonArray = JSONArray()
        for (item in cart) {
            val obj = JSONObject()
            obj.put("dishId", item.dish.id)
            obj.put("quantity", item.quantity)
            jsonArray.put(obj)
        }
        prefs.edit().putString("cart", jsonArray.toString()).apply()
    }

    fun getCartCount(): Int {
        var count = 0
        for (item in getCartItems()) {
            count += item.quantity
        }
        return count
    }
    
    fun clearCart() {
        prefs.edit().putString("cart", "[]").apply()
    }

    // Orders
    fun getOrders(): List<Order> {
        val ordersString = prefs.getString("orders", "[]") ?: "[]"
        val jsonArray = JSONArray(ordersString)
        val list = mutableListOf<Order>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val id = obj.getString("id")
            val timestamp = obj.getLong("timestamp")
            val totalPrice = obj.getInt("totalPrice")
            val itemsArray = obj.getJSONArray("items")
            val itemsList = mutableListOf<CartItem>()
            for (j in 0 until itemsArray.length()) {
                val itemObj = itemsArray.getJSONObject(j)
                val dishId = itemObj.getString("dishId")
                val quantity = itemObj.getInt("quantity")
                val dish = DataProvider.getDishById(dishId)
                if (dish != null) {
                    itemsList.add(CartItem(dish, quantity))
                }
            }
            list.add(Order(id, timestamp, itemsList, totalPrice))
        }
        return list
    }

    fun addOrder(order: Order) {
        val orders = getOrders().toMutableList()
        orders.add(0, order) // Mới nhất lên đầu
        val jsonArray = JSONArray()
        for (o in orders) {
            val orderObj = JSONObject()
            orderObj.put("id", o.id)
            orderObj.put("timestamp", o.timestamp)
            orderObj.put("totalPrice", o.totalPrice)
            
            val itemsArray = JSONArray()
            for (item in o.items) {
                val itemObj = JSONObject()
                itemObj.put("dishId", item.dish.id)
                itemObj.put("quantity", item.quantity)
                itemsArray.put(itemObj)
            }
            orderObj.put("items", itemsArray)
            
            jsonArray.put(orderObj)
        }
        prefs.edit().putString("orders", jsonArray.toString()).apply()
    }
}

data class CartItem(val dish: Dish, var quantity: Int)
data class Order(val id: String, val timestamp: Long, val items: List<CartItem>, val totalPrice: Int)
