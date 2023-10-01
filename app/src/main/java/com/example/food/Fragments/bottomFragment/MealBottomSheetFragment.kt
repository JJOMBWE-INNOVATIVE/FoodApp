package com.example.food.Fragments.bottomFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.food.Fragments.homeFragment
import com.example.food.ViewModel.HomeViewModel
import com.example.food.activities.MainActivity
import com.example.food.activities.MealActivity
import com.example.food.databinding.FragmentMealBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


private const val MEAL_ID = "param1"

class MealBottomSheetFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentMealBottomSheetBinding
    private var mealId: String? = null
    private lateinit var viewModel : HomeViewModel
    private   var mealName : String? = null
    private  var mealThumb : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }

        viewModel = (activity as MainActivity).ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let { viewModel.getMealByID(it)
        }

        observeBottomSheetMeal()
        onbottomSheetDialogueClick()

    }

    private fun onbottomSheetDialogueClick() {
        binding.bottomSheet.setOnClickListener {
        if(mealName != null&&mealThumb != null){
            val intent = Intent(activity,MealActivity::class.java)
            intent.apply {
                putExtra(homeFragment.MEAL_ID,mealId)
                putExtra(homeFragment.MEAL_NAME,mealName)
                putExtra(homeFragment.MEAL_THUMB,mealThumb)
            }
            startActivity(intent)


        }
        }
    }

    private fun observeBottomSheetMeal() {
        viewModel.observeBottomSheetMeal().observe(viewLifecycleOwner, Observer {
            meal ->
            Glide.with(this).load(meal.strMealThumb).into(binding.imgBottomSheet)
            binding.tvBottomSheetArea.text = meal.strArea
            binding.tvBottomSheetCategory.text = meal.strCategory
            binding.bottomSheetMealName.text = meal.strMeal

            mealName = meal.strMeal
            mealThumb = meal.strMealThumb
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)

                }
            }




    }
}