package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.Normalizer
import java.util.regex.Pattern

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

        // Lọc ngay khi người dùng gõ phím
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString(), tvNoResult, recyclerSearch)
            }
        })

        // Bấm nút Tìm kiếm trên bàn phím (Search / Done)
        edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                filter(edtSearch.text.toString(), tvNoResult, recyclerSearch)
                true
            } else {
                false
            }
        }

        return view
    }

    private fun filter(query: String, tvNoResult: TextView, recyclerSearch: RecyclerView) {
        val normalizedQuery = unaccent(query.lowercase().trim())

        val filteredList = if (normalizedQuery.isEmpty()) {
            allDishes
        } else {
            allDishes.filter { dish ->
                val nameNormalized = unaccent(dish.name.lowercase())
                val categoryNormalized = unaccent(dish.category.lowercase())
                nameNormalized.contains(normalizedQuery) || categoryNormalized.contains(normalizedQuery)
            }
        }

        searchAdapter.filterList(filteredList)

        if (filteredList.isEmpty()) {
            tvNoResult.visibility = View.VISIBLE
            recyclerSearch.visibility = View.GONE
        } else {
            tvNoResult.visibility = View.GONE
            recyclerSearch.visibility = View.VISIBLE
        }
    }

    // Hàm bỏ dấu tiếng Việt để tìm kiếm không dấu (vd: "pho" vẫn tìm ra "Phở")
    private fun unaccent(text: String): String {
        val temp = Normalizer.normalize(text, Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(temp)
            .replaceAll("")
            .replace('đ', 'd')
            .replace('Đ', 'D')
    }
}