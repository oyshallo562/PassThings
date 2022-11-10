package com.degitalcon.passthings.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.degitalcon.passthings.DBKey.Companion.CHILD_CHAT
import com.degitalcon.passthings.DBKey.Companion.DB_ARTICLES
import com.degitalcon.passthings.DBKey.Companion.DB_USER
import com.degitalcon.passthings.R
import com.degitalcon.passthings.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment: Fragment(R.layout.fragment_home) {

    private lateinit var articleDB: DatabaseReference
    private lateinit var userDB: DatabaseReference
    private lateinit var articleAdapter: ArticleAdapter

    private val articleList = mutableListOf<ArticleModel>()
    private val listener = object: ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

            val articleModel = snapshot.getValue(ArticleModel::class.java)
            articleModel ?: return

            articleList.add(articleModel)
            articleAdapter.submitList(articleList)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onChildRemoved(snapshot: DataSnapshot) {}

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onCancelled(error: DatabaseError) {}

    }

    private var binding: FragmentHomeBinding? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentHomeBinding = FragmentHomeBinding.bind(view)
        binding = fragmentHomeBinding

        articleDB = Firebase.database.reference.child(DB_ARTICLES)
        userDB = Firebase.database.reference.child(DB_USER)
        articleAdapter = ArticleAdapter(onItemClicked = { articleModel ->
            if (auth.currentUser != null) {
                if (auth.currentUser?.uid != articleModel.sellerId) {
                    val intent = Intent(context, ArticleActivity::class.java)
                    intent.putExtra("sellerId", articleModel.sellerId)
                    intent.putExtra("title", articleModel.title)
                    intent.putExtra("price", articleModel.price)
                    intent.putExtra("createdAt", articleModel.createdAt)
                    intent.putExtra("imageUrl", articleModel.imageURL)
                    intent.putExtra("description", articleModel.description)
                    intent.putExtra("tag", articleModel.tag)
                    startActivity(intent)
                } else {
                    Snackbar.make(view, "내가 올린 아이템 입니다.", Snackbar.LENGTH_LONG).show()
                }
            } else {
                Snackbar.make(view, "로그인 후 사용해주세요.", Snackbar.LENGTH_LONG).show()
            }

        })
        articleList.clear()

        fragmentHomeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentHomeBinding.articleRecyclerView.adapter = articleAdapter

        fragmentHomeBinding.addFloatingButton.setOnClickListener {
            context?.let {

                if (auth.currentUser != null) {
                    val intent = Intent(it, AddArticleActivity::class.java)
                    startActivity(intent)
                } else {
                    Snackbar.make(view, "로그인 후 사용해주세요.", Snackbar.LENGTH_LONG).show()
                }

            }
        }

        articleDB.addChildEventListener(listener)

    }

    override fun onResume() {
        super.onResume()

        articleAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        articleDB.removeEventListener(listener)
    }
}