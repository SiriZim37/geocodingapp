package com.tlt.georepo.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ResponseStatus {
    @SerializedName("WSStatus")
    @Expose
    private var wSStatus: String? = null
    @SerializedName("WSMsg")
    @Expose
    private var wSMsg: WSMsgGetBanner? = null

    fun getWSStatus(): String? {
        return wSStatus
    }

    fun setWSStatus(wSStatus: String) {
        this.wSStatus = wSStatus
    }

    fun getWSMsg(): WSMsgGetBanner? {
        return wSMsg
    }

    fun setWSMsg(wSMsg: WSMsgGetBanner) {
        this.wSMsg = wSMsg
    }
}