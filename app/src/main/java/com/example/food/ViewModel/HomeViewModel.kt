package com.example.food.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food.db.MealDataBase
import com.example.food.pojo.Category
import com.example.food.pojo.CategoryList
import com.example.food.pojo.MealsByCategoryList
import com.example.food.pojo.MealsByCategory
import com.example.food.pojo.Meal
import com.example.food.pojo.MealList
import com.example.food.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query

class HomeViewModel(
    private var mealDataBase: MealDataBase
): ViewModel(
) {
    private  var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesListLiveData = MutableLiveData<List<Category>>()
    private var favorateMealsLiveData = mealDataBase.mealDao().getAllMeals()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()
    private var searchMealsLiveData = MutableLiveData<List<Meal>>()

    init {
        getRandomMeal()
    }

    fun getRandomMeal(){

        RetrofitInstance.api.getRandomMeal().enqueue(object: Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body()!=null){
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Home Fragment",t.message.toString())
            }

        })

    }

    fun searchMeals(searchQuery: String) =
        RetrofitInstance.api.searchMeals(searchQuery).enqueue(object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealsList = response.body()?.meals
                mealsList?.let {
                    searchMealsLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Home Fragment",t.message.toString())
            }

        })

    fun observeSearchMealsLiveData() : LiveData<List<Meal>> {
        return searchMealsLiveData
    }

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object: Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let {
                    categoryList -> categoriesListLiveData.postValue(categoryList.categories)
                }

            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.e("Home Fragment",t.message.toString())
            }

        })
    }

    fun getMealByID(id:String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let {meal ->
                    bottomSheetMealLiveData.postValue(meal)
                }

            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel ",t.message.toString() )
            }

        })
    }

    fun observeBottomSheetMeal(): LiveData<Meal>{
        return bottomSheetMealLiveData
    }

    fun observeRandomLiveData():LiveData<Meal>{
        return randomMealLiveData
    }

    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object: Callback<MealsByCategoryList> {
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if (response.body()!=null){
                    popularItemsLiveData.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("homeFragment",t.message.toString())
            }

        })
    }

    fun deleteMeal(meal:Meal){
        viewModelScope.launch {
            mealDataBase.mealDao().deleteMeal(meal)
        }
    }


    fun insertMeal(meal:Meal){
        viewModelScope.launch {
            mealDataBase.mealDao().upsert(meal)
        }
    }


    fun observePopularItemsLiveData():LiveData<List<MealsByCategory>>{
        return popularItemsLiveData
    }

    fun observeCategoriesLiveData():LiveData<List<Category>>{
        return categoriesListLiveData
    }

    fun observeFavorateMealsLiveData():LiveData<List<Meal>>{
        return favorateMealsLiveData

    }




}