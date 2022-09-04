package com.recipe.app.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.recipe.app.R
import com.recipe.app.db.DbController
import com.recipe.app.db.SharedPref
import com.recipe.app.models.User
import com.squareup.picasso.Picasso

class RecipeAdapter(val context: Context,val list: ArrayList<com.recipe.app.models.Recipe>,val dbController: DbController,val callBack: IRecipe) :  RecyclerView.Adapter<RecipeAdapter.Recipe>(){
   var fileteredList=ArrayList<com.recipe.app.models.Recipe>()
    init {
        fileteredList=list
    }



   public  interface  IRecipe{
       fun onRecipeClicked(pos:Int)
       fun onFavouriteClicked(pos:Int,isFavourite:Boolean)
       fun onMenuClicked(pos:Int)
   }



    class  Recipe(item:View) : RecyclerView.ViewHolder(item){
          lateinit var  name:TextView
          lateinit var  adddedBy:TextView
          lateinit var  tv_rating:TextView
          lateinit var  image:ImageView
          lateinit var  favIcon:ImageView
          lateinit var  menu:ImageView
          lateinit var  viewGroup:View
          init {
              menu=item.findViewById(R.id.ic_menu)
              name=item.findViewById(R.id.tv_recipe_name)
              adddedBy=item.findViewById(R.id.tv_addedBy)
              tv_rating=item.findViewById(R.id.tv_rating)
              image=item.findViewById(R.id.image)
              favIcon=item.findViewById(R.id.iv_favorite)
              viewGroup=item.findViewById(R.id.recipeViewGroup)

          }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Recipe {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return Recipe(view)
    }

    override fun onBindViewHolder(holder: Recipe, position: Int) {
        val favouriteRecipies=SharedPref.getFavourites(context)
        var isFavourite=false

         holder.menu.visibility=View.GONE

          val model=fileteredList.get(position)

//        if(model.recipeAddedBy.equals(SharedPref.getUserKey(context)))
//        {
//            holder.menu.visibility=View.VISIBLE;
//        }

        if(favouriteRecipies.contains(model.recipeKey))
        {
              holder.favIcon.setImageDrawable(context.getDrawable(R.drawable.icon_favourite_filled))
            isFavourite=true
        }else{
            holder.favIcon.setImageDrawable(context.getDrawable(R.drawable.ic_favorite_border))
            isFavourite=false
        }

        holder.favIcon.setOnClickListener {

            callBack.onFavouriteClicked(position,isFavourite)
        }
        holder.tv_rating.text=""+model.rating
        holder.adddedBy.text=""+model.recipeAddedByName!!

        holder.name.text=model.recipeName
        holder.viewGroup.setOnClickListener {
            callBack.onRecipeClicked(position)
        }
        val imageRef=  this.dbController.getUsersReference().child(model.recipeAddedBy)
        imageRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user=snapshot.getValue(User::class.java)

                if(user?.imageUrl?.length!!>5)
                {
                    Picasso.get()
                        .load(user.imageUrl!!)
                        .placeholder(R.drawable.icon_person)
                        .error(R.drawable.icon_person)
                        .into(holder.image);
                }



               imageRef.removeEventListener(this)
            }

            override fun onCancelled(error: DatabaseError) {
                imageRef.removeEventListener(this)
            }

        })









    }

    fun search(s:String)
    {
        if(s.length>0) {
            val tempList=ArrayList<com.recipe.app.models.Recipe>()

            for (i in list) {
              if(i.recipeName.contains(s,true) ||
                  i.recipeIngredients.contains(s,true) ||
                  i.recipeDescription.contains(s,true)){
                  tempList.add(i)
              }
            }

            fileteredList=tempList;
            notifyDataSetChanged()
        }else{
            fileteredList=list
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
     return  fileteredList.size
    }
}