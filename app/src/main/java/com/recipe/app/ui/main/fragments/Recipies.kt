package com.recipe.app.ui.main.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.recipe.app.databinding.FragmentRecipiesBinding
import com.recipe.app.db.DbController
import com.recipe.app.db.SharedPref
import com.recipe.app.models.Recipe
import com.recipe.app.ui.main.activities.IFragmentCallbacks
import com.recipe.app.ui.main.activities.MainActivity
import com.recipe.app.ui.main.adapter.RecipeAdapter
import com.recipe.app.ui.main.activities.ViewRecipe


class Recipies : Fragment() {
    lateinit var  binding:FragmentRecipiesBinding
    lateinit var  adapter: RecipeAdapter
    lateinit var  dbController: DbController
    var  list=ArrayList<Recipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding=  FragmentRecipiesBinding.inflate(inflater, container, false)
        val view = binding.root
        return  view;
    }



    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        dbController= DbController(activity as Context)
        dbController.getRecipies(object :DbController.ITask{
            override fun onComplete() {

            }

            override fun onComplete(listn: ArrayList<Recipe>) {
                if(listn.size>0) {
                    list.clear()
                    list.addAll(listn)
                    list.sortBy { it.rating }
                    list.reverse()
                    adapter.notifyDataSetChanged()
                }

            }

            override fun onComplete(msg: String) {

            }

            override fun onError(msg: String) {

            }

        })

        binding.searchView.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                adapter?.search(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE

                return true

            }

        })
        initAdapter()
    }

    override fun onResume() {
        super.onResume()
        adapter?.notifyDataSetChanged()
        MainActivity.setCallBack(object :IFragmentCallbacks{
            override fun onFilterSelected(o: Int) {
                if(o==1)
                {
                    list?.sortBy { it.rating }
                    list.reverse()
                    adapter.notifyDataSetChanged()
                }

                if(o==2)
                {
                    list?.sortBy { it.rating }
                    adapter.notifyDataSetChanged()
                }
            }


        })
    }


    fun initAdapter()
    {



        adapter= RecipeAdapter(activity as Context,list,dbController,object: RecipeAdapter.IRecipe {
            override fun onRecipeClicked(pos: Int) {

                MainActivity.setCurrentRecipe(list.get(pos))
                startActivity(Intent(activity as Context, ViewRecipe::class.java))


            }

            override fun onFavouriteClicked(pos: Int,isFav:Boolean) {
                if(isFav)
                {
                    SharedPref.removeFromFavourites(activity as Context,list.get(pos).recipeKey)
                }else{
                    SharedPref.addToFavourites(activity as Context,list.get(pos).recipeKey)
                }
                adapter.notifyDataSetChanged()

            }

            override fun onMenuClicked(pos: Int) {

            }

        })
        binding.recipeisRv.layoutManager=LinearLayoutManager(activity)
        binding.recipeisRv.adapter=adapter
    }


}