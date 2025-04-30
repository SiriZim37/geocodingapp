package com.tlt.georepo.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetDealerShowroomResponse(
    @SerializedName("DealerCode")
    @Expose
    var dealerCode: String,
    @SerializedName("Latitude")
    @Expose
    var latitude: String,
    @SerializedName("Longtitude")
    @Expose
    var longtitude: String,
    @SerializedName("ShowroomCode")
    @Expose
    var showroomCode: String,
    @SerializedName("ShowroomName")
    @Expose
    var showroomName: String
)