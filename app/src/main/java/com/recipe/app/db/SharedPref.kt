package com.recipe.app.db

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.recipe.app.models.User


class SharedPref {

    companion object{
        val  SHARED_PREF="MySharedPref"
        val  FAVOURITES="FAVOURITES"
        fun storeUser(context: Context,key:String,name:String,imageurl:String){
            val sharedPreferences: SharedPreferences =
               context. getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
            val myEdit = sharedPreferences.edit()
            myEdit.putString("key",key)
            myEdit.putString("name", name)
            myEdit.putString("image", imageurl)
            myEdit.apply()

        }


        fun storeUserImage(context: Context,image:String){
            val sharedPreferences: SharedPreferences =
                context. getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
            val myEdit = sharedPreferences.edit()
            myEdit.putString("image", image)
            myEdit.apply()

        }
        fun getUserKey(context: Context): String? {
            val sh: SharedPreferences =context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
            val s1 = sh.getString("key", "")
            return  s1;
        }

        fun getUserImage(context: Context): String? {
            val sh: SharedPreferences =context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
            val s1 = sh.getString("image", "")
            return  s1;
        }

        fun getUserName(context: Context): String? {
            val sh: SharedPreferences =context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
            val s1 = sh.getString("name", "")
            return  s1;
        }


        fun isLoggedIn(context: Context):Boolean{
            val sh: SharedPreferences =context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
            val s1 = sh.getString("key", "")
            if(s1?.length!!>2)
            {
                return  true
            }


            return  false
        }


        fun getFavourites(context: Context):ArrayList<String>{
            val list=ArrayList<String>()
            val keys= getFavouritesString(context)
            val keysList=keys.split(",")
            for (k in keysList)
            {
                list.add(k)
            }
            return list;
        }

        fun getFavouritesString(context: Context):String{
            val sh: SharedPreferences =context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
            val s1 = sh.getString(FAVOURITES, "")
           return  s1!!
        }



      fun  addToFavourites(context: Context,key:String){
          val sh: SharedPreferences =context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
          val myEdit = sh.edit()
          var  s= getFavouritesString(context);
          s=s+",$key"
          myEdit.putString(FAVOURITES,s)
          myEdit.apply()
        }


        fun  removeFromFavourites(context: Context,key:String){
            val  list= getFavourites(context)
            if(list.contains(key))
            {
                list.remove(key)
            }
            var keys=""
            for(k in list)
            {
                keys=keys+",$k"
            }
            val sh: SharedPreferences =context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
            val myEdit = sh.edit()
            myEdit.putString(FAVOURITES,keys)
            myEdit.apply()
        }


        fun clearPref(con:Context)
        {
            val sh: SharedPreferences =con.getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
            val myEdit = sh.edit()
            myEdit.clear()
            myEdit.apply()

        }


        fun addFavPref(fav:String,context: Context)
        {
            val sh: SharedPreferences =context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
            val myEdit = sh.edit()
            myEdit.putString(FAVOURITES,fav)
            myEdit.apply()

        }


    }

}