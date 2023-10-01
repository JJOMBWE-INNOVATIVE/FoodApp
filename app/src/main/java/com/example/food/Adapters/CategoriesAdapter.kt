package com.example.food.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food.databinding.CategoryItemBinding
import com.example.food.pojo.Category

class CategoriesAdapter(): RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    private var CategoriesList = ArrayList<Category>()
        var onItemClick : ((Category) -> Unit)? =  null

    fun  setCategoryList(CategoriesList: List<Category>){
        this.CategoriesList = CategoriesList as ArrayList<Category>
        notifyDataSetChanged()
    }

    class CategoriesViewHolder( val binding: CategoryItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return CategoriesList.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        Glide.with(holder.itemView).load(CategoriesList[position].strCategoryThumb)
            .into(holder.binding.imgCategory)
        holder.binding.tvCategoryName.text = CategoriesList[position].strCategory

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(CategoriesList[position])
        }
    }}

