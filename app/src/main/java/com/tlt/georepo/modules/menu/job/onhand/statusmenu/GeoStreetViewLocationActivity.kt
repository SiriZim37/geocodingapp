package com.tlt.georepo.modules.menu.job.onhand.statusmenu

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.TextView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.tlt.georepo.R
import kotlinx.android.synthetic.main.fragment_geo_streetview.*
import kotlinx.android.synthetic.main.widget_toolbar.view.*
import java.text.DecimalFormat
import java.util.*


class GeoStreetViewLocationActivity : FragmentActivity(), StreetViewPanorama.OnStreetViewPanoramaChangeListener{

    lateinit var mStreetViewPanorama: StreetViewPanorama
    private var dLat: Double? = null
    private var dLng: Double? = null
    private var c_province: String? = null
    private lateinit var desAddr  : LatLng
    private lateinit var staAddr  : LatLng
    private lateinit var StrDesAddr  : String
    private lateinit var StrCurrentAddr  : String
    lateinit var btmtxtAddressName  : TextView
    lateinit var btmLat : TextView
    lateinit var btmLng: TextView


    private val s_Location by lazy {
        intent.getStringExtra("DES_LOCATION") ?: ""
    }

    private val sLat by lazy {
        intent.getStringExtra("DES_LAT") ?: ""
    }
    private val sLng by lazy {
        intent.getStringExtra("DES_LNG") ?: ""
    }

    private val mainID by lazy {
        intent.getStringExtra("MAIN_ID") ?: ""
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_geo_streetview)

        initInstances()
        initViewModel()
    }

    fun initInstances() {
        try{
            val streetViewFragment = supportFragmentManager.findFragmentById(R.id.streetviewpanorama) as SupportStreetViewPanoramaFragment

            btmtxtAddressName = (findViewById(R.id.txtAddressName) as TextView)
            btmLat = findViewById(R.id.txtPositiondetail1) as TextView
            btmLng = findViewById(R.id.txtPositiondetail2) as TextView

            streetViewFragment.getStreetViewPanoramaAsync { streetViewPanorama ->
                streetViewPanorama.setPosition(LatLng(sLat.toDouble(), sLng.toDouble()))
                mStreetViewPanorama = streetViewPanorama
                mStreetViewPanorama.getLocation()
                mStreetViewPanorama.setOnStreetViewPanoramaChangeListener(
                    this@GeoStreetViewLocationActivity
                )
            }

            toolbar.widget_toolbar_navigation.setOnClickListener{
                OnClickBacktoLastActivity()
            }
        }catch (e : Exception){
            Log.e("geo street instance : " , e.stackTrace.toString())
        }
    }

    fun initViewModel(){
        try{
        StrCurrentAddr =  s_Location

        btnAccept.setOnClickListener {
            WorkDoneStatusActivity.OpenWithLocation(this@GeoStreetViewLocationActivity ,
                StrDesAddr ,
                desAddr.latitude.toString() ,
                desAddr.longitude.toString(),
                mainID )
        }
        }catch (e : Exception){
            Log.e("geo street view : " , e.stackTrace.toString())
        }
    }

    override fun onStreetViewPanoramaChange(location: StreetViewPanoramaLocation?) {
        try {
            if (location != null) {
                dLat = location.position.latitude
                dLng = location.position.longitude
                desAddr = LatLng(dLat!!,dLng!!)
                val geoCoder = Geocoder(baseContext, Locale.getDefault())
                val addresses = geoCoder.getFromLocation(dLat!!, dLng!!, 1)
                 StrDesAddr = addresses[0].getAddressLine(0) //0 to obtain first possible address
                val city = addresses[0].locality
                val state = addresses[0].adminArea
                val country = addresses[0].countryName
                val postalCode = addresses[0].postalCode
                //create your custom title
                c_province = country
                val formatter = DecimalFormat("#,###.00")
                val lat = formatter.format(dLat!!)
                val lng = formatter.format(dLng!!)
                val strLatlng = String.format("Lat: %s°, Long: %s°", lat, lng)
                btmtxtAddressName.setText(StrDesAddr)
                btmLat.setText(String.format("%s°" ,  String.format("%.6f", dLat)))
                btmLng.setText(String.format("%s°" ,  String.format("%.6f", dLng)))

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun onBackPressed(){

    }

    fun OnClickBacktoLastActivity(){
        try {
            GeoLocationActivity.Open(this@GeoStreetViewLocationActivity , mainID , s_Location , sLat , sLng)

        }catch (e : Exception){
            e.message
        }
    }

    companion object {

        val MAIN_ID = ""
        fun Open(  context: Context?  , curr_location : String  , lat : String ,lng : String   , mainID : String ) {
            val intent = Intent(context, GeoStreetViewLocationActivity::class.java)
            intent.putExtra("DES_LOCATION" , curr_location)
            intent.putExtra("DES_LAT" , lat)
            intent.putExtra("DES_LNG" , lng)
            intent.putExtra("MAIN_ID" , mainID)
            context?.startActivity(intent)
        }
    }
}