package com.recipe.app.ui.main.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.recipe.app.R
import com.recipe.app.db.SharedPref
import com.recipe.app.ui.auth.Login

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(Runnable {
                 if(SharedPref.isLoggedIn(applicationContext))
                 {
                     startActivity(Intent(Splash@this, MainActivity::class.java))
                 }else{
                     startActivity(Intent(Splash@this, Login::class.java))
                 }
            finish()


        },2000)
    }
}