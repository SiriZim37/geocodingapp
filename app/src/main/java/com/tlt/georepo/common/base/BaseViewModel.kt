package com.tlt.georepo.common.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.tlt.georepo.manager.LocationManager
import com.tlt.georepo.manager.api.GeoApiManager
import com.tlt.georepo.model.request.SetDataLocationRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    val whenLoading = MutableLiveData<Boolean>()
    val whenNoInternetConnection = MutableLiveData<Boolean>()
    val whenDataFailure = MutableLiveData<String>()

    fun SetCurrentLocation() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val location = LocationManager.getLastKnowLocation()
                val request = SetDataLocationRequest.build(
                    lat = location.latitude.toString(),
                    lng = location.longitude.toString()
                )
                GeoApiManager.getInstance().setDataLocation(request) { isError, result ->

                    if (isError) {
                        return@setDataLocation
                    }
                }
            } catch (error: Exception) {
                error.printStackTrace()
            }
        }
    }


}