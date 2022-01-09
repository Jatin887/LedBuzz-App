package com.example.ledbuzz.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.ledbuzz.MainActivity
import com.example.ledbuzz.R
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val topAnim = AnimationUtils.loadAnimation(this,R.anim.top)
        val bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom)
        val logo=findViewById<ImageView>(R.id.logo)
        val logoImg=findViewById<ImageView>(R.id.LogoImg)
        logoImg.startAnimation(topAnim)
        logo.startAnimation(bottomAnim)

        mAuth= FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        val timeout = 4000
        val homeIntent= Intent(this, MainActivity::class.java)

        Handler().postDelayed({
            if (user!=null){
                startActivity(Intent(this,UserActivity::class.java))
                finish()
            }
            else{
                startActivity(homeIntent)
            }
        }, timeout.toLong())

    }

}