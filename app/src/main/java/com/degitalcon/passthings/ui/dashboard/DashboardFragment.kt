package com.degitalcon.passthings.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.degitalcon.passthings.DBKey
import com.degitalcon.passthings.R
import com.degitalcon.passthings.databinding.FragmentDashboardBinding
import com.degitalcon.passthings.ui.home.AddArticleActivity
import com.degitalcon.passthings.ui.home.ArticleModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DashboardFragment : Fragment() {

    //private val articleList = mutableListOf<UserInfoModel>()
    private var _binding: FragmentDashboardBinding? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val UserInfoDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DBKey.DB_USER_INFO).child(auth.uid.toString())
    }
    private var name :String ?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        UserInfoDB.child("name").get().addOnSuccessListener {
            name = it.value.toString()
            root.findViewById<TextView>(R.id.Nickname_textView).text = name
        }

        UserInfoDB.child("profileImage").get().addOnSuccessListener {
            val imageURL = it.value.toString()
            if (name != null) {
                Glide.with(binding.imageView.context)
                    .load(imageURL)
                    .into(binding.imageView)
            }

            root.findViewById<TextView>(R.id.Nickname_textView).text = name
        }

        root.findViewById<ImageButton>(R.id.Edit_Profile_Button).setOnClickListener {
            if (auth.currentUser != null) {
                val intent = Intent(context, AddUserinfoActivity::class.java)
                startActivity(intent)
            } else {
                Snackbar.make(requireView(), "로그인 후 사용해주세요.", Snackbar.LENGTH_LONG).show()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}