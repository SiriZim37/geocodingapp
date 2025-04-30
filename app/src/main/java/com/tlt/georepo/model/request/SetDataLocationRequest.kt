package com.tlt.georepo.model.request

import com.google.gson.annotations.SerializedName

 class SetDataLocationRequest {
     @SerializedName("PKEY1")
     var pKEY1: String = ""
     @SerializedName("PKEY2")
     var pKEY2: String = ""
     @SerializedName("PKEY3")
     var pKEY3: String = ""
     @SerializedName("PLAT")
     var pLAT: String = ""
     @SerializedName("PLONG")
     var pLONG: String = ""
     companion object {
         fun build(lat : String ,
                   lng : String ): SetDataLocationRequest {
             return SetDataLocationRequest().apply {
                 pLAT = lat
                 pLONG = lng
             }
         }
     }
 }