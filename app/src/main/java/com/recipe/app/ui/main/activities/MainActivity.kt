package com.recipe.app.ui.main.activities

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.recipe.app.R
import com.recipe.app.databinding.ActivityMainBinding
import com.recipe.app.db.DbController
import com.recipe.app.db.SharedPref
import com.recipe.app.models.Recipe
import com.recipe.app.ui.main.fragments.BottomSheet
import com.recipe.app.ui.main.fragments.FavouriteRecipies
import com.recipe.app.ui.main.fragments.Profile
import com.recipe.app.ui.main.fragments.Recipies
import java.io.File
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {
   lateinit var  binding:ActivityMainBinding
    private var filePath: Uri? = null
    private val PICK_IMAGE_REQUEST = 71
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    lateinit var dbController:DbController

    companion object{
        lateinit var currentViewRecipe:Recipe
      private  var fragementCallbacks: IFragmentCallbacks?=null
        fun getCurrentRecipe():Recipe{
            return currentViewRecipe;
        }

        fun  setCurrentRecipe(recipe: Recipe)
        {
            currentViewRecipe=recipe;
        }

        fun setCallBack(callbacks: IFragmentCallbacks)
        {
            fragementCallbacks=callbacks
        }

    }


   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Recipies())

     //  BottomSheet.show(this)
       dbController= DbController(this)

       dbController.backupFavorites()
       dbController.restorePref()
       storage = FirebaseStorage.getInstance();
       storageReference = storage?.getReference()!!


       binding.bottomNavigationView.setOnItemSelectedListener { item ->
           when (item.itemId) {


               R.id.home ->{
                   binding.toolBar.addRecipe.visibility=View.VISIBLE
                   binding.toolBar.filter.visibility=View.VISIBLE
                   replaceFragment(Recipies())

               }
               R.id.favorite -> {
                   binding.toolBar.addRecipe.visibility=View.GONE
                   binding.toolBar.filter.visibility=View.GONE
                   replaceFragment(FavouriteRecipies())
               }
               R.id.profil -> {
                   binding.toolBar.addRecipe.visibility=View.GONE
                   binding.toolBar.filter.visibility=View.GONE
                   replaceFragment(Profile(object :IFragmentProfileChooseCallback{
                       override fun chooseImage() {
                           chooseImageFromGallery()
                       }

                       override fun removeImage() {
                          removeProfile()
                       }

                   }))
               }


           }
           true
       }
       binding.toolBar.addRecipe.visibility=View.VISIBLE
       binding.toolBar.addRecipe.setOnClickListener {
           var i= Intent(MainActivity@this, AddRecipe::class.java)
           startActivity(i)

       }

       binding.toolBar.filter.setOnClickListener {

           BottomSheet.showFilterBottomSheet(MainActivity@this,object:BottomSheet.IBottomSheet{
               override fun onOptionClicked(option: Int) {
                   fragementCallbacks?.onFilterSelected(option)!!

               }

           })
        }



    }

    private fun removeProfile() {
        SharedPref.storeUserImage(applicationContext,"")
        dbController.removeProfileImage(SharedPref.getUserKey(MainActivity@this)!!)
    }

    fun chooseImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }
    fun replaceFragment(fragment: Fragment) {

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(binding.fragmentWrapper.id, fragment, fragment.toString())
    //    fragmentTransaction.addToBackStack(fragment.toString())
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                uploadImage()
           //     imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }






    }

    private fun uploadImage() {
        if (filePath != null) {
            val progress = ProgressDialog(this)
            progress.setTitle("Uploading....")
            progress.show()
            val ref: StorageReference =
                storageReference?.child("images/" +SharedPref.getUserKey(this))!!

            var uploadTask = ref.putFile(filePath!!)

            val urlTask = uploadTask.continueWithTask { task ->

                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }

                ref.downloadUrl


            }.addOnCompleteListener { task ->
                progress.dismiss()
                if (task.isSuccessful) {
                    val downloadUri = task.result

                    dbController.uploadProfileImage(SharedPref.getUserKey(MainActivity@this)!!,downloadUri.toString())






                } else {
                    // Handle failures
                    // ...

                }
            }
        }
    }







}