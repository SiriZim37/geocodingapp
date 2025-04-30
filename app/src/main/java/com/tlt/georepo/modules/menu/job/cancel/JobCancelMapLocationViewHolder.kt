package com.tlt.georepo.modules.menu.job.cancel
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import com.tlt.georepo.R

class JobCancelMapLocationViewHolder(private val mContext: Context, view: View) : RecyclerView.ViewHolder(view),
    OnMapReadyCallback {
    var title: TextView
    var description: TextView
    var name: TextView
    var status: TextView
    var datetime: TextView
    var locate: TextView
    var canceldetail: TextView
    protected var mGoogleMap: GoogleMap? = null
    var mapView: MapView
    protected var mJobCancelMapLocation: JobCancelMapLocation? = null



    init {

        title = view.findViewById(R.id.title) as TextView
        name = view.findViewById(R.id.description) as TextView
        description = view.findViewById(R.id.description2) as TextView
        locate = view.findViewById(R.id.locate) as TextView
        status = view.findViewById(R.id.title2) as TextView
        datetime = view.findViewById(R.id.datetime) as TextView
        name = view.findViewById(R.id.description) as TextView
        canceldetail = view.findViewById(R.id.cancelDetail) as TextView
        mapView = view.findViewById(R.id.map) as MapView

        mapView.onCreate(null)
        mapView.getMapAsync(this)
    }

    fun setMapLocation(jobCancelMapLocation: JobCancelMapLocation) {
        mJobCancelMapLocation = jobCancelMapLocation

        // If the map is ready, update its content.
        if (mGoogleMap != null) {
            updateMapContents()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        MapsInitializer.initialize(mContext)
        googleMap.uiSettings.isMapToolbarEnabled = false

        // If we have map data, update the map content.
        if (mJobCancelMapLocation != null) {
            updateMapContents()
        }
    }

    protected fun updateMapContents() {
        // Since the mapView is re-used, need to remove pre-existing mapView features.
        mGoogleMap!!.clear()

        // Update the mapView feature data and camera position.
        mGoogleMap!!.addMarker(MarkerOptions().position(mJobCancelMapLocation!!.center))

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(mJobCancelMapLocation!!.center, 10f)
        mGoogleMap!!.moveCamera(cameraUpdate)
    }
}
