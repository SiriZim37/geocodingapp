package com.tlt.georepo.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ItemNotifyPushResponse(
        @SerializedName("title")
        @Expose
        var title: String? = "Title is empty",
        @SerializedName("job_id")
        @Expose
        var jobId: String? = "",
        @SerializedName("flag_chkbox")
        @Expose
        var flagChkbox: String? = "N",
        @SerializedName("exp_DateTime")
        @Expose
        var expDateTime: String? = "",
        @SerializedName("msg_type")
        @Expose
        var msgType: String? = "",
        @SerializedName("dateTime")
        @Expose
        var dateTime: String? = "",
        @SerializedName("msg")
        @Expose
        var msg: String? = "Message is empty",
        @SerializedName("image")
        @Expose
        var image: String? = "",
        @SerializedName("media")
        @Expose
        var media: String? = "",
        @SerializedName("ic_color")
        @Expose
        var iconColor: String? = "R",
        @SerializedName("navigation")
        @Expose
        var navigation: String? = ""
)