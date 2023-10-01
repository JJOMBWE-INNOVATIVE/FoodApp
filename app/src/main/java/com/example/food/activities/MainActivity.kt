package com.example.food.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.food.R
import com.example.food.ViewModel.HomeViewModel
import com.example.food.ViewModel.HomelViewModelFactory
import com.example.food.databinding.ActivityMainBinding
import com.example.food.db.MealDataBase
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding? =null
    val ViewModel:HomeViewModel by lazy{
        val mealDataBase = MealDataBase.getInstance(this)
        val homelViewModelProviderFactory = HomelViewModelFactory(mealDataBase)
        ViewModelProvider(this,homelViewModelProviderFactory)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      val btm_nav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController  = Navigation.findNavController(this, R.id.frag_host)

    NavigationUI.setupWithNavController(btm_nav,navController)



    }
}



