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
import com.degitalcon.passthings.DBKey.Companion.DB_USER_LOCATE
import com.degitalcon.passthings.R
import com.degitalcon.passthings.ui.chat.ChatFragment
import com.degitalcon.passthings.ui.chat.ChatListItem
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
    private val UserDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DBKey.DB_USER).child(auth.uid.toString()).child("chat")
    }
    private val UserInfoDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DBKey.DB_USER_INFO).child(auth.uid.toString())
    }
    private var UserLocateDB: DatabaseReference? = null
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

       UserDB.get().addOnSuccessListener {
            it.children.forEach { it1 ->
                val model = it1.getValue(ChatListItem::class.java)
                if (model?.buyerId.toString() == auth.uid.toString()) {
                    UserLocateDB = Firebase.database.reference.child(DB_USER_LOCATE).child(model?.sellerId.toString())
                } else {
                    UserLocateDB = Firebase.database.reference.child(DB_USER_LOCATE).child(model?.buyerId.toString())
                }
            }
       }

        findViewById<Button>(R.id.sendButton).setOnClickListener {
            val chatItem = ChatItem(
                senderId = name.toString(),
                message = findViewById<EditText>(R.id.messageEditText).text.toString()
            )
            chatDB!!.push().setValue(chatItem)
        }

        findViewById<Button>(R.id.addLocateButton).setOnClickListener {
                val UserLocate = UserLocateEntity(
                    latitude = 0.0,
                    longitude = 0.0,
                    name = name.toString(),
                    uid = auth.uid.toString()
                )
                UserLocateDB!!.setValue(UserLocate)
            finish()
        }

    }
}