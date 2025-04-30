package com.tlt.georepo.modules.cancel

import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.app.TaskStackBuilder
import android.view.MenuItem

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sangcomz.fishbun.BaseActivity
import com.tlt.georepo.R

class JobCancelMapActivity : BaseActivity(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_activity_map)

        val mapFragment = getSupportFragmentManager().findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    protected override fun onResume() {
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val upIntent = NavUtils.getParentActivityIntent(this)

                if (NavUtils.shouldUpRecreateTask(this, upIntent!!)) {
                    TaskStackBuilder.create(this)
                        .addNextIntentWithParentStack(upIntent)
                        .startActivities()
                } else {
                    NavUtils.navigateUpTo(this, upIntent)
                }

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(map: GoogleMap) {
        val lat = getIntent().getDoubleExtra(EXTRA_LATITUDE, 0.0)
        val lng = getIntent().getDoubleExtra(EXTRA_LONGITUDE, 0.0)

        map.addMarker(MarkerOptions().position(LatLng(lat, lng)))

        val coords = LatLng(lat, lng)
        map.addMarker(MarkerOptions().position(coords))
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(coords, 10f)
        map.moveCamera(cameraUpdate)
    }

    companion object {

        val EXTRA_LATITUDE = "lat"
        val EXTRA_LONGITUDE = "lng"


    }


}
