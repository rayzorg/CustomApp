package com.example.mainactivity

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.mainactivity.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_messages.*

import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class SettingsFragment : Fragment() {

    var refUsers: DatabaseReference?=null
    var firebaseUser: FirebaseUser? =null
    var currentUser:User? = null
    var selectedImage: Uri? = null
    var launchSomeActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri= data?.data
            selectedImage  = data?.data
            try {
                if (Build.VERSION.SDK_INT < 29) {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        activity?.contentResolver,
                        selectedImage
                    )


                    val cirleImage: CircleImageView? =view?.findViewById(R.id.imageViewUserSettings)

                    val buttonPicture:Button=view?.findViewById(R.id.buttonChangePicture)!!
                    cirleImage?.setImageBitmap(bitmap)
                    buttonPicture.alpha=0f
                } else {
                    val source: ImageDecoder.Source =
                        ImageDecoder.createSource(activity?.contentResolver!!, selectedImage!!)
                    val bitmap = ImageDecoder.decodeBitmap(source)



                    val imageUser:ImageView= view?.findViewById(R.id.imageViewUserSettings)!!
                    val buttonPicture:Button=view?.findViewById(R.id.buttonChangePicture)!!
                    imageUser.setImageBitmap(bitmap )

                    buttonPicture.alpha=0f
                }
            } catch (e: IOException) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=  inflater.inflate(R.layout.fragment_settings, container, false)

        fetchCurrentUser()
        firebaseUser= FirebaseAuth.getInstance().currentUser
        refUsers= FirebaseDatabase.getInstance("https://chatappcustomandroid-default-rtdb.europe-west1.firebasedatabase.app/").reference.child("users").child(firebaseUser!!.uid)

        refUsers!!.addValueEventListener((object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    val textviewNaam: TextView=view.findViewById(R.id.textViewNameSettings)
                    val imageUser:ImageView=view.findViewById(R.id.imageViewUserSettings)


                    val user:User?=snapshot.getValue(User::class.java)
                    textviewNaam.text=user!!.username


                    Picasso.with(view.context).load(user!!.profileImageUrl).into(imageUser)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }))


        val buttonChangePicture:Button=view.findViewById(R.id.buttonChangePicture)
        buttonChangePicture.setOnClickListener {
            openYourActivity()
        }
        val buttonChange:Button=view.findViewById(R.id.buttonChangeProfile)

        return view
    }

    private fun openYourActivity() {

            val intent=Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            launchSomeActivity.launch(intent)

    }

    private fun fetchCurrentUser() {
        val uid=FirebaseAuth.getInstance().uid
        val ref=FirebaseDatabase.getInstance("https://chatappcustomandroid-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/users/${uid}")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                currentUser =p0.getValue(User::class.java)

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}