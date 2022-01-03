package com.example.mainactivity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.models.ChatMessage
import com.example.mainactivity.models.User
import com.example.mainactivity.views.LatestMessageRow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.latest_messages_row.view.*

class ChatsFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    val adapter=GroupAdapter<ViewHolder>()
    val latestMessagesMap = HashMap<String,ChatMessage>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view: View = inflater.inflate(R.layout.fragment_chats, container, false)
        //recyclerView= view?.findViewById(R.id.recyclerview_latest_messages)
       // val textview :TextView=view?.findViewById(R.id.textViewLatestMessage)
        //recyclerView!!.adapter=adapter
        //recyclerView!!.addItemDecoration(DividerItemDecoration(textview.context,DividerItemDecoration.VERTICAL))
        //listenForLatestMessages()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView= view?.findViewById(R.id.recyclerview_latest_messages)
        //val textview :TextView=view?.findViewById(R.id.textViewUsernameLatestMessage)
        recyclerView!!.adapter=adapter
        adapter.setOnItemClickListener { item, view ->
            val intent=Intent(view.context,ChatLogActivity::class.java)

            val row= item as LatestMessageRow

            intent.putExtra(SearchFragment.USER_KEY,row.partnerUser)
            startActivity(intent)
        }
        //recyclerView!!.addItemDecoration(DividerItemDecoration(textview.context,DividerItemDecoration.VERTICAL))
        listenForLatestMessages()
    }
    private fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val refUsers= FirebaseDatabase.getInstance("https://chatappcustomandroid-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/latest-messages/${fromId}")
        refUsers.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, previousChildName: String?) {
                val message= p0.getValue(ChatMessage::class.java)

                if(message != null){
                    latestMessagesMap[p0.key!!]=message
                    refreshRecyclerViewMessages()
                }

            }
            override fun onChildChanged(p0: DataSnapshot, previousChildName: String?) {
                val message= p0.getValue(ChatMessage::class.java)

                if(message != null){
                    latestMessagesMap[p0.key!!]=message
                    refreshRecyclerViewMessages()
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        latestMessagesMap.values.forEach{
            adapter.add(LatestMessageRow(it))
        }
    }

}

