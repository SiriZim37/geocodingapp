package com.tlt.georepo.modules.menu.job.request

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseActivity
import com.tlt.georepo.common.extension.ifFalse
import com.tlt.georepo.manager.BusManager
import com.tlt.georepo.manager.LocationManager
import com.tlt.georepo.modules.location.main.JobRequestLocationFragment
import com.tlt.georepo.modules.location.main.JobRequestLocationViewModel
import com.tlt.georepo.modules.main.MainMenuActivity
import com.tlt.georepo.modules.pincode.IsExitDialogFragment
import kotlinx.android.synthetic.main.map_activity_map.*
import com.google.android.gms.maps.SupportMapFragment


class JobRequestMapActivity : BaseActivity()  ,
//    , OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
//    GoogleApiClient.OnConnectionFailedListener ,
    IsExitDialogFragment.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(JobRequestMapViewModel::class.java)
    }
    private var mMap: GoogleMap? = null
    private lateinit var mLastLocation: Location
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mLocationRequest: LocationRequest

    private val defaultMenuPosition by lazy {
        intent.getIntExtra(MENU_POSITION_EXTRA, INSTALLMENT_MENU_POSITION)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_activity_map)

        initInstances()

    }

    @SuppressLint("ServiceCast")
    private fun initInstances() {
//        val mlocManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        val mapFragment = getSupportFragmentManager().findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)

        isSavedInstanceStateNotNull().ifFalse {
            loadMultipleRootFragment(content_container.id, defaultMenuPosition,
                findFragment(JobRequestLocationFragment::class.java)
                    ?: JobRequestLocationFragment.newInstance()
            )
        }

    }

    fun OnClickBacktoLastActivity(){
        try {
            MainMenuActivity.open(this)
        }catch (e : Exception){
            e.message
        }
    }

    override fun onBackPressedSupport() {
        OnClickBacktoLastActivity()
    }

    override fun onIsExitCancelClicked() {

    }

    override fun onIsExitConfirmClicked() {
        finishAffinity()
    }

    override fun onDestroy() {
        super.onDestroy()
        BusManager.unsubscribe(this)
    }

//    @SuppressLint("MissingPermission")
//    override fun onMapReady(googleMap: GoogleMap) {
//        try{
//            mMap = googleMap
//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (ContextCompat.checkSelfPermission(
//                        this,
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                    ) === PackageManager.PERMISSION_GRANTED
//                ) {
//                    buildGoogleApiClient()
//                    mMap!!.setMyLocationEnabled(true)
//                }
//            } else {
//                buildGoogleApiClient()
//                mMap!!.setMyLocationEnabled(true)
//            }
//        }catch (e : Exception ){
//            e.message
//        }
//    }
//
//
//    @Synchronized
//    protected fun buildGoogleApiClient() {
//        mGoogleApiClient = GoogleApiClient.Builder(this)
//            .addConnectionCallbacks(this)
//            .addOnConnectionFailedListener(this)
//            .addApi(LocationServices.API).build()
//        mGoogleApiClient!!.connect()
//    }
//
//    override fun onLocationChanged(location: Location) {
//
//        mLastLocation = location
//        //stop location updates
//        if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
//        }
//        viewModel.SetCurrentLocation(location.latitude.toString() , location.longitude.toString())
//
//    }
//    override fun onConnected(p0: Bundle?) {
//        mLocationRequest = LocationRequest()
//        mLocationRequest.setInterval(1000)
//        mLocationRequest.setFastestInterval(1000)
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) === PackageManager.PERMISSION_GRANTED
//        ) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
//        }
//    }
//
//    override fun onConnectionSuspended(i: Int) {
//    }
//
//    override fun onConnectionFailed(connectionResult: ConnectionResult) {
//    }
//


    companion object {
        const val INSTALLMENT_MENU_POSITION = 0
        const val DATA_POSITION_EXTRA = "DATA_POSITION_EXTRA"
        private const val MENU_POSITION_EXTRA = "MENU_POSITION_EXTRA"

        const val MAP_POSITION = 0

        fun Open(context: Context) {
            val intent = Intent(context, JobRequestMapActivity::class.java).apply {
                putExtra(MENU_POSITION_EXTRA, MAP_POSITION)
            }

            context.startActivity(intent)
        }

        fun startWithClearStack(context: Context?) {
            startWithClearStack(context, MAP_POSITION)
        }

        fun startWithClearStack(context: Context?, position: Int = MAP_POSITION) {
            startWithClearStack(context, position, Bundle())
        }

        fun startWithClearStack(context: Context?,
                                position: Int = MAP_POSITION,
                                data: Bundle) {
            val intent = Intent(context, JobRequestMapActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(MENU_POSITION_EXTRA, position)
                putExtra(DATA_POSITION_EXTRA, data)
            }
            context?.startActivity(intent)
        }

        fun startWithClearStackByDeeplink(context: Context?,
                                          position: Int = MAP_POSITION,
                                          data: Bundle) {
            val intent = Intent(context, JobRequestMapActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(MENU_POSITION_EXTRA, position)
                putExtra(DATA_POSITION_EXTRA, data)
            }
            context?.startActivity(intent)
        }
    }
}
