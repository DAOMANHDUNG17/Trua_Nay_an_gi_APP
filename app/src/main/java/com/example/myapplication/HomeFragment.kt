package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {
    private lateinit var rvDishes: RecyclerView
    private lateinit var dishAdapter: DishAdapter
    private lateinit var llCategoryContainer: LinearLayout
    private lateinit var tvSortPrice: TextView
    private var currentDishes = DataProvider.allDishes.toList()
    private var isSortAscending = true
    private var currentCategory = "Tất cả"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        rvDishes = view.findViewById(R.id.rvDishes)
        llCategoryContainer = view.findViewById(R.id.llCategoryContainer)
        tvSortPrice = view.findViewById(R.id.tvSortPrice)

        setupRecyclerView()
        setupCategories(inflater)
        
        tvSortPrice.setOnClickListener {
            isSortAscending = !isSortAscending
            tvSortPrice.text = if (isSortAscending) "Giá: Tăng dần ↑" else "Giá: Giảm dần ↓"
            applyFilters()
        }

        return view
    }

    private fun setupRecyclerView() {
        dishAdapter = DishAdapter(currentDishes) { selectedDish ->
            val fragment = DishDetailFragment.newInstance(selectedDish)
            (activity as? MainActivity)?.loadFragment(fragment, addToBackStack = true)
        }
        rvDishes.layoutManager = LinearLayoutManager(context)
        rvDishes.adapter = dishAdapter
        applyFilters()
    }

    private fun setupCategories(inflater: LayoutInflater) {
        llCategoryContainer.removeAllViews()
        for (category in DataProvider.categories) {
            val tvCategory = inflater.inflate(R.layout.item_category, llCategoryContainer, false) as TextView
            tvCategory.text = category
            
            updateCategoryUI(tvCategory, category == currentCategory)

            tvCategory.setOnClickListener {
                currentCategory = category
                // Update UI for all
                for (i in 0 until llCategoryContainer.childCount) {
                    val child = llCategoryContainer.getChildAt(i) as TextView
                    updateCategoryUI(child, child.text.toString() == currentCategory)
                }
                applyFilters()
            }
            llCategoryContainer.addView(tvCategory)
        }
    }

    private fun updateCategoryUI(tv: TextView, isSelected: Boolean) {
        if (isSelected) {
            tv.setBackgroundResource(R.drawable.bg_category_selected)
            tv.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        } else {
            tv.setBackgroundResource(R.drawable.bg_category_unselected)
            tv.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
        }
    }

    private fun applyFilters() {
        var filtered = if (currentCategory == "Tất cả") {
            DataProvider.allDishes
        } else {
            DataProvider.allDishes.filter { it.category == currentCategory }
        }

        filtered = if (isSortAscending) {
            filtered.sortedBy { it.price }
        } else {
            filtered.sortedByDescending { it.price }
        }

        currentDishes = filtered
        dishAdapter.filterList(currentDishes)
    }
}