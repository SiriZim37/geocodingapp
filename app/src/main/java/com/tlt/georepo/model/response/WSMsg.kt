package com.tlt.georepo.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WSMsg {
    @SerializedName("Msgstatus")
    @Expose
    private var msgstatus: String? = null
    @SerializedName("Result")
    @Expose
    private var result: String? = null
    @SerializedName("Rslc")
    @Expose
    private var rslc: String? = null

    fun getMsgstatus(): String? {
        return msgstatus
    }

    fun setMsgstatus(msgstatus: String) {
        this.msgstatus = msgstatus
    }

    fun getResult(): String? {
        return result
    }

    fun setResult(result: String) {
        this.result = result
    }

    fun getRslc(): String? {
        return rslc
    }

    fun setRslc(rslc: String) {
        this.rslc = rslc
    }

}