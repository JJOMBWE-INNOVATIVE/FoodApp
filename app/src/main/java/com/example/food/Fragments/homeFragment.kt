package com.example.food.Fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.food.Adapters.CategoriesAdapter
import com.example.food.Adapters.MostPopularAdapter
import com.example.food.Fragments.bottomFragment.MealBottomSheetFragment
import com.example.food.R
import com.example.food.ViewModel.HomeViewModel
import com.example.food.activities.CategoryMealsActivity
import com.example.food.activities.MainActivity
import com.example.food.activities.MealActivity
import com.example.food.databinding.FragmentHomeBinding
import com.example.food.pojo.MealsByCategory
import com.example.food.pojo.Meal


class homeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewModel : HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object{
        const val MEAL_ID = "com.example.food.Fragments.idMeal"
        const val MEAL_NAME = "com.example.food.Fragments.nameMeal"
        const val MEAL_THUMB = "com.example.food.Fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.food.Fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    viewModel = (activity as MainActivity).ViewModel
        popularItemsAdapter = MostPopularAdapter()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    preparePopularRecyclerView()
        
    observeRandomMeal()
    onRandomMealClick()

    viewModel.getPopularItems()
        observePopularItemsLiveData()

    onPopularItemClick()

    prepareCategoriesRecyclerview()
    viewModel.getCategories()
    observeCategoriesLiveData()

    onCategoryClick()

    onPopularItemLongItemClick()

    onSearchIconClick()

    }

    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularItemLongItemClick() {
        popularItemsAdapter.onLongItemClick = { meal ->
            val mealBottomSheetFragment = MealBottomSheetFragment
                .newInstance(meal.idMeal)

            mealBottomSheetFragment.show(childFragmentManager,"Meal Information")



        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = {
            category ->
            val intent = Intent(activity,CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerview() {
        categoriesAdapter = CategoriesAdapter()
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer {



             categories ->  categoriesAdapter.setCategoryList(categories)

        } )
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = {
            meal -> val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            adapter = popularItemsAdapter
        }
    }

    private fun observePopularItemsLiveData() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner
        ) { mealList ->
            popularItemsAdapter.setMeals(mealList = mealList as ArrayList<MealsByCategory>)
        }
    }


    private fun observeRandomMeal() {
        viewModel.observeRandomLiveData().observe(viewLifecycleOwner
        ) { meal ->
            Glide.with(this@homeFragment)
                .load(meal!!.strMealThumb)
                .into(binding.imgRandomMeal)

            this.randomMeal = meal
        }
    }



    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME,randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)

            startActivity(intent)
        }
    }


}



