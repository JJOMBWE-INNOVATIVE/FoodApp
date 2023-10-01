package com.example.food.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.food.Adapters.MealsAdapter
import com.example.food.ViewModel.HomeViewModel
import com.example.food.activities.MainActivity
import com.example.food.databinding.FragmentFavoritesBinding
import com.google.android.material.snackbar.Snackbar


class favoritesFragment : Fragment() {
    private  lateinit var viewModel: HomeViewModel
    private  lateinit var favoriteAdapter: MealsAdapter
    private  lateinit var binding: FragmentFavoritesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).ViewModel


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareFavoritesRecyclerView()
        observeFavoraites()

        val itemsTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                // Check if position is within bounds
                if (position != RecyclerView.NO_POSITION) {
                    val deletedMeal = favoriteAdapter.differ.currentList.getOrNull(position)

                    // Check if the deletedMeal is not null
                    if (deletedMeal != null) {
                        viewModel.deleteMeal(deletedMeal)

                        Snackbar.make(requireView(), "Meal Deleted", Snackbar.LENGTH_LONG)
                            .setAction(
                                "Undo",
                                View.OnClickListener {
                                    viewModel.insertMeal(deletedMeal)
                                }
                            ).show()
                    }
                }

            }
        }
            ItemTouchHelper(itemsTouchHelper).attachToRecyclerView(binding.favRecView)

    }

    private fun prepareFavoritesRecyclerView() {
        favoriteAdapter = MealsAdapter()
        binding.favRecView.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
           adapter = favoriteAdapter
        }

    }

    private fun observeFavoraites() {
        viewModel.observeFavorateMealsLiveData().observe(requireActivity(), Observer { meals ->
            meals.forEach {
                favoriteAdapter.differ.submitList(meals)
            }
        })
    }





}



