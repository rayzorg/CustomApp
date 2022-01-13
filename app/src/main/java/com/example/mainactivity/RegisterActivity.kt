package com.example.mainactivity

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.mainactivity.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private var selectedImage: Uri? = null
    private var launchSomeActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            selectedImage = data?.data
            try {
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(contentResolver, selectedImage!!)
                val bitmap = ImageDecoder.decodeBitmap(source)

                selectPhotoImageView.setImageBitmap(bitmap)
                pictureButton.alpha = 0f
            } catch (e: IOException) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar_register)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Registreren"

        buttonRegister.setOnClickListener {
            register()
        }
        alreadyAccounttextView.setOnClickListener {
            goToLogin()
        }
        pictureButton.setOnClickListener {
            Log.d("Main", "try to show selector")
            openYourActivity()
        }
    }

    private fun openYourActivity() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        launchSomeActivity.launch(intent)
    }
    private fun goToLogin() {
        Log.d("MainActivity", "try to show login")
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    private fun register() {
        val email = emailRegister.text.toString()
        val password = passwordRegister.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Gelieve alle velden in te vullen", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                uploadImage()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Niet gelukt om gebruiker aan te maken :${it.message} ", Toast.LENGTH_SHORT).show()
            }
    }
    private fun uploadImage() {
        if (selectedImage == null)return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedImage!!)
            .addOnSuccessListener {
                Toast.makeText(this, "succesfully uploade image: ${it.metadata?.path}", Toast.LENGTH_SHORT).show()
                ref.downloadUrl.addOnSuccessListener {
                    Toast.makeText(this, "file location: $it", Toast.LENGTH_SHORT).show()
                    saveUserToDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "failed uploading image ", Toast.LENGTH_SHORT).show()
            }
    }
    private fun saveUserToDatabase(imageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance("https://chatappcustomandroid-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/users/$uid")

        val user = User(uid!!, usernameRegister.text.toString(), imageUrl, usernameRegister.text.toString().lowercase())
        ref.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "save user to database", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "save user failed", Toast.LENGTH_SHORT).show()
            }
    }
}
