package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritesFragment : Fragment() {
    private lateinit var prefsManager: SharedPrefsManager
    private lateinit var rvFavorites: RecyclerView
    private lateinit var tvEmptyFav: TextView
    private lateinit var favAdapter: DishAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        prefsManager = SharedPrefsManager(requireContext())

        rvFavorites = view.findViewById(R.id.rvFavorites)
        tvEmptyFav = view.findViewById(R.id.tvEmptyFav)

        val favIds = prefsManager.getFavorites()
        val favDishes = DataProvider.allDishes.filter { favIds.contains(it.id) }

        favAdapter = DishAdapter(favDishes) { selectedDish ->
            val fragment = DishDetailFragment.newInstance(selectedDish)
            (activity as? MainActivity)?.loadFragment(fragment, addToBackStack = true)
        }

        rvFavorites.layoutManager = LinearLayoutManager(context)
        rvFavorites.adapter = favAdapter

        if (favDishes.isEmpty()) {
            tvEmptyFav.visibility = View.VISIBLE
            rvFavorites.visibility = View.GONE
        } else {
            tvEmptyFav.visibility = View.GONE
            rvFavorites.visibility = View.VISIBLE
        }

        return view
    }
}