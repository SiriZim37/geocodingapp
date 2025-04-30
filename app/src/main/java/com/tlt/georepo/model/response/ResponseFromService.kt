package com.tlt.georepo.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseFromService {

    @SerializedName("WSStatus")
    @Expose
    private var wSStatus: String? = null
    @SerializedName("WSMsg")
    @Expose
    private var wSMsg: WSMsg? = null

    fun getWSStatus(): String? {
        return wSStatus
    }

    fun setWSStatus(wSStatus: String) {
        this.wSStatus = wSStatus
    }

    fun getWSMsg(): WSMsg? {
        return wSMsg
    }

    fun setWSMsg(wSMsg: WSMsg) {
        this.wSMsg = wSMsg
    }

}