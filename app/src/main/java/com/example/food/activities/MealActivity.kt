package com.example.food.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.food.Fragments.homeFragment
import com.example.food.R
import com.example.food.ViewModel.MealViewModel
import com.example.food.ViewModel.MealViewModelFactory
import com.example.food.databinding.ActivityMealBinding
import com.example.food.db.MealDataBase
import com.example.food.pojo.Meal

class MealActivity : AppCompatActivity() {

    private lateinit  var mealId : String
    private lateinit  var mealName : String
    private lateinit  var mealThumb : String
    private lateinit var mealMvvm:MealViewModel
    private lateinit  var  binding: ActivityMealBinding
    private lateinit var youTubeLink: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getMealInformationFromIntent()

        val mealDataBase = MealDataBase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDataBase)

        mealMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        loadingCase()

        mealMvvm.getMealDetail(mealId)

        observeMealDetailsLiveData()

        setInformationInViews()

        onFavoriteClick()

        onYouTubeImageClick()


    }

    private fun onFavoriteClick() {
        binding.btnAddToFloat.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, "Meal Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYouTubeImageClick() {
        binding.imgYoutube.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youTubeLink))
            startActivity(intent)

        }
    }

    private var mealToSave : Meal?= null
    private fun observeMealDetailsLiveData() {
        mealMvvm.observeMealDetailsLiveData().observe(this,object:Observer<Meal>{
            override fun onChanged(value: Meal) {
                onResponseCase()
                val meal = value

                mealToSave = meal

                binding.tvCategoryInfo.text = "Category:${meal!!.strCategory}"
                binding.tvAreaInfo.text = "Area:${meal!!.strArea}"
                binding.tvInstructions.text = meal.strInstructions

                youTubeLink = meal.strYoutube.toString()

            }

        })

    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar
            .setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar
            .setExpandedTitleColor(resources.getColor(R.color.white))

    }

    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(homeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(homeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(homeFragment.MEAL_THUMB)!!

    }

    private fun loadingCase(){
        binding.btnAddToFloat.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvCategoryInfo.visibility = View.INVISIBLE
        binding.tvAreaInfo.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun onResponseCase(){
        binding.btnAddToFloat.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvCategoryInfo.visibility = View.VISIBLE
        binding.tvAreaInfo.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
        binding.progressBar.visibility = View.INVISIBLE
    }



}

