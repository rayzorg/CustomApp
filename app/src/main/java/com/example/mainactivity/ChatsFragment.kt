package com.example.mainactivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.models.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
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
        recyclerView= view?.findViewById(R.id.recyclerview_latest_messages)
        recyclerView!!.adapter=adapter
        listenForLatestMessages()
        return view
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

                  // adapter.add(LatestMessageRow(message))

                }

            }

            override fun onChildChanged(p0: DataSnapshot, previousChildName: String?) {
                val message= p0.getValue(ChatMessage::class.java)

                if(message != null){
                    latestMessagesMap[p0.key!!]=message
                    refreshRecyclerViewMessages()

                    // adapter.add(LatestMessageRow(message))

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

    class LatestMessageRow(val chatMessage: ChatMessage): Item<ViewHolder>(){
        override fun bind(viewHolder: ViewHolder, position: Int) {

            viewHolder.itemView.textViewLatestMessage.text=chatMessage.text
        }

        override fun getLayout(): Int {
            return R.layout.latest_messages_row
        }

    }
}

