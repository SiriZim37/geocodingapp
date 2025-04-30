package com.tlt.georepo.modules.menu.job.onhand


import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.tlt.georepo.R
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.tlt.georepo.common.extension.setDrawableTop
import com.tlt.georepo.manager.geomap.GeoDataParser
import com.tlt.georepo.modules.main.MainMenuActivity
import com.tlt.georepo.modules.menu.job.onhand.common.OnHandMainItem
import com.tlt.georepo.modules.menu.job.onhand.statusmenu.WorkDoneStatusActivity
import com.tlt.georepo.modules.menu.job.onhand.topmenu.SubDetailMainActivity
import kotlinx.android.synthetic.main.bottom_sheet_onhand_map.*
import kotlinx.android.synthetic.main.bottom_sheet_onhand_map_btnsheet.*
import kotlinx.android.synthetic.main.fragment_dialog_alert_temp.view.*
import kotlinx.android.synthetic.main.fragment_dialog_alert_temp.view.btn_yes
import kotlinx.android.synthetic.main.fragment_dialog_alert_temp.view.txt_status
import kotlinx.android.synthetic.main.fragment_dialog_sts_succes.view.*
import kotlinx.android.synthetic.main.fragment_location_with_btnsheet.*
import kotlinx.android.synthetic.main.widget_toolbar.view.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList


class JobOnHandBtnSheetActivity : FragmentActivity(), OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener{


    private var mMap: GoogleMap? = null
    private lateinit var mLastLocation: Location
    private var mCurrLocationMarker: Marker? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mLocationRequest: LocationRequest

    internal lateinit var MarkerPoints: ArrayList<LatLng>
    private lateinit var desAddr  : LatLng
    private lateinit var staAddr  : LatLng
    private var job_onhand_list : List<OnHandMainItem>? = null
    private var CRRENT_LAT  : String = ""
    private var CRRENT_LNG : String = ""


    private val viewModel by lazy {
        ViewModelProviders.of(this).get(JobOnHandBtnSheetViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_location_with_btnsheet)


        initInstances()
        initViewModel()


    }


    fun initInstances() {

        toolbar.widget_toolbar_navigation.setOnClickListener{
            OnClickBacktoLastActivity()
        }


        // ---  Default Menu  --- //
        val bottomSheetBehavior = BottomSheetBehavior.from(map_bottom_sheet_onhand_bottom_view)
        // change the state of the bottom sheet
        txtHeadContent.text = getString(R.string.job_hide_Menu)
        txtHeadContent.setDrawableTop(R.drawable.arrow_down_1)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.isHideable = false
        viewModel.GetCurrentLocation()

    }

    fun initViewModel(){


        btnNavigator.setOnClickListener {
            var  latlngDes= desAddr
            var latlngStart = staAddr
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr="+ latlngStart.latitude + "," + latlngStart.longitude + "&daddr=" + latlngDes.latitude + "," + latlngDes.longitude));
            intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
            startActivity(intent);

        }

        btn_sts_arrive.setOnClickListener {
            try {
                viewModel.GetCurrentLocationByProcess("arrive")
            }catch (e : Exception){
                e.message
            }
        }

        btn_sts_finish.setOnClickListener {
            try {
                viewModel.GetCurrentLocationByProcess("sendjob")
            }catch (e : Exception){
                e.message
            }
        }

        btn_sts_cancel.setOnClickListener {
            try {
                viewModel.GetCurrentLocationByProcess("cancel")
            }catch (e : Exception){
                e.message

            }
        }

        btn_sts_hold.setOnClickListener {
            try {
                viewModel.GetCurrentLocationByProcess("hold")
            }catch (e : Exception){
                e.message
            }
        }

        btnCustomerDetail.setOnClickListener {
            SubDetailMainActivity.start(this , this!!.job_onhand_list!![0].mAINID)
            return@setOnClickListener
        }


        // set callback for changes
        txtHeadContent.setOnClickListener {
            val bottomSheetBehavior = BottomSheetBehavior.from(map_bottom_sheet_onhand_bottom_view)

            if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_COLLAPSED) {
                txtHeadContent.text = getString(R.string.job_hide_Menu)
                txtHeadContent.setDrawableTop(R.drawable.arrow_down_1)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                txtHeadContent.text = getString(R.string.job_show_Menu)
                txtHeadContent.setDrawableTop(R.drawable.arrow_up_1)
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        viewModel.whenDataLoadedLocationSuccess.observe(this, Observer {
            it?.let {
                CRRENT_LAT = it.latitude.toString()
                CRRENT_LNG = it.longitude.toString()
                viewModel.getData(CRRENT_LAT, CRRENT_LNG)
                staAddr = LatLng(CRRENT_LAT.toDouble() , CRRENT_LNG.toDouble())
            }

        })


        viewModel.whenLoading.observe(this, Observer {
//            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDataLoadedFailure.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })


        viewModel.whenDataLoadedSuccess.observe(this, Observer {
            it?.let {
                //                (it)
                if (it.jobMainOnHandItem.count() > 0) {
                    job_onhand_list = it.jobMainOnHandItem
                    setupDataIntoViews(it.jobMainOnHandItem[0])

                }
            }

        })

        viewModel.whenDataLoadedNoData.observe(this, Observer {

            showPopupNoJobDialog()

        })

        viewModel.whenOvertime.observe(this , Observer {
            showOvertimeDialog()
        })

        viewModel.whenLoadDataCurrentLocation_arrive.observe(this, Observer {
            it?.let {
                CRRENT_LAT = it.latitude.toString()
                CRRENT_LNG = it.longitude.toString()
                try {
                    JobArriveDialogFragment.show(
                        fragmentManager = supportFragmentManager,
                        mainID = this!!.job_onhand_list!![0].mAINID ,
                        lat = CRRENT_LAT,
                        lng = CRRENT_LNG
                    )

                }catch (e : Exception){
                    e.message
                }
            }
        })
        viewModel.whenLoadDataCurrentLocation_sendjob.observe(this, Observer {
            it?.let {
                CRRENT_LAT = it.latitude.toString()
                CRRENT_LNG = it.longitude.toString()
                try {
                    val mainId = this!!.job_onhand_list!![0].mAINID
                    val dataAddres = this!!.job_onhand_list!![0].rEALADDRESS
                    val dataLat = this!!.job_onhand_list!![0].lATITUDE
                    val dataLng = this!!.job_onhand_list!![0].lONGITUDE
                    WorkDoneStatusActivity.start(this , mainId , dataAddres , dataLat ,dataLng )
                }catch (e : Exception){
                    e.message
                }
            }

        })
        viewModel.whenLoadDataCurrentLocation_hold.observe(this, Observer {
            it?.let {
                CRRENT_LAT  = it.latitude.toString()
                CRRENT_LNG  = it.longitude.toString()
                try {
                    JobHoldDialogFragment.show(
                        fragmentManager = supportFragmentManager,
                        mainID = this!!.job_onhand_list!![0].mAINID ,
                        lat = CRRENT_LAT,
                        lng = CRRENT_LNG
                    )
                }catch (e : Exception){
                    e.message
                }
            }
        })
        viewModel.whenLoadDataCurrentLocation_cancel.observe(this, Observer {
            it?.let {
                CRRENT_LAT  =it.latitude.toString()
                CRRENT_LNG  =it.longitude.toString()
                try {
                    JobCancelDialogFragment.show(
                        fragmentManager = supportFragmentManager,
                        mainID = this!!.job_onhand_list!![0].mAINID ,
                        lat = CRRENT_LAT,
                        lng = CRRENT_LNG
                    )
                }catch (e : Exception){
                e.message
                }
            }
        })


        viewModel.whenDataLoadedDirection.observe(this, Observer {
            setupDataRouteIntiView(it!!)
        })
    }


    private fun showPopupNoJobDialog() {
        try{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.fragment_dialog_alert_temp, null)
            mDialogView.txt_status.text = getString(R.string.dia_req_status_please_add_job)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)

            val  mAlertDialog = mBuilder.show()
            mDialogView.btn_yes.setOnClickListener {
                mAlertDialog.dismiss()
                /* GOTO ADD MAIN */
                MainMenuActivity.open(this)
            }
        }catch (e : Exception){
            e.message
        }

    }

    fun setupDataRouteIntiView(jsonData : String){
        try{
            var distance = ""
            var duration = ""
            val result =  jsonData.substring(1, jsonData.length)
            val jsonObj = JSONObject(result)
            val parser = GeoDataParser()
            // Starts parsing data
            var routes: List<List<HashMap<String, String>>>  = parser.parse(jsonObj)

            var points: ArrayList<LatLng>
            var lineOptions: PolylineOptions? = null

            // Traversing through all the routes
            for (i in routes.indices) {
                points = ArrayList<LatLng>()
                lineOptions = PolylineOptions()

                // Fetching i-th route
                val path = routes[i]
                // Fetching all the points in i-th route
                for (j in path.indices) {
                    val point = path[j]

                    val lat = java.lang.Double.parseDouble(point["lat"])
                    val lng = java.lang.Double.parseDouble(point["lng"])
                     distance = point["distance"].toString()
                     duration = point["duration"].toString()
                    val position = LatLng(lat, lng)
                    points.add(position)
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points)
                lineOptions.width(10f)
                lineOptions.color(Color.BLUE)

                btnEstTime.text = duration
                btnEstDistance.text = distance

                Log.d("onPostExecute", "onPostExecute lineoptions decoded")
            }
            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap!!.addPolyline(lineOptions)
            } else {
                Log.d("onPostExecute", "without Polylines drawn")
            }

        } catch (e: Exception) {
            Log.d("ParserTask", e.toString())
            e.printStackTrace()
        }
    }




    fun setupDataIntoViews(jobMainOnHandItem : OnHandMainItem){
        try {
            val mapFragment = getSupportFragmentManager().findFragmentById(R.id.map) as SupportMapFragment
            desAddr = LatLng( jobMainOnHandItem.lATITUDE.toDouble()  ,jobMainOnHandItem.lONGITUDE.toDouble() )
            mapFragment.getMapAsync(this)
            MarkerPoints = ArrayList<LatLng>()

        }catch (e : Exception){
            e.message
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val thailand = LatLng(13.7563, 100.5018)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(thailand))
        mMap!!.getUiSettings().setZoomGesturesEnabled(true);
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


        if (MarkerPoints.size > 1) {
            MarkerPoints.clear()
            mMap!!.clear()
        }

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

        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }
        //Place current location marker
        val latLng = LatLng(location.getLatitude(), location.getLongitude())
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        staAddr = latLng
        markerOptions.title("Current Position")
//        desAddr = LatLng(13.683380, 100.495960)
        val destinationDetal =  getGeoAddress(desAddr)
        mMap!!.addMarker(MarkerOptions().position(desAddr).title(destinationDetal))
        MarkerPoints.add(staAddr)
        MarkerPoints.add(desAddr)

        if (MarkerPoints.size >= 2) {
            val origin = MarkerPoints[0]
            val dest = MarkerPoints[1]

            // Getting URL to the Google Directions API
//            val url = getUrl(origin, dest)
//            Log.d("onMapClick", url.toString())

            viewModel.getDirection( lat = CRRENT_LAT ,
                lng = CRRENT_LNG ,
                desLat = desAddr.latitude.toString() ,
                desLng = desAddr.longitude.toString())

//            val FetchUrl = FetchUrl()
            // Start downloading json data from Google Directions API
//            FetchUrl.execute(url)
            //move map camera
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(dest))
            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(12F))
        }
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
        }

        try{
            viewModel.SetCurrentLocation(location.latitude.toString() , location.longitude.toString())
        }catch (e : Exception){
            e.message
        }

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
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


    override fun onBackPressed(){
        OnClickBacktoLastActivity()
    }

    fun OnClickBacktoLastActivity(){
        try {
            MainMenuActivity.open(this)
        }catch (e : Exception){
            e.message
        }
    }

    private fun showOvertimeDialog() {
        try{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.fragment_dialog_alert_temp, null)
            mDialogView.txt_status.text = getString(R.string.txt_time)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
            val  mAlertDialog = mBuilder.show()
            mDialogView.btn_yes.setOnClickListener {
                mAlertDialog.dismiss()
                /* GO TO MAIN */
                MainMenuActivity.open(this)
            }
        }catch (e : Exception){
            e.message
        }
    }
    companion object {

        fun Open(context: Context? ) {
            val intent = Intent(context, JobOnHandBtnSheetActivity::class.java)
            context?.startActivity(intent)
        }
    }



}