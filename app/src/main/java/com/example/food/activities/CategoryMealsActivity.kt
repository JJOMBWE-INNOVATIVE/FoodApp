package com.example.food.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.food.Adapters.CategoryMealsAdapter
import com.example.food.Fragments.homeFragment
import com.example.food.R
import com.example.food.ViewModel.CategoriesMealsViewModel
import com.example.food.databinding.ActivityCategoryMealsBinding

class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryMealsBinding
    private  lateinit var categoryMealsViewModel : CategoriesMealsViewModel
    private lateinit var  categoriesMealsAdapter : CategoryMealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareCategoryMealsRecyclerView()

        categoryMealsViewModel = ViewModelProvider(this).get(CategoriesMealsViewModel::class.java)

        categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(homeFragment.CATEGORY_NAME)!!)

        categoryMealsViewModel.observeMealsLiveData().observe(this, Observer { mealsList ->
            binding.tvCategoryCount.text = mealsList.size.toString()
            categoriesMealsAdapter.setMealsList(mealsList)
        })




    }

    private fun prepareCategoryMealsRecyclerView() {
        categoriesMealsAdapter = CategoryMealsAdapter()
        binding.mealRecyclerview.apply{
            layoutManager = GridLayoutManager(context,2,GridLayoutManager
                .VERTICAL,false)
            adapter = categoriesMealsAdapter
        }
    }
}