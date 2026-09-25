package com.example.myapplication

import java.io.Serializable

data class Dish(
    val id: String,
    val name: String,
    val price: Int,
    val description: String,
    val ingredients: String = "Thành phần cơ bản",
    val imageUrl: String = "",
    val category: String = "Khác"
) : Serializable