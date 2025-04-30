package com.tlt.georepo.model.request
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SendOTPRequest {
    @SerializedName("PSELECT")
    @Expose
    var pselect: String? = null
    @SerializedName("PKEY1")
    @Expose
    var pkeY1: String? = null
    @SerializedName("PKEY2")
    @Expose
    var pkeY2: String? = null
    @SerializedName("PKEY3")
    @Expose
    var pkeY3: String? = null
    @SerializedName("PKEY4")
    @Expose
    var pkeY4: String? = null
    @SerializedName("PKEY5")
    @Expose
    var pkeY5: String? = null
    @SerializedName("SIGN")
    @Expose
    var sign: String? = null


    companion object {
        fun build(phone : String  ): SendOTPRequest {
            return SendOTPRequest().apply {
               pselect  = "FORGOT"
                pkeY1 = phone
                pkeY2 = ""
                pkeY3 = ""
                pkeY4 = ""
                pkeY5 = ""
                sign = ""

            }
        }
    }
}