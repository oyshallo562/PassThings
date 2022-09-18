package com.degitalcon.passthings.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.degitalcon.passthings.R
import com.degitalcon.passthings.databinding.FragmentAddItemBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import android.widget.EditText as EditText

class AddItemFragment : Fragment() {

    private var binding: FragmentAddItemBinding? = null

    val IMAGE_PICK=1111

    var selectImage: Uri?=null

    lateinit var storage:FirebaseStorage
    lateinit var firestore: FirebaseFirestore
    lateinit var image:ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddItemBinding.inflate(layoutInflater, container, false)
        val v: View = binding!!.root

            storage= FirebaseStorage.getInstance()
            firestore=FirebaseFirestore.getInstance()

            val image=v.findViewById<ImageView>(R.id.imageView)
            val title=v.findViewById<EditText>(R.id.editText_title)
            val price=v.findViewById<EditText>(R.id.editText_price)
            val description=v.findViewById<EditText>(R.id.editText_description)

            val uploadBtn = v.findViewById<Button>(R.id.button_upload)

            image.setOnClickListener {
                var intent= Intent(Intent.ACTION_PICK) //선택하면 무언가를 띄움. 묵시적 호출
                intent.type="image/*"
                startActivityForResult(intent,IMAGE_PICK)
            }
            uploadBtn.setOnClickListener {
                if(selectImage!=null) {
                    var fileName =
                        SimpleDateFormat("yyyyMMddHHmmss").format(Date()) // 파일명이 겹치면 안되기 떄문에 시년월일분초 지정
                    storage.getReference().child("image").child(fileName)
                        .putFile(selectImage!!)//어디에 업로드할지 지정
                        .addOnSuccessListener {
                                taskSnapshot -> // 업로드 정보를 담는다
                            taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { it ->
                                var imageUrl = it.toString()
                                var id: Int = 0
                                firestore.collection("index").document("itemid").get()
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            id = it.result?.get("id").toString().toInt()
                                        }
                                    }
                                var item = Item(
                                    title.text.toString(),
                                    price.text.toString(),
                                    description.text.toString(),
                                    imageUrl,
                                    "1",
                                    id.toString()
                                )
                                id += 1
                                firestore.collection("item")
                                    .document(id.toString()).set(item)
                                //.addOnSuccessListener {
                                //}
                                firestore.collection("index")
                                    .document("itemid").set(id)
                                //.addOnSuccessListener {
                                //}
                                //원래 프레그먼트로 돌아가기
                            }
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.nav_host_fragment_activity_main, HomeFragment())
                                .commit()
                        }
                }
            }

        return v
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==IMAGE_PICK&&resultCode== Activity.RESULT_OK){
            selectImage=data?.data
            image.setImageURI(selectImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}