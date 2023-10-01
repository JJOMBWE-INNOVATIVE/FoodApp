package com.example.food.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.food.db.MealDataBase

class MealViewModelFactory(
   private val mealViewModel: MealDataBase
):ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealViewModel(mealViewModel) as T
    }
}
