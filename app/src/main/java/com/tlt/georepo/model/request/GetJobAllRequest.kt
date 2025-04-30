package com.tlt.georepo.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


 class GetJobAllRequest {


     @SerializedName("PSELECT")
        @Expose
        var pselect: String = ""
        @SerializedName("PSUCCESS")
        @Expose
        var psuccess: String = ""

        companion object {

            fun build(): GetJobAllRequest {
                return GetJobAllRequest()
            }
        }
}