package com.tlt.georepo.manager.api

import android.util.Log
import com.tlt.georepo.common.eventbus.DeviceLogonEvent
import com.tlt.georepo.common.eventbus.NoInternetEvent
import com.tlt.georepo.common.eventbus.ServerUnAvailableEvent
import com.tlt.georepo.common.eventbus.SocketTimeoutEvent
import com.tlt.georepo.manager.BusManager
import com.tlt.georepo.manager.JsonMapperManager
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.manager.db.UserManager
import com.tlt.georepo.model.response.GeoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class GeoApiCallback<String> : Callback<String> {

    abstract fun onSuccess(jsonResult: kotlin.String)
    abstract fun onFailure(message: kotlin.String, isWorkable: Boolean = true)

    override fun onResponse(call: Call<String>?, response: Response<String>?) {
//        Log.e("GeoApiCallback " ,response.)
        if (response?.isSuccessful == false) {
            if (response.code() == 503) {
                onFailure(CASE_SERVER_UNAVAILABLE)
                sendEventToBaseActivity(ServerUnAvailableEvent())
            } else {
                onFailure(call, Exception(response.message()))
            }
            return
        }
        Log.e("GeoApiCallback ", response.toString())

        val tltResponse = JsonMapperManager.getInstance()
            .gson.fromJson(response?.body() as kotlin.String, GeoResponse::class.java)

        if (tltResponse.isDeviceLogon()) {
            onFailure(tltResponse.wsMsg.msgStatus)
            sendEventToBaseActivity(DeviceLogonEvent())
            return
        }

        if (tltResponse.isAccessDenied()) {
            onFailure(tltResponse.wsMsg.msgStatus)
            return
        }

        if (!tltResponse.isSuccess()) {
            onFailure(tltResponse.wsMsg.msgStatus)
            return
        }

        if (tltResponse.isOvertime()) {
            DatabaseManager.getInstance().setOverTime(true)
            onFailure(tltResponse.wsMsg.msgStatus)
            return
        }

        UserManager.getInstance().saveAccessToken(tltResponse.wsMsg.token)
        onSuccess(tltResponse.getResult())
    }

    override fun onFailure(call: Call<String>?, t: Throwable?) {
        if (isSocketTimeout(call, t)) {
            sendEventToBaseActivity(SocketTimeoutEvent())
            return
        }

        if (t is UnknownHostException) {
            onFailure(CASE_NO_INTERNET)
            sendEventNoInternetToBaseActivity(NoInternetEvent())
            return
        }

        onFailure(t?.message ?: "")
    }

    private fun isSocketTimeout(call: Call<String>?, t: Throwable?): Boolean {
        if (t !is SocketTimeoutException) {
            return false
        }

        call?.clone()?.enqueue(this)

        return true
    }

    private fun sendEventNoInternetToBaseActivity(event: Any) {
        BusManager.observe(event)
    }

    private fun sendEventToBaseActivity(event: Any) {
        BusManager.observe(event)
    }

    companion object {
        const val CASE_NO_INTERNET = "noInternet"
        const val CASE_SERVER_UNAVAILABLE = "CASE_SERVER_UNAVAILABLE"
    }

}