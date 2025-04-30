package com.tlt.georepo.modules.menu.location.detail

import android.arch.lifecycle.MutableLiveData
import com.tlt.georepo.common.base.BaseViewModel
import com.tlt.georepo.manager.LocationManager
import com.tlt.georepo.modules.location.main.JobRequestLocationViewModel

class LocationDetailViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()

    fun getData() {
        val item = LocationManager.getLocationDetail()

//        val model = Model(
//            jobNo = item!!.jobNo ?: "",
//            cusName = item!!.cusName ?:"",
//            lat = item!!.lat ?:"",
//            lng = item!!.lng ?:"",
//            cusAddr = item!!.cusAddr ?: "",
//            mobileNo =item!!.mobileNo ?: "",
//            regNo = item!!.regNo ?: "",
//            contractNo = item!!.contractNo ?: "",
//            unpaidAmt = item!!.unpaidAmt ?: "",
//            jobStatus = item!!.jobStatus?:"",
//            distance = item!!.distance?:"",
//            datetime = item!!.datetime ?: ""
//        )
        val model = Model(
            aDDRESSID = item!!.addressid ?: "",
            bRANCH = item!!.branch ?: "",
            cONTRACTNO = item!!.contractno ?: "",
            cUSTOMERNAME =  item!!.customername ?: "",
            iDCARD = item!!.idcard ?: "",
            iNSTALLMENTDUEAMT = item!!.installmentdueamt ?: "",
            jOBID = item!!.jobid ?: "",
            lONGITUDE = item!!.longtitude ?: "",
            mAINID = item!!.mainid ?: "",
            pROFILEID = item!!.profileid ?: "",
            rEALADDRESS = item!!.realaddress ?: "",
            rEGISTERNO = item!!.registerno ?: "",
            rEGISTERPROVINCE = item!!.regisprovince ?: "",
            distance = item!!.distance ?: ""
        )

        whenDataLoaded.postValue(model)
    }

    data class Model(
        val  aDDRESSID: String = "",
        val  bRANCH: String = "",
        val  cONTRACTNO: String = "",
        val  cUSTOMERNAME: String  = "",
        val  iDCARD: String = "",
        val  iNSTALLMENTDUEAMT: String = "",
        val  jOBID: String = "",
        val  lATITUDE: String = "",
        val  lONGITUDE: String = "",
        val  mAINID: String = "",
        val  pROFILEID: String = "",
        val  rEALADDRESS: String = "",
        val  rEGISTERNO: String  = "",
        val  rEGISTERPROVINCE: String  = "",
        val  distance : String  = "",
        val type: JobRequestLocationViewModel.Type = JobRequestLocationViewModel.Type.CUSTHOUSE

    )
}