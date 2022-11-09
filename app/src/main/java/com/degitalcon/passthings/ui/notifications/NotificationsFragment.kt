package com.degitalcon.passthings.ui.notifications


import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.degitalcon.passthings.R
import com.degitalcon.passthings.databinding.FragmentNotificationsBinding
import com.skt.Tmap.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch


//@Config(manifest = Config.NONE)
class NotificationsFragment : Fragment(R.layout.fragment_notifications) {
    private var _binding: FragmentNotificationsBinding? = null
    var tMapView: TMapView? = null //티맵 함수 정의
    var gps: TMapGpsManager? = null //티맵 gps 매니저 변수 정의
    var lat: Double = 0.0
    var lon: Double = 0.0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val linearLayoutTmap = root.findViewById(R.id.linearLayoutTmap) as LinearLayout
        tMapView = TMapView(context)
        tMapView!!.setSKTMapApiKey("l7xxb00f8d9a0a484559993bcb6887ffeee1")
        linearLayoutTmap.addView(tMapView)

            Handler(Looper.getMainLooper()).postDelayed({
                LocationHelper().startListeningUserLocation(
                    requireContext(),
                    object : LocationHelper.MyLocationListener {
                        override fun onLocationChanged(location: Location) {
                            Log.d("Location", "" + location.latitude + "," + location.longitude)
                            //tMapView!!.removeAllMarkerItem()
                            lat = location.latitude
                            lon = location.longitude
                            val markerItem1 = TMapMarkerItem()
                            val tMapPoint1 = TMapPoint(lat, lon)
                            markerItem1.setPosition(0.5f, 1.0f) // 마커의 중심점을 중앙, 하단으로 설정
                            markerItem1.tMapPoint = tMapPoint1 // 마커의 좌표 지정
                            markerItem1.name = "내 위치" // 마커의 타이틀 지정
                            tMapView!!.addMarkerItem("markerItem1", markerItem1) // 지도에 마커 추가
                            //tMapView!!.setCenterPoint(lat, lon)
                            //tMapView!!.setZoomLevel(15)
                        }
                    })
            }, 1000)


        return root
    }
}


