package com.tlt.georepo.model.entity

import com.google.android.gms.maps.model.LatLng

open class JobEntity  {
   var jobNo : String
   var custDes: LatLng
   var cusName : String
   var cusAddr : String
   var mobileNo : String
    var regNo : String
   var contractNo :String
    var unpaidAmt : String
    var jobStatus : String
    var datetime : String


    constructor(jobNo: String ,
                cusName: String,
                lat: Double,
                lng: Double ,
                cusAddr: String ,
                mobileNo:String ,
                regNo: String ,
                contractNo:String ,
                unpaidAmt: String ,
                status: String ,
                datetime: String  ) {
        this.jobNo = jobNo
        this.custDes = LatLng(lat, lng)
        this.cusName = cusName
        this.cusAddr = cusAddr
        this.datetime = datetime
        this.mobileNo = mobileNo
        this.regNo = regNo
        this.contractNo = contractNo
        this.unpaidAmt = unpaidAmt
        this.jobStatus = status
        this.datetime = datetime

    }
//
//    fun getCompanyName() : String? {
//        return if (LocalizeManager.isThai()) {
//            cOMPANYTH
//        } else {
//            cOMPANYEN
//        }
//    }
//
//    fun getAddress() : String? {
//        return if (LocalizeManager.isThai()) {
//            cOMPANYADDRESSTH
//        } else {
//            cOMPANYADDRESSEN
//        }
//    }
//
//    fun getResponsibility() : String? {
//        return if (LocalizeManager.isThai()) {
//            sCOPEOFRESPONSIBILITYTH
//        } else {
//            sCOPEOFRESPONSIBILITYEN
//        }
//    }
}