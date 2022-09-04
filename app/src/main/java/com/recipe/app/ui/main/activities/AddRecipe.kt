package com.recipe.app.ui.main.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.recipe.app.databinding.ActivityAddRecipeBinding
import com.recipe.app.db.DbController
import com.recipe.app.db.DbController.ITask
import com.recipe.app.db.SharedPref
import com.recipe.app.models.Rating
import com.recipe.app.models.Recipe
import com.recipe.app.utils.ViewUtils


class AddRecipe : AppCompatActivity() {
     lateinit var dbController:DbController
     lateinit var binding:ActivityAddRecipeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbController= DbController(this)

        binding.btnAddRecipe.setOnClickListener {
            addRecipe()
        }
        binding.close.setOnClickListener { finish() }


    }

    fun addRecipe()
    {
        if(validate())
        {
            val recipe=Recipe(binding.etRecipeName.text.toString(),binding.etIngridients.text.toString(),"",binding.etRecipeResc.text.toString(),SharedPref.getUserKey(this)!!,SharedPref.getUserName(this)!!)
            ViewUtils.showProgressDialog(this,"Adding Recipe")

            dbController.addRecipe(object :ITask{
                override fun onComplete() {


                    finish()
                }

                override fun onComplete(list: ArrayList<Recipe>) {
                    TODO("Not yet implemented")
                }

                override fun onComplete(msg: String) {

                    showToast("Recipe Added",false)
                    ViewUtils.hideProgressDialog()
                    finish()
                }

                override fun onError(msg: String) {showToast("Failed to Add Recipe",true)
                }

            },recipe)
        }

    }

    fun updateRecipe()
    {
        if(validate())
        {
            val recipe=Recipe(binding.etRecipeName.text.toString(),binding.etIngridients.text.toString(),"",binding.etRecipeResc.text.toString(),SharedPref.getUserKey(this)!!,SharedPref.getUserName(this)!!)
            ViewUtils.showProgressDialog(this,"Adding Recipe")

            dbController.updateRecipe(object :ITask{
                override fun onComplete() {


                    finish()
                }

                override fun onComplete(list: ArrayList<Recipe>) {
                    TODO("Not yet implemented")
                }

                override fun onComplete(msg: String) {

                    showToast("Recipe Added",false)
                    ViewUtils.hideProgressDialog()
                    finish()
                }

                override fun onError(msg: String) {showToast("Failed to Add Recipe",true)
                }

            },recipe.recipeKey,recipe)
        }

    }

   fun validate():Boolean
   {
       if(binding.etRecipeName.text.isEmpty())
       {
           showToast("Please Enter Recipe Name ",true)
           return  false
       }

       if(binding.etIngridients.text.isEmpty())
       {

           showToast("Please Enter Recipe Ingridients",true)
           return  false
       }


       if(binding.etRecipeResc.text.isEmpty())
       {

           showToast("Please Enter Recipe Description",true)
           return  false
       }

       return  true;

   }

fun showToast(msg:String,isError:Boolean)   {
    ViewUtils.showToast(this,msg,isError);
}





}