package com.example.mainactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.mainactivity.models.User
import com.google.firebase.auth.FirebaseAuth
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val toolbar: Toolbar =findViewById(R.id.toolbar_chat)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent= Intent(this@ChatLogActivity,MessagesActivity::class.java)
            startActivity(intent)
            finish()
        }


        val user=intent.getParcelableExtra<User>(SearchFragment.USER_KEY)


        supportActionBar!!.title= user?.username
        setupDummyData()
        button_send.setOnClickListener {
            sendMessage()

        }

    }

    private fun sendMessage() {

        val text=editTextSendContent.text.toString()
        val fromId= FirebaseAuth.getInstance().uid
        val user=intent.getParcelableExtra<User>(SearchFragment.USER_KEY)
        val toId= user?.uid
        val refMessages= FirebaseDatabase.getInstance("https://chatappcustomandroid-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/messages").push()
        val chatMessage= ChatMessage(refMessages.key!!,text, fromId!!, toId!!,System.currentTimeMillis()/1000)

        
        refMessages.setValue(chatMessage)
            .addOnSuccessListener {
                Toast.makeText(this,"saved text to database: ${refMessages.key}",Toast.LENGTH_SHORT).show()
            }
    }

    companion object{
        val TAG="chatlog"
    }

    private fun setupDummyData() {
        val adapter=GroupAdapter<ViewHolder>()

        adapter.add(ChatFromItem("from message"))
        adapter.add(ChatToItem("to message"))
        adapter.add(ChatToItem("to message"))
        adapter.add(ChatFromItem("from message"))
        recyclerview_chatlog.adapter=adapter
    }
}
class ChatMessage(val id:String,val text:String,val fromId:String, val toId:String,val timestamp:Long){

}
class ChatFromItem(val text:String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textViewRow.text=text


    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}
class ChatToItem(val text:String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.textViewTo.text=text

    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}