package com.degitalcon.passthings.ui.chat.chatdetail

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.degitalcon.passthings.DBKey
import com.degitalcon.passthings.DBKey.Companion.DB_CHATS
import com.degitalcon.passthings.R
import com.degitalcon.passthings.ui.chat.ChatFragment
import com.degitalcon.passthings.ui.notifications.NotificationsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ChatRoomActivity: AppCompatActivity() {

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val UserInfoDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DBKey.DB_USER_INFO).child(auth.uid.toString())
    }
    private val UserLocateDB: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private var chatDB: DatabaseReference? = null

    private val chatList = mutableListOf<ChatItem>()
    private val adapter = ChatItemAdapter()
    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatroom)

        UserInfoDB.child("name").get().addOnSuccessListener {
            name = it.value.toString()
            if(name == null) {
                name = auth.uid.toString()
            }
        }

        val chatKey = intent.getLongExtra("chatKey", -1)
        Log.d("ChatRoomActivity", "chatKey = $chatKey")

        findViewById<RecyclerView>(R.id.chatRecyclerView).adapter = adapter
        findViewById<RecyclerView>(R.id.chatRecyclerView).layoutManager = LinearLayoutManager(this)

        chatDB = Firebase.database.reference.child(DB_CHATS).child("$chatKey")
        chatDB!!.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatItem = snapshot.getValue(ChatItem::class.java)
                chatItem ?: return
                Log.d("ChatRoomActivity", "${chatItem.message}, ${chatItem.senderId}" )
                chatList.add(chatItem)
                adapter.submitList(chatList)
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}

        })



        findViewById<Button>(R.id.sendButton).setOnClickListener {

            val chatItem = ChatItem(
                senderId = name.toString(),
                message = findViewById<EditText>(R.id.messageEditText).text.toString()
            )

            chatDB!!.push().setValue(chatItem)

        }

        findViewById<Button>(R.id.addLocateButton).setOnClickListener {

            finish()
        }

    }
}