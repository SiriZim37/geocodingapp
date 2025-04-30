package com.tlt.georepo.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ResultsGetBanner{
    @SerializedName("BANNER_NAME")
    @Expose
    private var bANNERNAME: String? = null
    @SerializedName("BANNER_IMG")
    @Expose
    private var bANNERIMG: String? = null
    @SerializedName("BANNER_TYPE")
    @Expose
    private var bANNERTYPE: String? = null
    @SerializedName("BANNER_INDEX")
    @Expose
    private var bANNERINDEX: Int? = null
    @SerializedName("BANNER_DESC_1")
    @Expose
    private var bANNERDESC1: String? = null
    @SerializedName("BANNER_DESC_2")
    @Expose
    private var bANNERDESC2: String? = null

    @SerializedName("PHONE")
    @Expose
    private var PHONE: String? = null

    fun getPHONE(): String? {
        return PHONE
    }

    fun setBANNERNAME(bANNERNAME: String) {
        this.bANNERNAME = bANNERNAME
    }

    fun getBANNERIMG(): String? {
        return bANNERIMG
    }

    fun setBANNERIMG(bANNERIMG: String) {
        this.bANNERIMG = bANNERIMG
    }

    fun getBANNERTYPE(): String? {
        return bANNERTYPE
    }

    fun setBANNERTYPE(bANNERTYPE: String) {
        this.bANNERTYPE = bANNERTYPE
    }

    fun getBANNERINDEX(): Int? {
        return bANNERINDEX
    }

    fun setBANNERINDEX(bANNERINDEX: Int?) {
        this.bANNERINDEX = bANNERINDEX
    }

    fun getBANNERDESC1(): String? {
        return bANNERDESC1
    }

    fun setBANNERDESC1(bANNERDESC1: String) {
        this.bANNERDESC1 = bANNERDESC1
    }

    fun getBANNERDESC2(): String? {
        return bANNERDESC2
    }

    fun setBANNERDESC2(bANNERDESC2: String) {
        this.bANNERDESC2 = bANNERDESC2
    }
}