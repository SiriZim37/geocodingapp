package com.tlt.georepo.modules.menu.job.cancel

import com.google.android.gms.maps.model.LatLng

class JobCancelMapLocation {
    lateinit var id_no: String
    lateinit var name: String
    lateinit var locate: String
    lateinit var center: LatLng
    lateinit var datetime: String
    lateinit var status: String
    lateinit var canceldetail: String


    constructor() {}

    constructor(id_no: String , name: String, lat: Double, lng: Double ,  locate: String , status:String , datetime : String , canceldetail : String ) {
        this.name = name
        this.center = LatLng(lat, lng)
        this.id_no = id_no
        this.locate = locate
        this.datetime = datetime
        this.status = status
        this.canceldetail = canceldetail
    }
}
