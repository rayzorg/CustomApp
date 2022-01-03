package com.example.mainactivity.views

import com.example.mainactivity.models.ChatMessage
import com.example.mainactivity.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_messages_row.view.*




class LatestMessageRow(val chatMessage: ChatMessage): Item<ViewHolder>(){
    var partnerUser:User? = null


    override fun bind(viewHolder: ViewHolder, position: Int) {

        val partnerId: String
        if(chatMessage.fromId== FirebaseAuth.getInstance().uid){
            partnerId=chatMessage.toId
        }else{
            partnerId=chatMessage.fromId
        }
        val refUsers= FirebaseDatabase.getInstance("https://chatappcustomandroid-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/users/${partnerId}")
        refUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                partnerUser=p0.getValue(User::class.java)
                val uri=partnerUser?.profileImageUrl
                viewHolder.itemView.textViewUsernameLatestMessage.text=partnerUser?.username
                Picasso.with(viewHolder.itemView.textViewUsernameLatestMessage.context).load(uri).into(viewHolder.itemView.imageViewLatestMessage)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        viewHolder.itemView.textViewLatestMessage.text=chatMessage.text
    }

    override fun getLayout(): Int {
        return com.example.mainactivity.R.layout.latest_messages_row
    }

}