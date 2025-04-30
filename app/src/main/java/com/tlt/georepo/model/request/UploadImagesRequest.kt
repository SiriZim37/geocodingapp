package com.tlt.georepo.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class UploadImagesRequest {
    @SerializedName("PSELECT")
    @Expose
    private var pSELECT: String? = null
    @SerializedName("PKEY1")
    @Expose
    private var pKEY1: String? = null
    @SerializedName("PKEY2")
    @Expose
    private var pKEY2: String? = null
    @SerializedName("PKEY3")
    @Expose
    private var pKEY3: String? = null
    @SerializedName("PKEY4")
    @Expose
    private var pKEY4: String? = null

    fun getPSELECT(): String? {
        return pSELECT
    }

    fun setPSELECT(pSELECT: String) {
        this.pSELECT = pSELECT
    }

    fun getPKEY1(): String? {
        return pKEY1
    }

    fun setPKEY1(pKEY1: String) {
        this.pKEY1 = pKEY1
    }

    fun getPKEY2(): String? {
        return pKEY2
    }

    fun setPKEY2(pKEY2: String) {
        this.pKEY2 = pKEY2
    }

    fun getPKEY3(): String? {
        return pKEY3
    }

    fun setPKEY3(pKEY3: String) {
        this.pKEY3 = pKEY3
    }

    fun getPKEY4(): String? {
        return pKEY4
    }

    fun setPKEY4(pKEY4: String) {
        this.pKEY4 = pKEY4
    }

    companion object {
        fun buildForImageProfile(base64: String = ""): UploadImagesRequest {
            return UploadImagesRequest().apply {
                pSELECT = "PROFILE"
                pKEY1 = "UPDATE"
                pKEY2 = ""
                pKEY3 = base64
                pKEY4 = ""
            }
        }
    }
}