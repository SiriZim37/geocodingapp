package com.tlt.georepo.modules.location.main.cluster

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class TLTClusterItem(lat: Double,
                     lng: Double,
                     title: String,
                     snippet: String,
                     index: Int) : ClusterItem {
    private var mPosition: LatLng = LatLng(lat, lng)
    private var mTitle = title
    private var mSnippet = snippet
    private var mIndex = index

    fun getIndex() = mIndex
    override fun getSnippet() = mSnippet
    override fun getTitle() = mTitle
    override fun getPosition() = mPosition
}