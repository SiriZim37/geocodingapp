package com.tlt.georepo.modules.menu.job.onhand.statusmenu

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.tlt.georepo.R
import kotlinx.android.synthetic.main.fragment_geo_maplocation.*
import kotlinx.android.synthetic.main.widget_toolbar.view.*
import java.io.IOException
import java.util.*


class GeoLocationActivity : FragmentActivity(), OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener{

    private var mMap: GoogleMap? = null
    private lateinit var mLastLocation: Location
    private var mCurrLocationMarker: Marker? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mLocationRequest: LocationRequest
    internal lateinit var MarkerPoints: ArrayList<LatLng>
    private lateinit var desAddr  : LatLng
    private lateinit var staAddr  : LatLng
    private lateinit var StrDesAddr  : String
    private lateinit var StrCurrentAddr  : String
     lateinit var btmtxtAddressName  : TextView
    lateinit var btmLat : TextView
    lateinit var btmLng: TextView

    private val mainID by lazy {
        intent.getStringExtra("MAIN_ID") ?: ""
    }

    private val dataLatitude by lazy {
        intent.getStringExtra("DATA_LAT") ?: ""
    }

    private val dataLongtitude by lazy {
        intent.getStringExtra("DATA_LNG") ?: ""
    }

    private val dataAddress by lazy {
        intent.getStringExtra("DATA_ADDRESS") ?: ""
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_geo_maplocation)

        initInstances()
        initViewModel()
    }



    fun initInstances() {

        val mapFragment = getSupportFragmentManager().findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
        MarkerPoints = ArrayList<LatLng>()

        btmtxtAddressName = (findViewById(R.id.txtAddressName) as TextView)
        btmLat = findViewById(R.id.txtAddressPositiondetail1) as TextView
        btmLng = findViewById(R.id.txtAddressPositiondetail2) as TextView

        toolbar.widget_toolbar_navigation.setOnClickListener{
            OnClickBacktoLastActivity()
        }

    }

    fun initViewModel(){

        btnAccept.setOnClickListener {
            WorkDoneStatusActivity.OpenWithLocation(this@GeoLocationActivity ,
                                                            StrDesAddr ,
                                                            desAddr.latitude.toString() ,
                                                            desAddr.longitude.toString(),
                                                            mainID )
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val thailand = LatLng(13.7563, 100.5018)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(thailand))
        mMap!!.uiSettings.isMapToolbarEnabled
        mMap!!.uiSettings.isZoomGesturesEnabled
        mMap!!.uiSettings.isZoomControlsEnabled

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) === PackageManager.PERMISSION_GRANTED
            ) {
                buildGoogleApiClient()
                mMap!!.setMyLocationEnabled(true)
            }
        } else {
            buildGoogleApiClient()
            mMap!!.setMyLocationEnabled(true)
        }

        mMap!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(p0: LatLng?) {
                try {
                    mMap!!.clear()
                    desAddr = p0!!
                    StrDesAddr = getGeoAddress(desAddr)
                    mMap!!.addMarker(
                        MarkerOptions()
                            .position(desAddr)
                            .title(StrDesAddr)
                    )
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLng(desAddr))
                    mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15F))
                    btmtxtAddressName.setText(StrDesAddr)
                    btmLat.setText(String.format("%s°", String.format("%.6f", desAddr.latitude)))
                    btmLng.setText(String.format("%s°", String.format("%.6f", desAddr.longitude)))
                } catch (e: Exception) {
                    e.message

                }
            }
        })

        search_edt.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                mMap!!.clear()
                val  destination = search_edt.text.toString()
                desAddr = geGeotLatLng(destination)
                // Find real location name
                StrDesAddr =  getGeoAddress(desAddr)
                mMap!!.addMarker(MarkerOptions().position(desAddr).title(StrDesAddr))
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(desAddr))
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15F))
                btmtxtAddressName.setText(StrDesAddr)
                btmLat.setText(String.format("%s°" , String.format("%.6f", desAddr.latitude )))
                btmLng.setText(String.format("%s°" ,  String.format("%.6f", desAddr.longitude)))
                return@OnKeyListener true
            }
            false
        })


        mMap!!.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View {
                val v = layoutInflater.inflate(R.layout.custom_marker_option, null)
                val tv_title = v.findViewById(R.id.tv_title) as TextView
                val position = v.findViewById(R.id.position) as TextView
                val position2 = v.findViewById(R.id.position2) as TextView
                try{
                    tv_title.setText(marker.getTitle().toString())
                    position.setText(desAddr.latitude.toString())
                    position2.setText(desAddr.longitude.toString())
                }catch (e : Exception){
                    Log.e("MapInfo : " ,e.printStackTrace().toString() )
                }

                return v
            }

            override fun getInfoContents(marker: Marker): View? {
                return null
            }
        })

        btnStreetView.setOnClickListener{
            try {
                if(desAddr.latitude != 0.0 && desAddr.longitude != 0.0 ){

                    GeoStreetViewLocationActivity.Open( this@GeoLocationActivity ,
                        StrDesAddr ,
                        desAddr.latitude.toString() ,
                        desAddr.longitude.toString(),
                        mainID
                        )
                }

            }catch (e: Exception){
                Log.e("geo-street" , e.printStackTrace().toString())
            }

        }

    }

    private fun getGeoAddress(latLng: LatLng): String {
        // 1
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        try {
            // 2
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            // 3
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses[0]
                addressText = address.getAddressLine(0)
                val country =  address.countryName
                val postalCode =  address.postalCode
                val state = address.adminArea
                val city =  address.locality
                val knownName = address.featureName
                address.subLocality
                address.thoroughfare
                address.subThoroughfare

            }
        } catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
        }

        return addressText
    }


    private fun geGeotLatLng(strAddress : String) : LatLng {
        // 1
        val geocoder = Geocoder(this , Locale.getDefault())
        val geocodeMatches: List<Address>?
        var latlng : LatLng? = null

        try {
            geocodeMatches = geocoder.getFromLocationName(strAddress , 1)
            val latitude = geocodeMatches[0].latitude
            val longitude = geocodeMatches[0].longitude
            latlng = LatLng(latitude,longitude)
        }catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
        }

        return latlng!!
    }


    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }

    override fun onConnected(p0: Bundle?) {

        mLocationRequest = LocationRequest()
        mLocationRequest.setInterval(1000)
        mLocationRequest.setFastestInterval(1000)
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) === PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
        }

    }


    override fun onConnectionSuspended(i: Int) {
    }

    override fun onLocationChanged(location: Location) {
    try{
        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }
        //Place current location marker
        val latLng = LatLng(location.getLatitude(), location.getLongitude())
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        staAddr = latLng
        desAddr = LatLng(dataLatitude.toDouble() , dataLongtitude.toDouble())
        StrCurrentAddr = getGeoAddress(desAddr)
        StrDesAddr =  dataAddress
        mMap!!.addMarker(MarkerOptions()
            .position(desAddr)
            .title(dataAddress))
        //move map camera
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(desAddr))
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15F))
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
        }
        btmtxtAddressName.setText(StrDesAddr)
        btmLat.setText(String.format("%s°" ,  String.format("%.6f", desAddr.latitude )))
        btmLng.setText(String.format("%s°" ,  String.format("%.6f", desAddr.longitude)))
    }catch(e : Exception) {

    }

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
    }


    override fun onBackPressed(){

    }

    fun OnClickBacktoLastActivity(){
        try {
            WorkDoneStatusActivity.start(this , mainID , dataAddress , dataLatitude ,dataLongtitude )
        }catch (e : Exception){
            e.message
        }
    }


    companion object {
        fun Open(context: Context? , mainID : String , address  : String , lat : String , lng : String  ) {
            val intent = Intent(context, GeoLocationActivity::class.java)
            intent.putExtra("MAIN_ID", mainID)
            intent.putExtra("DATA_LAT", lat)
            intent.putExtra("DATA_LNG", lng)
            intent.putExtra("DATA_ADDRESS", address)
            context?.startActivity(intent)
        }
    }
}