package com.example.ledbuzz

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.ledbuzz.databinding.ActivityMainBinding
import com.example.ledbuzz.model.Subject
import com.example.ledbuzz.view.RegisterActivity
import com.example.ledbuzz.view.UserActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var mAuth: FirebaseAuth
//    lateinit var ref: DatabaseReference
    lateinit var mProgressDialog: ProgressDialog
    private lateinit var database: DatabaseReference
//    lateinit var mDatabase: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
    //        mDatabase = FirebaseDatabase.getInstance()
         database = Firebase.database.reference


        binding.goToRegister.setOnClickListener {
            val startIntent = Intent(this, RegisterActivity::class.java)
            startActivity(startIntent)
        }



        mProgressDialog= ProgressDialog(this)
        val currentUser=mAuth.currentUser
        val uid = mAuth.uid



        binding.loginButton.setOnClickListener{
            val  email=binding.inEmail.text.toString().trim()
            val pass=binding.inPassword.text.toString().trim()
            if(TextUtils.isEmpty(email)){
                binding.inEmail.error="Enter Email"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(pass)){
                binding.inPassword.error="Enter password"
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.inEmail.error="Enter Valid Email"
                return@setOnClickListener
            }
            loginUser(email,pass)
//            if (uid != null) {
//                database.child("users").child(uid).child("Data").setValue(Subject(0,"jatin","jatin"))
//            }
        }
    }
    private fun loginUser(Email:String,Password:String){
        mProgressDialog.setMessage("Loading Please Wait..")
        mProgressDialog.show()
        mAuth.signInWithEmailAndPassword(Email, Password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"Sign In Sucessful",Toast.LENGTH_SHORT).show()
                    val intent= Intent(this, UserActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Wrong Password",
                        Toast.LENGTH_SHORT).show()
                }
                mProgressDialog.dismiss()
            }

    }


    public override fun onStart(){
        super.onStart()
        val currentUser = mAuth.currentUser
        if(currentUser!=null){
            updateUI(currentUser)
        }
    }
    fun updateUI(currentUser: FirebaseUser?){

    }




}
