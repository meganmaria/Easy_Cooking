package com.recipe.app.ui.main.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.recipe.app.R
import com.recipe.app.databinding.ActivityViewRecipeBinding
import com.recipe.app.db.DbController
import com.recipe.app.db.SharedPref
import com.recipe.app.models.Rating
import com.recipe.app.models.Recipe
import com.recipe.app.ui.main.fragments.BottomSheet
import com.recipe.app.utils.ViewUtils

class ViewRecipe : AppCompatActivity() {
    lateinit var  binding: ActivityViewRecipeBinding
   var  currentRecipe: Recipe?=null
    lateinit var  dbController: DbController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityViewRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentRecipe=MainActivity.getCurrentRecipe();
  if(currentRecipe==null)
  {
      finish()

  }
        dbController= DbController(this)
        binding.ingridients.setText(currentRecipe!!.recipeIngredients!!)
        binding.description.setText(currentRecipe!!.recipeDescription!!)
       binding.toolBar.toolBarTitle.setText(currentRecipe!!.recipeName)

        binding.toolBar.addRecipe.visibility=View.GONE
        binding.toolBar.filter.setImageDrawable(getDrawable(R.drawable.icon_more))
        binding.toolBar.filter.visibility=View.GONE
        val key=SharedPref.getUserKey(this)

      if(key.equals(currentRecipe!!.recipeAddedBy))
      {
          binding.toolBar.filter.visibility=View.VISIBLE
      }

        if(currentRecipe!!.ratings!=null)
        {
            for(r in currentRecipe!!.ratings)
            {
                if(r.userKey.equals(key!!))
                {
                    binding.rate.setText("Rated ${r.stars} Stars")
                    binding.rate.isEnabled=false
                    break
                }
            }
        }






        binding.toolBar.filter.setOnClickListener {
            BottomSheet.showRecipeModifyBottomSheet(ViewRecipe@this,object :BottomSheet.IBottomSheet{
                override fun onOptionClicked(option: Int) {

                  if(option==1)
                  {
                      binding.ingridients.isEnabled=true
                      binding.description.isEnabled=true
                      binding.save.visibility=View.VISIBLE

                  }

                  if(option==2)
                  {
                    dbController.removeRecipe(object :DbController.ITask{
                        override fun onComplete() {
                            showToast("Recipe Removed")
                            Handler().postDelayed(Runnable {
                                finish()
                            },200)
                        }

                        override fun onComplete(list: ArrayList<Recipe>) {

                        }

                        override fun onComplete(msg: String) {

                        }

                        override fun onError(msg: String) {

                        }

                    },currentRecipe!!.recipeKey)
                  }



                }

            })



        }

        binding.rate.setOnClickListener {
            BottomSheet.showRatingBar(ViewRecipe@this,object :BottomSheet.IBottomSheetRating {
                override fun onOptionClicked(option: Float) {
                    val  rating=Rating(option,key!!)

                    dbController.rateRecipe(currentRecipe!!.recipeKey,key!!,rating,object :DbController.ITask{
                        override fun onComplete() {
                            showToast("Rated Successfully")
                            binding.rate.visibility=View.GONE

                        }

                        override fun onComplete(list: ArrayList<Recipe>) {

                        }

                        override fun onComplete(msg: String) {

                        }

                        override fun onError(msg: String) {

                        }

                    })



                }
            })
        }

        binding.share.setOnClickListener {
            var  r = binding.toolBar.toolBarTitle.text.toString() + "\n" + "\n Ingridients: \n" + binding.ingridients.text.toString() + "\n" + "\n Description: \n" + binding.description.text.toString()

            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, r)
            intent.type = "text/plain"

            startActivity(Intent.createChooser(intent, "Share:"))

        }

        binding.save.setOnClickListener {
            updateRecipe()
        }
    }

    fun updateRecipe()
    {
        if(validate())
        {

            ViewUtils.showProgressDialog(this,"Updating Recipe")
             val recipe=currentRecipe;
            recipe?.recipeIngredients=binding.ingridients.text.toString()
            recipe?.recipeDescription=binding.description.text.toString()
            dbController.updateRecipe(object : DbController.ITask {
                override fun onComplete() {


                    finish()
                }

                override fun onComplete(list: ArrayList<Recipe>) {
                    TODO("Not yet implemented")
                }

                override fun onComplete(msg: String) {

                    showToast("Recipe Updated",false)
                    binding.save.visibility=View.GONE
                    ViewUtils.hideProgressDialog()
                    finish()
                }

                override fun onError(msg: String) {showToast("Failed to Add Recipe",true)
                }

            },recipe?.recipeKey!!,recipe!!)
        }

    }

   fun showToast(msg:String){
        ViewUtils.showToast(ViewRecipe@this,msg)
    }

    fun validate():Boolean
    {
        if(binding.toolBar.toolBarTitle.text.isEmpty())
        {
            showToast("Please Enter Recipe Name ",true)
            return  false
        }

        if(binding.ingridients.text.isEmpty())
        {

            showToast("Please Enter Recipe Ingridients",true)
            return  false
        }


        if(binding.description.text.isEmpty())
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