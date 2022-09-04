package com.recipe.app.ui.main.fragments

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.recipe.app.R

class BottomSheet {

    interface  IBottomSheet{
        fun onOptionClicked(option:Int)
    }

    interface  IBottomSheetRating{
        fun onOptionClicked(rating:Float)
    }

    companion object{
        fun showFilterBottomSheet(con:Activity,callBack:IBottomSheet)
        {
            val dialog = BottomSheetDialog(con)
            val view = con.layoutInflater.inflate(R.layout.bottom_sheet, null)
            val  close=view.findViewById<ImageView>(R.id.close)
            val  AC1=view.findViewById<TextView>(R.id.a1)
            val  AC2=view.findViewById<TextView>(R.id.a2)
            close.setOnClickListener {
                dialog.dismiss()
            }
            AC1.setOnClickListener {
                callBack.onOptionClicked(1)
                dialog.dismiss()
            }

            AC2.setOnClickListener {
                callBack.onOptionClicked(2)
                dialog.dismiss()
            }



            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        }


        fun showRecipeModifyBottomSheet(con:Activity,callBack:IBottomSheet)
        {
            val dialog = BottomSheetDialog(con)
            val view = con.layoutInflater.inflate(R.layout.bottom_sheet, null)
            val  close=view.findViewById<ImageView>(R.id.close)
            val  AC1=view.findViewById<TextView>(R.id.a1)
            val  AC2=view.findViewById<TextView>(R.id.a2)
            val  title=view.findViewById<TextView>(R.id.title)
           title.setText("More")
            AC1.setText("Edit Recipe")
            AC2.setText("Delete  Recipe")
            AC2.setTextColor(con.getColor(R.color.red))
            close.setOnClickListener {
                dialog.dismiss()
            }
            AC1.setOnClickListener {
                callBack.onOptionClicked(1)
                dialog.dismiss()
            }

            AC2.setOnClickListener {
                callBack.onOptionClicked(2)
                dialog.dismiss()
            }



            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()

        }


        fun showRatingBar(con:Activity,callBack:IBottomSheetRating)
        {
            val dialog = BottomSheetDialog(con)
            val view = con.layoutInflater.inflate(R.layout.bottom_sheet_rating, null)
            val  close=view.findViewById<ImageView>(R.id.close)
            val  ratingBar=view.findViewById<RatingBar>(R.id.ratingBar)
            close.setOnClickListener {
                dialog.dismiss()
            }
            val  title=view.findViewById<TextView>(R.id.title)
            title.setText("Rate Recipe")

            ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
                callBack.onOptionClicked(fl)
                dialog.dismiss() }


            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()

        }


        fun showProfileModifyBottomSheet(con:Activity,callBack:IBottomSheet)
        {
            val dialog = BottomSheetDialog(con)
            val view = con.layoutInflater.inflate(R.layout.bottom_sheet, null)
            val  close=view.findViewById<ImageView>(R.id.close)
            val  AC1=view.findViewById<TextView>(R.id.a1)
            val  AC2=view.findViewById<TextView>(R.id.a2)
            val  title=view.findViewById<TextView>(R.id.title)
            title.setText("Profile")
            AC1.setText("Change Profile")
            AC2.setText("Remove Profile")
            AC2.setTextColor(con.getColor(R.color.red))
            close.setOnClickListener {
                dialog.dismiss()
            }
            AC1.setOnClickListener {
                callBack.onOptionClicked(1)
                dialog.dismiss()
            }

            AC2.setOnClickListener {
                callBack.onOptionClicked(2)
                dialog.dismiss()
            }



            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()

        }
    }

}