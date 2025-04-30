package com.tlt.georepo.modules.location.main.cluster

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.tlt.georepo.R

class TLTClusterRenderer(context: Context?,
                         map: GoogleMap?,
                         clusterManager: ClusterManager<TLTClusterItem>?)
    : DefaultClusterRenderer<TLTClusterItem>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: TLTClusterItem?, markerOptions: MarkerOptions?) {
        super.onBeforeClusterItemRendered(item, markerOptions)
        markerOptions?.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pincar))
    }
}