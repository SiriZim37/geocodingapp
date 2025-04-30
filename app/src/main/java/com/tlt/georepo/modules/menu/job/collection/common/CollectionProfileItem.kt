package com.tlt.georepo.modules.menu.job.collection.common


data class CollectionProfileItem(
    var aDDRESSTYPE: String = "" ,
    var cUSTOMERNAME: String = "" ,
    var iDCARD: String = "" ,
    var iDPIC: String = "" ,
    var lATITUDE: String = "" ,
    var lONGITUDE: String = "" ,
    var pROFILEID: String = "" ,
    var rEALADDRESS: String = "" ,
    var rECORDTYPE: String = "" ,
    var tELHOME: String = "" ,
    var tELMOBILE: String = "",
    var gUARANTOR: List<CollectionProfileGuarantorItem>
)