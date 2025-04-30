package com.tlt.georepo.model.entity.location

import io.realm.RealmObject

open class LocationDetailEntity(
    var  mainid: String = "",
    var  jobid: String = "",
    var  addressid: String = "",
    var  branch: String = "",
    var  contractno: String = "",
    var  customername: String  = "",
    var  idcard: String = "",
    var  installmentdueamt: String = "",
    var  lattitude: String = "",
    var  longtitude: String = "",
    var  profileid: String = "",
    var  realaddress: String = "",
    var  registerno: String  = "",
    var  regisprovince: String  = "",
    var  distance : String? = "" ,
    var  recordtype: String  = "",
    var  overdueitem: String  = "" ,
    var  overdueday: String = "",
    var  colordesc: String = "",
    var  modeldesc: String = "",
    var record_type : String = ""

) : RealmObject()