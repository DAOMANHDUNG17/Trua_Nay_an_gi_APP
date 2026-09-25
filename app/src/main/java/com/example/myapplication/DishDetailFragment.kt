package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class DishDetailFragment : Fragment() {
    private var dish: Dish? = null
    private var quantity = 1
    private var basePrice = 0
    private lateinit var prefsManager: SharedPrefsManager

    companion object {
        fun newInstance(dish: Dish): DishDetailFragment {
            val fragment = DishDetailFragment()
            val args = Bundle()
            args.putSerializable("DISH_DATA", dish)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dish = arguments?.getSerializable("DISH_DATA") as? Dish
        prefsManager = SharedPrefsManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_dish_detail, container, false)

        dish?.let { currentDish ->
            basePrice = currentDish.price

            val tvDetailName = view.findViewById<TextView>(R.id.tvDetailName)
            val tvDetailPrice = view.findViewById<TextView>(R.id.tvDetailPrice)
            val tvDetailDesc = view.findViewById<TextView>(R.id.tvDetailDesc)
            val tvDetailIngredients = view.findViewById<TextView>(R.id.tvDetailIngredients)
            val tvQuantity = view.findViewById<TextView>(R.id.tvQuantity)
            val btnMinus = view.findViewById<ImageButton>(R.id.btnMinus)
            val btnPlus = view.findViewById<ImageButton>(R.id.btnPlus)
            val btnOrder = view.findViewById<Button>(R.id.btnOrder)
            val ivDetailImage = view.findViewById<ImageView>(R.id.ivDetailImage)

            tvDetailName.text = currentDish.name
            tvDetailPrice.text = "${currentDish.price} đ"
            tvDetailIngredients.text = currentDish.ingredients
            tvDetailDesc.text = currentDish.description
            
            Glide.with(this)
                .load(currentDish.imageUrl)
                .centerCrop()
                .placeholder(android.R.color.darker_gray)
                .into(ivDetailImage)
                
            updateTotalPrice(btnOrder)

            btnMinus.setOnClickListener {
                if (quantity > 1) {
                    quantity--
                    tvQuantity.text = quantity.toString()
                    updateTotalPrice(btnOrder)
                }
            }

            btnPlus.setOnClickListener {
                quantity++
                tvQuantity.text = quantity.toString()
                updateTotalPrice(btnOrder)
            }

            btnOrder.setOnClickListener {
                prefsManager.addToCart(currentDish.id, quantity)
                Toast.makeText(context, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show()
                (activity as? MainActivity)?.updateCartBadge()
                parentFragmentManager.popBackStack()
            }
        }

        return view
    }

    private fun updateTotalPrice(btnOrder: Button) {
        val total = basePrice * quantity
        btnOrder.text = "Thêm - $total đ"
    }
}