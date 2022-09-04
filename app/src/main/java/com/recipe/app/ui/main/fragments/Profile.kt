package com.recipe.app.ui.main.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.recipe.app.R
import com.recipe.app.databinding.FragmentProfileBinding
import com.recipe.app.db.DbController
import com.recipe.app.db.SharedPref
import com.recipe.app.ui.auth.Login
import com.recipe.app.ui.main.activities.IFragmentCallbacks
import com.recipe.app.ui.main.activities.IFragmentProfileChooseCallback
import com.recipe.app.ui.main.activities.MainActivity
import com.squareup.picasso.Picasso

class Profile(val callBack:IFragmentProfileChooseCallback) : Fragment() {
    lateinit var  binding:FragmentProfileBinding
    lateinit var  dbController: DbController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding=FragmentProfileBinding.inflate(layoutInflater)

        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadImage()
  dbController=DbController(activity as Context)


        binding.userName.text=""+SharedPref.getUserName(activity as Context)!!


        binding.profile.setOnClickListener {
            val   url:String= SharedPref.getUserImage(activity as Context)!!
            if(url?.length!!>3)
            {
                BottomSheet.showProfileModifyBottomSheet(activity as Activity,object:BottomSheet.IBottomSheet{
                    override fun onOptionClicked(option: Int) {
                      if(option==1)
                      {
                        callBack.chooseImage()
                      }
                        if(option==2)
                        {
                           callBack.removeImage()
                            binding.profile.setImageDrawable(activity?.getDrawable(R.drawable.icon_person))
                        }
                    }

                })

            }else {

                callBack.chooseImage()
            }
           }

        binding.logout.setOnClickListener {
            dbController.backupFavorites()
           Handler().postDelayed(Runnable {
               SharedPref.clearPref(activity as Context)
               val intent=Intent(activity, Login::class.java)
               (activity)?.startActivity(intent)
               (activity)?.finish()


           },1000)
        }
    }


    fun loadImage()
    {
     val   url:String= SharedPref.getUserImage(activity as Context)!!
        if(url.length>4) {
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.icon_person)
                .error(R.drawable.icon_person)
                .into(binding.profile);
        }
    }
}