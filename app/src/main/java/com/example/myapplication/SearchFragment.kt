package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchFragment : Fragment() {
    private lateinit var searchAdapter: DishAdapter
    private val allDishes = DataProvider.allDishes

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_search, container, false)

        val edtSearch = view.findViewById<EditText>(R.id.edtSearch)
        val recyclerSearch = view.findViewById<RecyclerView>(R.id.recyclerSearch)
        val tvNoResult = view.findViewById<TextView>(R.id.tvNoResult)

        searchAdapter = DishAdapter(allDishes) { selectedDish ->
            val fragment = DishDetailFragment.newInstance(selectedDish)
            (activity as? MainActivity)?.loadFragment(fragment, addToBackStack = true)
        }

        recyclerSearch.layoutManager = LinearLayoutManager(context)
        recyclerSearch.adapter = searchAdapter

        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val keyword = s.toString().lowercase()
                val filteredList = allDishes.filter { it.name.lowercase().contains(keyword) }
                searchAdapter.filterList(filteredList)
                
                if (filteredList.isEmpty()) {
                    tvNoResult.visibility = View.VISIBLE
                    recyclerSearch.visibility = View.GONE
                } else {
                    tvNoResult.visibility = View.GONE
                    recyclerSearch.visibility = View.VISIBLE
                }
            }
        })

        return view
    }
}