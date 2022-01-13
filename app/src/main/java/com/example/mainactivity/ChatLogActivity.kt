package com.example.mainactivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.mainactivity.models.ChatMessage
import com.example.mainactivity.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import kotlinx.android.synthetic.main.user_search_item_layout.view.*

class ChatLogActivity : AppCompatActivity() {
    val adapter = GroupAdapter<ViewHolder>()
    var toUser: User? = null
    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        recyclerview_chatlog.adapter = adapter

        val toolbar: Toolbar = findViewById(R.id.toolbar_chat)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@ChatLogActivity, MessagesActivity::class.java)
            startActivity(intent)
            finish()
        }

        toUser = intent.getParcelableExtra(SearchFragment.USER_KEY)

        supportActionBar!!.title = toUser?.username
        listenForMessages()
        button_send.setOnClickListener {
            sendMessage()
        }
    }

    private fun listenForMessages() {

        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid

        val messages = FirebaseDatabase.getInstance("https://chatappcustomandroid-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/user-messages/$fromId/$toId")
        messages.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, previousChildName: String?) {
                val message = p0.getValue(ChatMessage::class.java)
                if (message != null) {

                    println("fromid" + message.text)
                    if (message.fromId == FirebaseAuth.getInstance().uid) {
                        val currentUser = MessagesActivity.currentUser
                        adapter.add(ChatFromItem(message.text, currentUser!!))
                    } else {

                        adapter.add(ChatToItem(message.text, toUser!!))
                    }
                }
                recyclerview_chatlog.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    private fun sendMessage() {

        val text = editTextSendContent.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(SearchFragment.USER_KEY)
        val toId = user?.uid

        val refMessages = FirebaseDatabase.getInstance("https://chatappcustomandroid-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/user-messages/$fromId/$toId").push()
        val refToMessages = FirebaseDatabase.getInstance("https://chatappcustomandroid-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatMessage(refMessages.key!!, text, fromId!!, toId!!, System.currentTimeMillis() / 1000)

        refMessages.setValue(chatMessage)
            .addOnSuccessListener {
                editTextSendContent.text.clear()
                recyclerview_chatlog.scrollToPosition(adapter.itemCount - 1)
            }
        refToMessages.setValue(chatMessage)

        val latestrefMessages = FirebaseDatabase.getInstance("https://chatappcustomandroid-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/latest-messages/$fromId/$toId")
        latestrefMessages.setValue(chatMessage)
        val latestrefToMessages = FirebaseDatabase.getInstance("https://chatappcustomandroid-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/latest-messages/$toId/$fromId")
        latestrefToMessages.setValue(chatMessage)
    }
}

class ChatFromItem(val text: String, val user: User) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textViewRow.text = text

        val uri = user.profileImageUrl
        Picasso.with(viewHolder.itemView.textViewRow.context).load(uri).into(viewHolder.itemView.imageViewFrom)
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}
class ChatToItem(val text: String, val user: User) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.textViewTo.text = text
        val uri = user.profileImageUrl
        Picasso.with(viewHolder.itemView.textViewTo.context).load(uri).into(viewHolder.itemView.imageViewTo)
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}
