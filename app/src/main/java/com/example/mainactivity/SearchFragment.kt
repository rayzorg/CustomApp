package com.example.mainactivity

import android.content.Intent
import android.os.Bundle
import android.provider.Contacts.SettingsColumns.KEY
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.mainactivity.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_messages.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.user_search_item_layout.view.*

class SearchFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var mUsers:List<User>? = null
    private var searchEdit: EditText? = null

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view:View=  inflater.inflate(R.layout.fragment_search, container, false)
        searchEdit=view.findViewById(R.id.searchUsers)
        mUsers=ArrayList()
        fetchUsers()

        searchEdit!!.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchForUsers(p0.toString().lowercase())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        return view
    }
    companion object{
     val USER_KEY="USER_KEY"
}
    private fun fetchUsers() {
        var firebaseUserId=FirebaseAuth.getInstance().currentUser!!.uid
        val refUsers= FirebaseDatabase.getInstance("https://chatappcustomandroid-default-rtdb.europe-west1.firebasedatabase.app/").reference.child("users")
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val adapter=GroupAdapter<ViewHolder>()
                if (searchEdit!!.text.toString() == ""){
                    for (snapshot in p0.children){
                        val user:User?=snapshot.getValue(User::class.java)
                        if(!(user!!.uid).equals(firebaseUserId)){
                            adapter.add(UserItem(user))
                        }
                    }
                    adapter.setOnItemClickListener { item, view ->
                        val userItem=item as UserItem
                        val intent= Intent(view.context,ChatLogActivity::class.java)
                        intent.putExtra(USER_KEY,userItem.user)

                        startActivity(intent)

                    }
                    recyclerView= view?.findViewById(R.id.searchList)
                    recyclerView!!.adapter=adapter
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    private fun searchForUsers(str:String){
        var firebaseUserId=FirebaseAuth.getInstance().currentUser!!.uid

        val queryUsers= FirebaseDatabase.getInstance("https://chatappcustomandroid-default-rtdb.europe-west1.firebasedatabase.app/").reference.child("users")
            .orderByChild("search")
            .startAt(str)
            .endAt(str + "\uf8ff")

        queryUsers.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val adapter=GroupAdapter<ViewHolder>()
                for (snapshot in p0.children){
                    val user:User?=snapshot.getValue(User::class.java)
                    if(!(user!!.uid).equals(firebaseUserId)){
                        adapter.add(UserItem(user))
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val userItem=item as UserItem
                    val intent= Intent(view.context,ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY,userItem.user)

                    startActivity(intent)

                }
                recyclerView= view?.findViewById(R.id.searchList)
                recyclerView!!.adapter=adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}
class UserItem(val user:User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.username_user.text=user.username
        Picasso.with(viewHolder.itemView.username_user.context).load(user!!.profileImageUrl).into(viewHolder.itemView.profile_image_user)
    }

    override fun getLayout(): Int {
        return R.layout.user_search_item_layout
    }

}