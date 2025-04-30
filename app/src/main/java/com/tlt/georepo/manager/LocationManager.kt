package com.tlt.georepo.manager

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.tlt.georepo.common.exception.PermissionException
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.model.entity.location.LocationDetailEntity
import com.tlt.georepo.model.entity.location.LocationFilterEntity
import com.tlt.georepo.util.PermissionUtils
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object LocationManager {

    private val databaseManager = DatabaseManager.getInstance()

    fun saveLocationFilter(
        dealerName: String = "",
        provinceIndex: Int = -1,
        amphurIndex: Int = -1,
        isOfficeChecked: Boolean = false,
        isShowRoomChecked: Boolean = false,
        isServiceCenterChecked: Boolean = false,
        isRepairServiceChecked: Boolean = false,
        isNearly: Boolean = false
    ) {
        val entity = LocationFilterEntity(
            dealerName,
            provinceIndex,
            amphurIndex,
            isOfficeChecked,
            isShowRoomChecked,
            isServiceCenterChecked,
            isRepairServiceChecked,
            isNearly
        )

        databaseManager.save(LocationFilterEntity::class.java, listOf(entity))
    }

    fun getLocationFilter() = databaseManager.findAllBy(LocationFilterEntity::class.java)?.firstOrNull()

    fun clearLocationFilter() {
        databaseManager.deleteBy(LocationFilterEntity::class.java)
    }

    fun saveLocationDetail(locationDetail: LocationDetailEntity) {
        databaseManager.deleteBy(LocationDetailEntity::class.java)
        databaseManager.save(LocationDetailEntity::class.java, listOf(locationDetail))
    }

    fun getLocationDetail() = databaseManager.findAllBy(LocationDetailEntity::class.java)?.firstOrNull()

    @SuppressLint("MissingPermission")
    suspend fun getLastKnowLocation() = suspendCoroutine<Location> { continuation ->
        val context = ContextManager.getInstance().getApplicationContext()

        if (!PermissionUtils.isGrantedLocation()) {
            continuation.resumeWithException(PermissionException("Permission ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION not grant."))
            return@suspendCoroutine
        }

        LocationServices.getFusedLocationProviderClient(context)
            .lastLocation
            .addOnSuccessListener { location ->
                if (location == null) {
                    val locationDefault = Location("GPS_Provider")
                    try {
                        locationDefault.latitude  =  DatabaseManager.getInstance().getCurrent().latitude.toDouble()
                        locationDefault.longitude =  DatabaseManager.getInstance().getCurrent().longitude.toDouble()
                    }catch (e : Exception){
                        locationDefault.latitude = 13.7292258
                        locationDefault.longitude = 100.5411709
                    }
                    continuation.resume(locationDefault)
                } else {
                    location.let {
                        continuation.resume(it)
                    }
                }
            }
    }
}