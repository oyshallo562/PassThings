package com.degitalcon.passthings.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.degitalcon.passthings.DBKey
import com.degitalcon.passthings.R
import com.degitalcon.passthings.databinding.ActivityArticleBinding
import com.degitalcon.passthings.ui.chat.ChatListItem
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.json.JSONObject.NULL
import java.text.SimpleDateFormat
import java.util.*

class ArticleActivity : AppCompatActivity() {
    private lateinit var userDB: DatabaseReference
    private lateinit var binding: ActivityArticleBinding
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDB = Firebase.database.reference.child(DBKey.DB_USER)

        val sellerId = intent.getSerializableExtra("sellerId") as String
        val title = intent.getSerializableExtra("title") as String
        val price = intent.getSerializableExtra("price")
        val createdAt = intent.getSerializableExtra("createdAt")
        val imageUrl = intent.getSerializableExtra("imageUrl")
        val description = intent.getSerializableExtra("description")
        val tag = intent.getSerializableExtra("tag")

        val format = SimpleDateFormat("MM월 dd일")
        val date = Date(createdAt as Long)
        val time = format.format(date)

        if (imageUrl != NULL) {
            Glide.with(binding.ArticleImageView.context)
                .load(imageUrl)
                .into(binding.ArticleImageView)
        }

        binding.ArticleTitleTextView.text = title
        binding.ArticlePriceTextView.text = price.toString()
        binding.ArticleDateTextView.text = time.toString()
        binding.ArticleDescriptionTextView.text = description.toString()
        binding.ArticleTagTextView.text = tag.toString()

        binding.ArticleBuyButton.setOnClickListener {
            val chatRoom = ChatListItem(
                buyerId = auth.currentUser!!.uid,
                sellerId = sellerId,
                itemTitle = title,
                key = System.currentTimeMillis()
            )

            userDB.child(auth.currentUser!!.uid)
                .child(DBKey.CHILD_CHAT)
                .push()
                .setValue(chatRoom)

            userDB.child(sellerId)
                .child(DBKey.CHILD_CHAT)
                .push()
                .setValue(chatRoom)
            Toast.makeText(this,"채팅방이 생성되었습니다. 채팅탭에서 확인해주세요.",Toast.LENGTH_SHORT).show()
        }
    }
}