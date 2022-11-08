package com.degitalcon.passthings.ui.notifications


import android.Manifest
import android.content.Context
import android.graphics.Color
import android.location.Location
import android.location.LocationRequest
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.degitalcon.passthings.R
import com.degitalcon.passthings.databinding.FragmentNotificationsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationResult.create
import com.skt.Tmap.*
import com.skt.Tmap.TMapGpsManager.onLocationChangedCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.junit.Before

//@Config(manifest = Config.NONE)
class NotificationsFragment : Fragment(R.layout.fragment_notifications), onLocationChangedCallback {
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

        gps = TMapGpsManager(context) // 생성자 함수 정의

       // gps!!.minTime = 1000 //현재 위치를 찾을 최소 시간 (밀리초)
       /// gps!!.minDistance = 5f //현재 위치를 갱신할 최소 거리
        gps!!.provider = TMapGpsManager.GPS_PROVIDER //위치 제공자 설정
        gps!!.OpenGps() //네트워크 위치 탐색 허용


        //setlocationthread() //쓰레드를 통해 현재 위치 탐색을 하는 함수 실행


        return root
    }

    private fun setlocationthread() {
        gps!!.provider = TMapGpsManager.GPS_PROVIDER //현재위치 찾을 방법(휴대폰 gps)
        gps!!.setMinTime(500);
        //gps!!.setMinDistance(5);
        gps!!.OpenGps() //gps 위치 탐색 허용
        gpsLocationthread() //쓰레드를 실행해 위치 탐색
        //ThreadActivity.WaitDlg.stop(dlg) //쓰레드 완료시 대화상자 닫음
        if (lat == 0.0 || lon == 0.0) { //위치를 불러왔는지 확인
           // Toast.makeText(context, "위치를 불러오지 못했습니다. 다시한번 실행해주세요", Toast.LENGTH_SHORT).show()
            Log.d("위치를 불러오지 못했습니다. 다시한번 실행해주세요", "위치를 불러오지 못했습니다. 다시한번 실행해주세요")
        } else tMapView!!.setCenterPoint(lat!!, lon!!)
        gps!!.CloseGps()
    }

    private fun gpsLocationthread()
    {//위치탐색 쓰레드
        //위치탐색 쓰레드
        for (i in 0..9) {
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                setlocationthread()
            }
        }
    }
    override fun onLocationChange(location: Location) {
        lat = location.latitude
        lon = location.longitude
        if (lat == 0.0 || lon == 0.0) { //위치를 불러왔는지 확인
            // Toast.makeText(context, "위치를 불러오지 못했습니다. 다시한번 실행해주세요", Toast.LENGTH_SHORT).show()
            Log.d("위치를 불러오지 못했습니다. 다시한번 실행해주세요", "위치를 불러오지 못했습니다. 다시한번 실행해주세요")
        } else {

            Log.d("lat!!!!!", lat.toString())
            Log.d("lon!!!!!", lon.toString())

            tMapView!!.setCenterPoint(lat!!, lon!!)
            tMapView!!.setZoomLevel(15)

            var tpoint = TMapPoint(lat, lon)
            var tItem = TMapMarkerItem()
            tItem.setTMapPoint(tpoint)
            tItem.setVisible(TMapMarkerItem.VISIBLE)

            //Bitmap bitmap = itmapFactory.decodeResource(context.getResources(),R.drawable.Icon);
            //tItem.setIcon(bitmap);
            // 핀모양으로 된 마커를 사용할 경우 마커 중심을 하단 핀 끝으로 설정.
            //tItem.setPosition(0.5 1.0)
            tMapView!!.addMarkerItem("1", tItem)

        }



        // gps!!.CloseGps()
    }
}