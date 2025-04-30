package com.tlt.georepo.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class WSMsgGetBanner {
    @SerializedName("Msgstatus")
    @Expose
    private var msgstatus: String? = null
    @SerializedName("Result")
    @Expose
    private var result: List<ResultsGetBanner>? = null
    @SerializedName("Rslc")
    @Expose
    private var rslc: String? = null

    fun getMsgstatus(): String? {
        return msgstatus
    }

    fun setMsgstatus(msgstatus: String) {
        this.msgstatus = msgstatus
    }

    fun getResult(): List<ResultsGetBanner>? {
        return result
    }

    fun setResult(result: List<ResultsGetBanner>) {
        this.result = result
    }

    fun getRslc(): String? {
        return rslc
    }

    fun setRslc(rslc: String) {
        this.rslc = rslc
    }
}