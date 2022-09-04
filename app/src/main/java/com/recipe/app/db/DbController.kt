package com.recipe.app.db

import android.R.attr
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.recipe.app.models.Rating
import com.recipe.app.models.Recipe
import com.recipe.app.models.User


class DbController {

    val db:FirebaseDatabase
    val users:DatabaseReference
    val recipies:DatabaseReference
    val auth:FirebaseAuth
    val context:Context

    constructor(context: Context)
    {
          db= FirebaseDatabase.getInstance("https://recipeapp-13143-default-rtdb.europe-west1.firebasedatabase.app");
          users=db.reference.child("users")
          recipies=db.reference.child("recipies")
           auth=FirebaseAuth.getInstance();
           this.context=context;
    }

  public  interface  ITask{
        fun onComplete()
        fun onError(msg:String)
        fun onComplete(list:ArrayList<Recipe>)
        fun onComplete(msg:String)
    }




     fun getRecipies(callback: ITask):ArrayList<Recipe>{
         val  list=ArrayList<Recipe>()

         recipies.addValueEventListener(object :ValueEventListener{
             override fun onDataChange(snapshot: DataSnapshot) {
                 list.clear()


                for(d in snapshot.children)
                {
                    var recipe=Recipe()

                    if(d.hasChild("ratings")) {
                        val snapshot = d.child("ratings")
                        val ratingList=ArrayList<Rating>()

                        for(c in snapshot.children)
                        {
                            val  rating=c.getValue(Rating::class.java)
                            ratingList.add(rating!!)
                        }

                        recipe.recipeName=d.child("recipeName").getValue(String::class.java)!!
                        recipe.recipeDescription=d.child("recipeDescription").getValue(String::class.java)!!
                        recipe.recipeAddedBy=d.child("recipeAddedBy").getValue(String::class.java)!!
                        recipe.recipeAddedByName=d.child("recipeAddedByName").getValue(String::class.java)!!
                        recipe.recipeIngredients=d.child("recipeIngredients").getValue(String::class.java)!!



                        recipe.ratings=ratingList;

                        recipe.rating=getRating(ratingList)


                    }else {

                        recipe = d.getValue(Recipe::class.java)!!
                    }

                    recipe?.recipeKey=d.key!!




                    list.add(recipe!!)
                }
                 callback.onComplete(list)


             }

             override fun onCancelled(error: DatabaseError) {
                 callback.onError("Failed to get Data")
             }

         })




         return list

     }

    fun getFavouriteRecipies(context: Context,callback: ITask):ArrayList<Recipe>{
        val  list=ArrayList<Recipe>()
        val  fav=SharedPref.getFavourites(context)

        recipies.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()


                for(d in snapshot.children)
                {
                    var recipe=Recipe()

                    if(d.hasChild("ratings")) {
                        val snapshot = d.child("ratings")
                        val ratingList=ArrayList<Rating>()

                        for(c in snapshot.children)
                        {
                            val  rating=c.getValue(Rating::class.java)
                            ratingList.add(rating!!)
                        }

                        recipe.recipeName=d.child("recipeName").getValue(String::class.java)!!
                        recipe.recipeDescription=d.child("recipeDescription").getValue(String::class.java)!!
                        recipe.recipeAddedBy=d.child("recipeAddedBy").getValue(String::class.java)!!
                        recipe.recipeAddedByName=d.child("recipeAddedByName").getValue(String::class.java)!!
                        recipe.recipeIngredients=d.child("recipeIngredients").getValue(String::class.java)!!



                        recipe.ratings=ratingList;
                        recipe.rating=getRating(ratingList)

                    }else {

                        recipe = d.getValue(Recipe::class.java)!!
                    }

                    recipe?.recipeKey=d.key!!




                    if(fav.contains(d.key)) {
                        list.add(recipe!!)
                    }
                }

                callback.onComplete(list)





            }

            override fun onCancelled(error: DatabaseError) {
                callback.onError("Failed to get Data")
            }

        })




        return list

    }




    fun addRecipe(callback: ITask,recipe: Recipe){
        recipies.push().setValue(recipe).addOnCompleteListener(OnCompleteListener {
            callback.onComplete("Recipe Added Successfully")
        }).addOnFailureListener( {
            callback.onError("Failed to add Recipe ${it.localizedMessage}")
        }).addOnCanceledListener {
            callback.onError("Failed to add Recipe " )
        }

    }

    fun removeRecipe(callback: ITask,id:String){
        recipies.child(id).removeValue().addOnCompleteListener(OnCompleteListener {
            callback.onComplete()
        }).addOnFailureListener {
            callback.onError("Failed to add Recipe ${it.localizedMessage}")
        }
    }
    fun updateRecipe(callback: ITask,id:String,recipe: Recipe){
        recipies.child(id).setValue(recipe).addOnCompleteListener(OnCompleteListener {
            callback.onComplete("Recipe Updated Successfully")
        }).addOnFailureListener {
            callback.onError("Failed to add Recipe ${it.localizedMessage}")
        }
    }


    fun rateRecipe(recipeKey:String,userKey:String,rating: Rating,callback: ITask)
    {
        recipies.child(recipeKey).child("ratings").child(userKey).setValue(rating).addOnCompleteListener({

            callback.onComplete()

        }).addOnFailureListener({
            callback.onError("Failed")
        })
    }


    fun login(callback:ITask,email:String,password:String)
    {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener({
            if(it.isSuccessful)
            {


                users.child(auth.uid.toString()).addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                      if(snapshot.exists())
                      {

                          val user=snapshot.getValue(User::class.java)
                          callback.onComplete("Logged in Succesfully")
                          SharedPref.storeUser(context,auth.uid.toString(),user?.name!!,user.imageUrl)

                          restorePref()
                      }

                        users.child(auth.uid.toString()).removeEventListener(this)

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })




            }else{
                callback.onError("Failed to login")
            }


        }).addOnFailureListener({
            callback.onError("Failed to login ${it.localizedMessage}")
        })



    }

    fun register(callback: ITask,user: User){
        auth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener({
           if(it.isSuccessful) {
               addUsertoDataBase(callback,it.result.user?.uid!!, user)
           }else{
               callback.onError("Failed to create user account")
           }
        }).addOnFailureListener({

            callback.onError("Failed to create user account")
        })

    }

  private  fun addUsertoDataBase(callback: ITask,id:String,user: User)
  {

      users.child(id).setValue(user).addOnCompleteListener ({
          if(it.isSuccessful)
              callback.onComplete("User created ")
      }).addOnFailureListener({
          callback.onError("Something went wrong while creating the user")
      })

  }







fun getRating(list:ArrayList<Rating>):Float{
    var rating=0.0F;

    for (r in list)
    {
        rating=rating+r.stars;

    }

    rating=rating/list.size


    return  rating;

}



    fun uploadProfileImage(userKey:String,imageUrl:String)
    {

        users.child(userKey).child("imageUrl").setValue(imageUrl).addOnSuccessListener {
            SharedPref.storeUserImage(context,imageUrl!!)
        }





    }

    fun removeProfileImage(userKey:String)
    {

        users.child(userKey).child("imageUrl").removeValue()





    }


    fun getUsersReference():DatabaseReference
    {
        return  users;
    }



  fun backupFavorites(){
    val str=  SharedPref.getFavouritesString(context)
      if(str.length>4) {
          users.child(SharedPref.getUserKey(context)!!).child("fav").setValue(str)
      }
  }

fun restorePref()
{
    users.child(SharedPref.getUserKey(context)!!).child("fav").addValueEventListener(object :ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
           try {
               val value=snapshot.getValue(String::class.java)
               SharedPref.addFavPref(value!!,context)
           }catch (e:Exception){}

        }

        override fun onCancelled(error: DatabaseError) {

        }

    })
}






}