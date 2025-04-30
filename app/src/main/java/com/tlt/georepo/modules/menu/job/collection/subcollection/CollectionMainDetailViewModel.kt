package com.tlt.georepo.modules.menu.job.collection.subcollection

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import com.tlt.georepo.manager.JsonMapperManager
import com.tlt.georepo.manager.LocationManager
import com.tlt.georepo.manager.api.GeoApiManager
import com.tlt.georepo.model.request.JobCollectionDetailRequest
import com.tlt.georepo.model.response.ItemJobCollectionDetailResponse
import com.tlt.georepo.modules.menu.job.collection.common.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CollectionMainDetailViewModel : ViewModel() {
    val whenDataLoadedLocationSuccess = MutableLiveData<LatLng>()
    val whenLoading = MutableLiveData<Boolean>()
    val whenDataLoadedSuccess = MutableLiveData<Model>()
    val whenDataLoadedFailure = MutableLiveData<String>()
    private var job_MainCollection : List<CollectionMainItem>? = null
    private var job_Detaillist : List<CollectionDetailItem>? = null
    private var job_Remarklist : List<CollectionRemarkItem>? = null
    private var job_Profilelist : List<CollectionProfileItem>? = null
    val whenOvertime = MutableLiveData<Boolean>()


    fun getData(mainIDExtra : String , lat: String , lng: String) {
        GlobalScope.launch(Dispatchers.Main) {
            whenLoading.postValue(true)

            try {
                val model = getJobCollectionDetail(mainIDExtra ,lat , lng)

            } catch (e: Exception) {
                if (e.message != "device logon") {
                    whenDataLoadedFailure.postValue(e.message)
                }
                e.printStackTrace()
            }
        }
    }

    /*    -------- CALL SERVICE API --------  */

    private val apiManager by lazy { GeoApiManager.getInstance() }

    private suspend fun getJobCollectionDetail(mainIDExtra : String , lat :String , lng : String ) = suspendCoroutine<ItemJobCollectionDetailResponse?> {
        val request = JobCollectionDetailRequest.build(mainIDExtra , lat , lng)

        apiManager.getJobCollectionDetail(request){ isError: Boolean, result: String ->
            try{
                whenLoading.value = false
                if (isError) {
                    if(result.equals("overtime")){
                        whenOvertime.postValue(true)
                    }else {
                    it.resumeWithException(Exception(result))}
                    return@getJobCollectionDetail
                }
                try {

                    val items = JsonMapperManager.getInstance()
                        .gson.fromJson(result, Array<ItemJobCollectionDetailResponse>::class.java)

                    if (items.isEmpty()) {
                        it.resumeWithException(Exception())
                        return@getJobCollectionDetail
                    }

                    val itemJobDetail = items.map {
                        CollectionDetailItem(
                            aDDRESSID = it.dETAIL.aDDRESSID?: "",
                            bRANCH = it.dETAIL.bRANCH?: "",
                            cHASISNO = it.dETAIL.cHASISNO?: "",
                            cOLORDESC = it.dETAIL.cOLORDESC?: "",
                            cONTRACTNO = it.dETAIL.cONTRACTNO?: "",
                            cONTRACTSTATUS = it.dETAIL.cONTRACTSTATUS?: "",
                            eNGINENO = it.dETAIL.eNGINENO?: "",
                            iNSTALLMENTAMT = it.dETAIL.iNSTALLMENTAMT?: "",
                            iNSTALLMENTDUEAMT = it.dETAIL.iNSTALLMENTDUEAMT?: "",
                            jOBID = it.dETAIL.jOBID?: "",
                            lASTDUEDATE = it.dETAIL.lASTDUEDATE?: "",
                            mAINID = it.dETAIL.mAINID?: "",
                            mODELDESC = it.dETAIL.mODELDESC?: "",
                            oTHERSAMT = it.dETAIL.oTHERSAMT?: "",
                            oVERDUEDATEFROM = it.dETAIL.oVERDUEDATEFROM?: "",
                            oVERDUEDATETO = it.dETAIL.oVERDUEDATETO?: "",
                            oVERDUEDAYS = it.dETAIL.oVERDUEDAYS?: "",
                            oVERDUEITEM = it.dETAIL.oVERDUEITEM?: "",
                            oVERDUEITEMFROM = it.dETAIL.oVERDUEITEMFROM?: "",
                            oVERDUEITEMTO = it.dETAIL.oVERDUEITEMTO?: "",
                            pAIDDATE = it.dETAIL.pAIDDATE?: "",
                            pAIDTOITEM = it.dETAIL.pAIDTOITEM?: "",
                            pENALTYAMT = it.dETAIL.pENALTYAMT?: "",
                            pENALTYDATE = it.dETAIL.pENALTYDATE?: "",
                            rEGISTERNO = it.dETAIL.rEGISTERNO?: "",
                            rEGISTERPROVINCE = it.dETAIL.rEGISTERPROVINCE?: "",
                            tOTALOVDAMT = it.dETAIL.tOTALOVDAMT?: "" ,
                            eXPECTSUINGDATE  = it.dETAIL.eXPECTSUINGDATE?: ""
                            )
                        }.toMutableList()
                         job_Detaillist = itemJobDetail


                    val itemJobProfile = items?.map {
                        CollectionProfileItem(
                            aDDRESSTYPE = it.pROFILE.aDDRESSTYPE?: "",
                            cUSTOMERNAME = it.pROFILE.cUSTOMERNAME?: "",
                            iDCARD = it.pROFILE.iDCARD?: "",
                            iDPIC = it.pROFILE.iDPIC?: "",
                            lATITUDE = it.pROFILE.lATITUDE?: "",
                            lONGITUDE = it.pROFILE.lONGITUDE?: "",
                            pROFILEID = it.pROFILE.pROFILEID?: "",
                            rEALADDRESS = it.pROFILE.rEALADDRESS?: "",
                            rECORDTYPE = it.pROFILE.rECORDTYPE?: "",
                            tELHOME = it.pROFILE.tELHOME?: "",
                            tELMOBILE= it.pROFILE.tELMOBILE?: "",
                            gUARANTOR =  it.pROFILE.gUARANTOR.map {
                                CollectionProfileGuarantorItem(
                                    aDDRESS_ID = it.aDDRESS_ID ,
                                    cUSTOMER_NAME = it.cUSTOMERNAME ,
                                    iDCARD= it.iDCARD ,
                                    iDPIC= it.iDPIC ,
                                    rECORDTYPE= it.rECORDTYPE ,
                                    tELHOME= it.tELHOME ,
                                    tELMOBILE= it.tELMOBILE
                                )
                            })
                        }!!.toMutableList()
                        job_Profilelist = itemJobProfile

                    val itemJobRemark = items.firstOrNull()?.rEMARK?.map {
                        CollectionRemarkItem(
                            jOB_REMARK = it?.jOBREMARK?: "",
                            jOB_REMARK_DATE = it?.jOBREMARKDATE ?: ""
                        )
                    }
                         job_Remarklist = itemJobRemark

                   val model =
                       Model(
                           jobDetailList = job_Detaillist ?: listOf(),
                           jobRemarkList = job_Remarklist ?: listOf(),
                           jobProfileList = job_Profilelist ?: listOf()
                       )

                    whenDataLoadedSuccess.postValue(model)
                }catch ( e : Exception){
                    Log.d("Error Connect API : " , e.stackTrace.toString())
                    whenLoading.postValue(false)
                }
            }catch ( e : Exception ) {
                Log.e( "getJobCollectionDetail : " , e.stackTrace.toString() )
                whenLoading.postValue(false)
            }
        }

    }

    data class Model(
        var jobDetailList: List<CollectionDetailItem> = listOf(),
        var jobRemarkList: List<CollectionRemarkItem> = listOf(),
        var jobProfileList: List<CollectionProfileItem> = listOf()
    )


    fun GetCurrentLocation() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val location = LocationManager.getLastKnowLocation()
                val latLng = LatLng(location.latitude, location.longitude)
                whenDataLoadedLocationSuccess.postValue(latLng)
            } catch (error: Exception) {
                error.printStackTrace()
            }
        }
    }


}