package com.degitalcon.passthings.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.degitalcon.passthings.DBKey.Companion.CHILD_CHAT
import com.degitalcon.passthings.DBKey.Companion.DB_CHATS
import com.degitalcon.passthings.DBKey.Companion.DB_USER
import com.degitalcon.passthings.R
import com.degitalcon.passthings.databinding.FragmentChatBinding
import com.degitalcon.passthings.ui.chat.chatdetail.ChatRoomActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatFragment: Fragment(R.layout.fragment_chat) {

    private var binding: FragmentChatBinding? = null
    private lateinit var chatListAdapter: ChatListAdapter
    private val chatRoomList = mutableListOf<ChatListItem>()

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private var ChatsDB: DatabaseReference?= null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentChatBinding = FragmentChatBinding.bind(view)
        binding = fragmentChatBinding

        chatListAdapter = ChatListAdapter(onItemClicked = { chatRoom ->
            context?.let {
                ChatsDB = Firebase.database.reference.child(DB_CHATS).child(chatRoom.key.toString())
                val itemId = ChatsDB!!.key
                val intent = Intent(it, ChatRoomActivity::class.java)

                intent.putExtra("chatKey", chatRoom.key)
                intent.putExtra("ItemId", itemId)
                startActivity(intent)
            }
        })

        chatRoomList.clear()

        fragmentChatBinding.chatRecyclerView.adapter = chatListAdapter
        fragmentChatBinding.chatRecyclerView.layoutManager = LinearLayoutManager(context)


        if (auth.currentUser == null) {
            return
        }

        val chatDB = Firebase.database.reference.child(DB_USER).child(auth.currentUser!!.uid).child(
            CHILD_CHAT)
        chatDB.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val model = it.getValue(ChatListItem::class.java)
                    model ?: return

                    chatRoomList.add(model)
                }

                chatListAdapter.submitList(chatRoomList)
                chatListAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    override fun onResume() {
        super.onResume()

        chatListAdapter.notifyDataSetChanged()
    }
}