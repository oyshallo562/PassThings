package com.degitalcon.passthings.ui.notifications


import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.degitalcon.passthings.DBKey
import com.degitalcon.passthings.R
import com.degitalcon.passthings.databinding.FragmentNotificationsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.skt.Tmap.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch


//@Config(manifest = Config.NONE)
class NotificationsFragment : Fragment(R.layout.fragment_notifications) {
    private var _binding: FragmentNotificationsBinding? = null
    private var tMapView: TMapView? = null //티맵 함수 정의
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private var OpUid: String? = null
    private var Oplat: Double = 0.0
    private var Oplon: Double = 0.0

    private val binding get() = _binding!!

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val UserLocateDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DBKey.DB_USER_LOCATE).child(auth.uid.toString())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        UserLocateDB.child("name").get().addOnSuccessListener {
            OpUid = it.value.toString()
        }

        val linearLayoutTmap = root.findViewById(R.id.linearLayoutTmap) as LinearLayout
        tMapView = TMapView(context)
        tMapView!!.setSKTMapApiKey("l7xxb00f8d9a0a484559993bcb6887ffeee1")
        linearLayoutTmap.addView(tMapView)
        if(OpUid == null) {
            Handler(Looper.getMainLooper()).postDelayed({
                LocationHelper().startListeningUserLocation(
                    requireContext(),
                    object : LocationHelper.MyLocationListener {
                        override fun onLocationChanged(location: Location) {
                            lat = location.latitude
                            lon = location.longitude
                            val markerItem1 = TMapMarkerItem()
                            val tMapPoint1 = TMapPoint(lat, lon)
                            markerItem1.setPosition(0.5f, 1.0f) // 마커의 중심점을 중앙, 하단으로 설정
                            markerItem1.tMapPoint = tMapPoint1 // 마커의 좌표 지정
                            markerItem1.name = "내 위치" // 마커의 타이틀 지정
                            tMapView!!.addMarkerItem("markerItem1", markerItem1) // 지도에 마커 추가
                            UserLocateDB.child("latitude").setValue(lat)
                            UserLocateDB.child("longitude").setValue(lon)
                        }
                    })
            }, 1000)
        }
        else {
            val  OpDB = Firebase.database.reference.child(DBKey.DB_USER_LOCATE).child(OpUid.toString())
            OpDB.child("latitude").get().addOnSuccessListener {
                Oplat = it.value.toString().toDouble()
            }
            OpDB.child("longitude").get().addOnSuccessListener {
                Oplon = it.value.toString().toDouble()
            }

            Handler(Looper.getMainLooper()).postDelayed({
                LocationHelper().startListeningUserLocation(
                    requireContext(),
                    object : LocationHelper.MyLocationListener {
                        override fun onLocationChanged(location: Location) {
                            lat = location.latitude
                            lon = location.longitude
                            val markerItem1 = TMapMarkerItem()
                            val tMapPoint1 = TMapPoint(lat, lon)
                            markerItem1.setPosition(0.5f, 1.0f) // 마커의 중심점을 중앙, 하단으로 설정
                            markerItem1.tMapPoint = tMapPoint1 // 마커의 좌표 지정
                            markerItem1.name = "내 위치" // 마커의 타이틀 지정
                            tMapView!!.addMarkerItem("markerItem1", markerItem1) // 지도에 마커 추가
                            UserLocateDB.child("latitude").setValue(lat)
                            UserLocateDB.child("longitude").setValue(lon)

                            val markerItem2 = TMapMarkerItem()
                            val tMapPoint2 = TMapPoint(Oplat, Oplon)
                            markerItem2.setPosition(0.5f, 1.0f) // 마커의 중심점을 중앙, 하단으로 설정
                            markerItem2.tMapPoint = tMapPoint2 // 마커의 좌표 지정
                            markerItem2.name = "상대방 위치" // 마커의 타이틀 지정
                            tMapView!!.addMarkerItem("markerItem2", markerItem2) // 지도에 마커 추가
                        }
                    })
            }, 1000)
        }

        return root
    }
}


