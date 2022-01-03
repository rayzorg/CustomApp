package com.example.mainactivity

import android.os.Bundle
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

class SettingsFragment : Fragment() {

    var refUsers: DatabaseReference?=null
    var firebaseUser: FirebaseUser? =null
    var currentUser:User? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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
        return view
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