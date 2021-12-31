package com.example.mainactivity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        buttonInloggen.setOnClickListener {
           inloggen()
        }

        noAccountTextView.setOnClickListener {
            finish()
        }
    }

    private fun inloggen (){
        val email=emailLogin.text.toString()
        val password=passwordLogin.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"Gelieve alle velden in te vullen",Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener

                Log.d("LoginActivity","User ingelogd: ${it.result.user?.uid}")
            }
            .addOnFailureListener{
                Toast.makeText(this,"Niet gelukt om in te loggen :${it.message} ",
                    Toast.LENGTH_SHORT).show()
            }
    }
}