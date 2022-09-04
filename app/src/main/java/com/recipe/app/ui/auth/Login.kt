package com.recipe.app.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.recipe.app.databinding.ActivityLoginBinding
import com.recipe.app.db.DbController
import com.recipe.app.db.SharedPref
import com.recipe.app.models.Recipe
import com.recipe.app.ui.main.activities.MainActivity
import com.recipe.app.utils.ViewUtils

class Login : AppCompatActivity() {
    lateinit var  binding: ActivityLoginBinding
    lateinit var  dbController: DbController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbController= DbController(this)

         binding.btnNewUser.setOnClickListener {
             if(validate())
             {

                 ViewUtils.showProgressDialog(this,"Logging In")

                 dbController.login(object :DbController.ITask{
                     override fun onComplete() {
                         ViewUtils.hideProgressDialog()
                         if(SharedPref.isLoggedIn(applicationContext))
                         {
                             move()
                         }
                     }

                     override fun onComplete(list: ArrayList<Recipe>) {
                         TODO("Not yet implemented")
                     }

                     override fun onComplete(msg: String) {
                         ViewUtils.hideProgressDialog()
                      if(SharedPref.isLoggedIn(applicationContext))
                      {
                        move()
                      }
                     }

                     override fun onError(msg: String) {
                         ViewUtils.hideProgressDialog()
                        showToast( msg)
                     }

                 },binding.etUsernameLog.text.toString(),binding.etPassLog.text.toString())



             }
         }


        binding.btnRegister.setOnClickListener {
            startActivity(Intent(Login@this,RegisterActivity::class.java))
        }
    }

    fun showToast(msg:String)
    {
        ViewUtils.showToast(Login@this,msg,true)
    }

    fun move()
    {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    fun validate():Boolean{

        if(binding.etUsernameLog.text.isEmpty())
        {
            ViewUtils.showToast(this,"Please enter your email",true)
            return false
        }

        if(binding.etPassLog.text.isEmpty())
        {
            ViewUtils.showToast(this,"Please enter your password",true)
            return false
        }

        return  true
    }
}