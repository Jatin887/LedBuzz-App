package com.example.ledbuzz.view

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.ledbuzz.MainActivity
import com.example.ledbuzz.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

@Suppress("DEPRECATION")
class RegisterActivity : AppCompatActivity() {
    lateinit var binding:ActivityRegisterBinding
    lateinit var mAuth: FirebaseAuth
    lateinit var ref: DatabaseReference
    lateinit var mDatabase: FirebaseDatabase
    lateinit var mProgressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mProgressDialog= ProgressDialog(this)
        mAuth = FirebaseAuth.getInstance()
        binding.btnRegister.setOnClickListener{
            val username = binding.InputName.text.toString().trim()
            val userEmail = binding.inputEmail.text.toString().trim()
            val userPassword = binding.inputPassword.text.toString().trim()

            if (TextUtils.isEmpty(username)){
                binding.InputName.error="Enter User Name"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(userEmail)){
                binding.inputEmail.error="Enter Email"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(userPassword)){
                binding.inputPassword.error="Enter User Name"
                return@setOnClickListener
            }
//
            if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                binding.inputEmail.error="Enter Valid Email"
                return@setOnClickListener
            }
            if(userPassword.length<8){
                binding.inputPassword.error="Password should contain 8 characters"
                return@setOnClickListener
            }
            registerUser(username,userEmail,userPassword)
        }
    }
    private fun registerUser(Username:String,UserEmail:String,UserPassword:String){
        mProgressDialog.setMessage("Loading Please Wait..")
        mProgressDialog.show()
        mAuth.createUserWithEmailAndPassword(UserEmail,UserPassword).addOnCompleteListener(this){task ->
            if(task.isSuccessful) {
                val currentUser = mAuth.currentUser
                val currentId = mAuth.currentUser!!.uid
                Toast.makeText(this,"Successfully registered", Toast.LENGTH_LONG).show()
                val intent= Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else {
                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
            mProgressDialog.dismiss()
        }
    }

}