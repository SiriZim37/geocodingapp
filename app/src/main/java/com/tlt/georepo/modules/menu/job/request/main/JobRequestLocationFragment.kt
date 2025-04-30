package com.tlt.georepo.modules.location.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.bottom_sheet_location_map_job_all.*
import kotlinx.android.synthetic.main.fragment_location_job_all.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseFragment
import com.tlt.georepo.common.dialog.NormalDialogFragment
import com.tlt.georepo.common.extension.gone
import com.tlt.georepo.common.extension.ifTrue
import com.tlt.georepo.common.extension.setDrawableEnd
import com.tlt.georepo.common.extension.visible
import com.tlt.georepo.manager.LocationManager
import com.tlt.georepo.modules.location.main.cluster.TLTClusterItem
import com.tlt.georepo.modules.main.MainMenuActivity
import com.tlt.georepo.modules.menu.job.request.detail.subrequest.RequestMainDetailActivity
import com.tlt.georepo.util.ExternalAppUtils
import kotlinx.android.synthetic.main.fragment_dialog_sts_succes.view.*
import kotlinx.android.synthetic.main.widget_toolbar.view.*

class JobRequestLocationFragment : BaseFragment(), OnMapReadyCallback, NormalDialogFragment.Listener {

    var LAT_CUR : String = ""
    var LNG_CUR : String = ""

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(JobRequestLocationViewModel::class.java)
    }

    private lateinit var googleMap: GoogleMap

    private val markers = ArrayList<Marker>()
    private val defaultLatLng = LatLng(15.8700, 100.9925)
    private var currentType = JobRequestLocationViewModel.Type.CUSTHOUSE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_location_job_all, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initViewModel()
        initInstances()

//        viewModel.getDataJobAll(LAT_CUR , LAT_CUR)

    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        checkLocationIsOn()
        updateMap()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            viewModel.getData(JobRequestLocationViewModel.Type.CUSTHOUSE)
        }
    }

    private fun initViewModel() {

        viewModel.whenLoading.observe(this, Observer {
        })

        viewModel.whenDataLoadedNoData.observe(this, Observer {

            showPopupNoJobDialog()

        })
        viewModel.whenDataLoadedJobDataAll.observe(this, Observer {
            setupDataIntoViews(it!!)
        })

        viewModel.whenDataLoadedSuccess.observe(this, Observer {

        })

        viewModel.whenOvertime.observe(this , Observer {
            showOvertimeDialog()
        })


    }

    private fun initInstances() {

        toolbar.setTitle(activity!!.getString(R.string.repoAllJob))

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        toolbar.hideTextRightMenu()
        filterHide(true)


        toolbar.widget_toolbar_navigation.setOnClickListener{
            OnClickBacktoLastActivity()
        }

        location_map_bottom_sheet_rv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = JobRequestLocationAdapter(
                    items = arrayListOf(),
                    listener = onCustLocationListener
            )
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        location_map_all_tv.setOnClickListener {
            val bottomSheetBehavior = BottomSheetBehavior.from(layout_map_bottom_sheet_view_all)

            if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_COLLAPSED) {
                map_bottom_sheet_bg.visible()
                location_map_all_tv.setDrawableEnd(R.drawable.ic_arrow_down)
                location_map_all_tv.text = activity!!.getString(R.string.location_hide)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                filterHide(true)
            } else {
                map_bottom_sheet_bg.gone()
                location_map_all_tv.setDrawableEnd(R.drawable.ic_arrow_up)
                location_map_all_tv.text = activity!!.getString(R.string.location_show_all)
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

    }

    private fun showPopupNoJobDialog() {
        try{
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.fragment_dialog_alert_temp, null)
            mDialogView.txt_status.text = getString(R.string.dia_req_status_not_found_data)
            val mBuilder = AlertDialog.Builder(this!!.context!!).setView(mDialogView)
            val  mAlertDialog = mBuilder.show()
            mDialogView.btn_yes.setOnClickListener {
                mAlertDialog.dismiss()
                /* GO TO MAIN */
                MainMenuActivity.open(context!!)
            }
        }catch (e : Exception){
            e.message
        }
    }

    private fun showOvertimeDialog() {
        try{
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.fragment_dialog_alert_temp, null)
            mDialogView.txt_status.text = getString(R.string.txt_time)
            val mBuilder = AlertDialog.Builder(this!!.context!!).setView(mDialogView)
            val  mAlertDialog = mBuilder.show()
            mDialogView.btn_yes.setOnClickListener {
                mAlertDialog.dismiss()
                /* GO TO MAIN */
                MainMenuActivity.open(context!!)
            }
        }catch (e : Exception){
            e.message
        }
    }




    private fun setupDataIntoViews(modelJobRequest: JobRequestLocationViewModel.JobModel) {
        try{
            txtNodata.gone()
            if (modelJobRequest?.jobLocationList?.isEmpty() == true) {
                txtNodata.visible()
//            group_normal.gone()
//            group_not_found.visible()
                return
            }

            group_normal.visible()
            group_not_found.gone()

            val items = modelJobRequest?.jobLocationList ?: listOf()
            val adapter = location_map_bottom_sheet_rv.adapter as JobRequestLocationAdapter

            moveCamera(LatLng(items[0].lATITUDE!!.toDouble(), items[0].lONGITUDE!!.toDouble()))

            adapter.update(items)
            addMarker(items)

            val title =  getString(R.string.total_job_assign)
            currentType = JobRequestLocationViewModel.Type.CUSTHOUSE
            location_map_company_tv.text = title + " ( " +  items.size.toString() + " )"
        }catch(e : Exception) {
            Log.e("setupDataIntoViews" ,e.message.toString() )
        }

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {

        this.googleMap = googleMap!!
        val thailand = LatLng(13.7563, 100.5018)
        this.googleMap .moveCamera(CameraUpdateFactory.newLatLng(thailand))
        this.googleMap.setOnMarkerClickListener(onMarkerClickListener)
    }


    private fun updateMap() {
        viewModel.whenDataLoaded.value?.let { return }

        checkPermission(success = {
            googleMap.isMyLocationEnabled = true
            googleMap.setOnMyLocationButtonClickListener {
                moveCameraToCurrentLocation()
                true
            }
            moveCameraToCurrentLocation()
            viewModel.getData(JobRequestLocationViewModel.Type.CUSTHOUSE)
        }, failure = {
            moveCamera(defaultLatLng)
            viewModel.getData(JobRequestLocationViewModel.Type.CUSTHOUSE)
        })
    }

    private val onCustLocationListener = object : JobRequestLocationAdapter.Listener {
        override fun onDetailClick(index: Int, item: JobRequestLocationViewModel.JobPullCollection) {
            viewModel.saveLocationDetail(item)
            RequestMainDetailActivity.start(context , item.mAINID)
        }

        override fun onDirectionClick(index: Int, item: JobRequestLocationViewModel.JobPullCollection) {
            val lat = item.lATITUDE!!.toDouble()
            val lng = item.lONGITUDE!!.toDouble()

            ExternalAppUtils.openGoogleDirection(context, lat, lng)
        }
    }

    private val onMarkerClickListener = GoogleMap.OnMarkerClickListener {

        moveCamera(it.position)
        val index = it.tag as Int
        viewModel.onLocationClick(index)
        true
    }

    private val onClusterItemClickListener = ClusterManager.OnClusterItemClickListener<TLTClusterItem> { item ->
        moveCamera(item!!.position)
        val index = item.getIndex()
        viewModel.onLocationClick(index)
        true
    }

    private fun moveCameraToCurrentLocation() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val location = LocationManager.getLastKnowLocation()
                val latLng = LatLng(location.latitude, location.longitude)
                LAT_CUR = location.latitude.toString()
                LNG_CUR = location.longitude.toString()
                viewModel.getDataJobAll(LAT_CUR , LNG_CUR)
//                moveCamera(latLng)
            } catch (error: Exception) {
                error.printStackTrace()
            }
        }
    }

    private fun moveCamera(latLng: LatLng) {
        GlobalScope.launch(Dispatchers.Main) {
            if (currentType == JobRequestLocationViewModel.Type.CUSTHOUSE) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            }
        }
    }

    private fun selectedMarker(position: Int): Bitmap {
        val normalWidth = 40
        val normalHeight = 63
        val selectedWidth = 80
        val selectedHeight = 126

        val bitmapDrawable = resources.getDrawable(R.drawable.geocoding_pin) as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        return if (position == 0) {
            Bitmap.createScaledBitmap(bitmap, selectedWidth, selectedHeight, false)
        } else {
            Bitmap.createScaledBitmap(bitmap, normalWidth, normalHeight, false)
        }
    }

    private fun addMarker(jobRequestLocationList: List<JobRequestLocationViewModel.JobPullCollection>) {
        GlobalScope.launch(Dispatchers.Main) {
            clearMarkers()

            jobRequestLocationList.forEachIndexed { index, location ->
                val latLng = LatLng(location.lATITUDE!!.toDouble(), location.lONGITUDE!!.toDouble())

                val marker = googleMap.addMarker(MarkerOptions()
                        .position(latLng)
                        .title(location.rEALADDRESS)
                        .icon(BitmapDescriptorFactory.fromBitmap(selectedMarker(index))))

                marker.tag = index

                markers.add(marker)

            }

        }
    }

    private fun clearMarkers() {
        markers.forEach { it.remove() }
        markers.clear()

//        googleMap.clear()
//        clusterManager.clearItems()
    }

    @SuppressLint("ServiceCast")
    private fun checkLocationIsOn() {
        val locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        var gps_enabled = false
        var network_enabled = false

        try {
            gps_enabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
        } catch (exception: Exception) {

        }

        try {
            network_enabled = locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
        } catch (exception: Exception) {

        }

        if (!gps_enabled && !network_enabled) {
            NormalDialogFragment.show(
                    fragmentManager = fragmentManager,
                    fragment = this,
                    description = getString(R.string.location_switch_on_location_title),
                    confirmButtonMessage = getString(R.string.dialog_button_ok),
                    cancelButtonMessage = getString(R.string.callcenter_dialog_btn_cancel)
            )
        }
    }

    override fun onDialogConfirmClick() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        activity!!.startActivity(intent)
    }

    override fun onDialogCancelClick() {
    }

    private fun checkPermission(success: () -> Unit, failure: () -> Unit) {
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            success.invoke()
                            return
                        }

                        failure.invoke()
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }


    private fun filterHide(isCollapsed: Boolean) {
        isCollapsed.ifTrue {
            val bottomSheetBehavior = BottomSheetBehavior.from(layout_map_bottom_sheet_view_all)

            if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
                map_bottom_sheet_bg.gone()
                location_map_all_tv.setDrawableEnd(R.drawable.ic_arrow_up)
                location_map_all_tv.text = activity!!.getString(R.string.location_show_all)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    fun OnClickBacktoLastActivity(){
        try {
            MainMenuActivity.open(context!!)
        }catch (e : Exception){
            e.message
        }
    }


    companion object {

        const val IS_FROM_CONTRACT_DETAIL = "is_from_contract_detail"
        const val OFFICE_LOCATION_MENU_POSITION = 0
        fun newInstance() = JobRequestLocationFragment()

    }
}