package com.example.mainactivity

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.mainactivity.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_messages.*

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.mainactivity.models.ChatMessage
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap


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

        buttonChange.setOnClickListener {
            updateProfile()
            editTextChangeName.text.clear()

        }
        return view
    }



    private fun openYourActivity() {

            val intent=Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            launchSomeActivity.launch(intent)

    }
    private fun updateProfile(){


        val hashMap = HashMap<String, String>()


        hashMap.put("username", editTextChangeName.text.toString())
        hashMap.put("search", editTextChangeName.text.toString())


        if(editTextChangeName.text.isEmpty() ) {
            Toast.makeText(view?.context,"Gelieve alle velden in te vullen",Toast.LENGTH_SHORT).show()
            return
        }
        refUsers?.updateChildren(hashMap as Map<String, Any>)?.addOnSuccessListener {

            Toast.makeText(view?.context,"user updated",Toast.LENGTH_SHORT).show()
        }
        uploadImage()


    }
    private fun uploadImage() {
        if(selectedImage == null)return

        val hashMap = HashMap<String, String>()




        val filename= UUID.randomUUID().toString()
        val ref= FirebaseStorage.getInstance().getReference("/images/${filename}")

        ref.putFile(selectedImage!!)
            .addOnSuccessListener {
                Toast.makeText(view?.context,"succesfully uploade image: ${it.metadata?.path}",Toast.LENGTH_SHORT).show()
                ref.downloadUrl.addOnSuccessListener {
                    Toast.makeText(view?.context,"file location: $it",Toast.LENGTH_SHORT).show()
                   // saveUserToDatabase(it.toString())
                    hashMap.put("profileImageUrl", it.toString())
                    refUsers?.updateChildren(hashMap as Map<String, Any>)?.addOnSuccessListener {

                        Toast.makeText(view?.context,"user updated",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener{
                Toast.makeText(view?.context,"failed uploading image ",Toast.LENGTH_SHORT).show()
            }
    }
    private fun saveUserToDatabase(imageUrl:String) {
        val status="offline"
        val uid= FirebaseAuth.getInstance().uid
        val ref= FirebaseDatabase.getInstance("https://chatappcustomandroid-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/users/${uid}")


        val user=User(uid!!,usernameRegister.text.toString(),imageUrl,usernameRegister.text.toString().lowercase(),status)

        ref.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(view?.context,"save user to database",Toast.LENGTH_SHORT).show()



            }
            .addOnFailureListener{
                Toast.makeText(view?.context,"save user failed",Toast.LENGTH_SHORT).show()
            }
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