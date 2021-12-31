package com.example.mainactivity

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.SyncStateContract
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

import android.R.attr

import android.graphics.Bitmap

import android.os.Build

import android.R.attr.data
import android.net.Uri
import java.io.IOException


class RegisterActivity : AppCompatActivity() {
    var launchSomeActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri= data?.data
            val selectedImage: Uri? = data?.data
            try {
                if (Build.VERSION.SDK_INT < 29) {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        contentResolver,
                        selectedImage
                    )



                    selectPhotoImageView.setImageBitmap(bitmap)
                    pictureButton.alpha=0f
                } else {
                    val source: ImageDecoder.Source =
                        ImageDecoder.createSource(contentResolver, selectedImage!!)
                    val bitmap = ImageDecoder.decodeBitmap(source)




                    selectPhotoImageView.setImageBitmap(bitmap)
                    pictureButton.alpha=0f
                }
            } catch (e: IOException) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        buttonRegister.setOnClickListener {
           Register()
        }
        alreadyAccounttextView.setOnClickListener {
           goToLogin()
        }
        pictureButton.setOnClickListener {
            Log.d("Main","try to show selector")
            openYourActivity()
        }
    }

    fun openYourActivity() {
        val intent=Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        launchSomeActivity.launch(intent)
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