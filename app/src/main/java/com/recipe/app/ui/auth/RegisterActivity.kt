package com.recipe.app.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.recipe.app.databinding.ActivityRegisterBinding
import com.recipe.app.db.DbController
import com.recipe.app.models.Recipe
import com.recipe.app.models.User
import com.recipe.app.utils.ViewUtils

class RegisterActivity : AppCompatActivity() {
    lateinit var  binding: ActivityRegisterBinding
    lateinit var dbController:DbController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbController= DbController(this)
        binding.btnNewUser.setOnClickListener{
           if(validate())
           {
               val name=binding.etNameReg.text.toString()+" "+binding.etLastNameReg.text.toString()
               val user=User(name,
                   " ",
                   " ",
                   binding.etUsernameReg.text.toString(),
                   binding.etPassReg.text.toString()
               )
               ViewUtils.showProgressDialog(this,"Creating Your Account")
               dbController.register(object :DbController.ITask{
                   override fun onComplete() {
                       showToast("Account Created ,Please login",false)
                       move()
                   }

                   override fun onComplete(list: ArrayList<Recipe>) {
                       TODO("Not yet implemented")
                   }

                   override fun onComplete(msg: String) {
                       showToast("Account Created ,Please login",false)
                       move()
                   }

                   override fun onError(msg: String) {
                       ViewUtils.hideProgressDialog()
                       showToast("Failed to create  Account",true)
                   }

               },user)




           }
        }

    }

    fun showToast(msg:String,isError:Boolean)
    {
        ViewUtils.showToast(Login@this,msg,isError)
    }

    fun move()
    {
        ViewUtils.hideProgressDialog()
        startActivity(Intent(this, Login::class.java))
        finish()
    }

    fun validate():Boolean{
        if(binding.etNameReg.text.isEmpty())
        {
            ViewUtils.showToast(this,"Please enter your first name",true)
            return  false
        }

        if(binding.etLastNameReg.text.isEmpty())
        {
            ViewUtils.showToast(this,"Please enter your last name",true)
            return  false
        }

        if(binding.etUsernameReg.text.isEmpty())
        {
            ViewUtils.showToast(this,"Please enter your email",true)
            return  false
        }
        if(binding.etPassReg.text.isEmpty())
        {
            ViewUtils.showToast(this,"Please enter a password",true)
            return  false
        }

        return  true;
    }
}