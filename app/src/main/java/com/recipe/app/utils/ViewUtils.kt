package com.recipe.app.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.recipe.app.R

class ViewUtils {

    companion object{
        lateinit var  pd:ProgressDialog

        fun showToast(context: Context ,msg:String)
        {

            val customToastLayout =(context as Activity).layoutInflater.inflate(R.layout.toast,(context as Activity).findViewById(R.id.toast_container))
            val customToast = Toast(context)
             val text:TextView=customToastLayout.findViewById(R.id.text)
            val icon:ImageView=customToastLayout.findViewById(R.id.icon)
            icon.visibility=View.GONE
            text.text=msg
            customToast.view = customToastLayout
            customToast.setGravity(Gravity.CENTER,0,0)
            customToast.duration = Toast.LENGTH_LONG
            customToast.show()

        }

        fun showToast(context: Context ,msg:String,isError:Boolean)
        {

            val customToastLayout =(context as Activity).layoutInflater.inflate(R.layout.toast,(context as Activity).findViewById(R.id.toast_container))
            val customToast = Toast(context)
            val text:TextView=customToastLayout.findViewById(R.id.text)
            val icon:ImageView=customToastLayout.findViewById(R.id.icon)
            if(isError)
            {
                icon.setImageDrawable(context.getDrawable(R.drawable.icon_error))
            }else{
                icon.setImageDrawable(context.getDrawable(R.drawable.icon_ok))
            }
            text.text=msg
            customToast.view = customToastLayout
            customToast.setGravity(Gravity.CENTER,0,0)
            customToast.duration = Toast.LENGTH_SHORT
            customToast.show()

        }

        fun showProgressDialog(context: Context,msg:String)
        {
            pd   = ProgressDialog(context)
           pd.setTitle(msg)
            pd.setMessage("Progressing..")
            pd.show()
        }

        fun hideProgressDialog()
        {
            pd?.hide()!!

        }



    }
}