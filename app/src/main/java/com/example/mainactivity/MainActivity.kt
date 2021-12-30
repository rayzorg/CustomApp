package com.example.mainactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonRegister.setOnClickListener {
           Register()
        }

        alreadyAccounttextView.setOnClickListener {
           goToLogin()
        }
    }


    private fun goToLogin(){
        Log.d("MainActivity","try to show login")
        val intent=Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }
    private fun Register(){
        val email=emailRegister.text.toString()
        val password=passwordRegister.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"Gelieve alle velden in te vullen",Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("MainActivity","email is"+email)
        Log.d("MainActivity","password is"+password)

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener

                Log.d("Main","User created: ${it.result.user?.uid}")

            }
            .addOnFailureListener {
                Log.d("Main","Niet gelukt om gebruiker aan te maken :${it.message} ")
                Toast.makeText(this,"Niet gelukt om gebruiker aan te maken :${it.message} ",Toast.LENGTH_SHORT).show()
            }
    }
}