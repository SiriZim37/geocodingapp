package com.tlt.georepo.modules.menu.job.extension.subextension

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import com.tlt.georepo.manager.JsonMapperManager
import com.tlt.georepo.manager.LocationManager
import com.tlt.georepo.manager.api.GeoApiManager
import com.tlt.georepo.model.request.JobExtensionDetailRequest
import com.tlt.georepo.model.response.ItemJobCollectionDetailResponse
import com.tlt.georepo.model.response.ItemJobExtensionDetailResponse
import com.tlt.georepo.modules.menu.job.extension.common.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.ln

class ExtensionMainDetailViewModel : ViewModel() {

    val whenDataLoadedLocationSuccess = MutableLiveData<LatLng>()
    val whenLoading = MutableLiveData<Boolean>()
    val whenDataLoadedSuccess = MutableLiveData<Model>()
    val whenDataLoadedFailure = MutableLiveData<String>()
    private var job_MainExtension: List<ExtensionMainItem>? = null
    private var job_Detaillist: List<ExtensionDetailItem>? = null
    private var job_Remarklist: List<ExtensionRemarkItem>? = null
    private var job_Profilelist: List<ExtensionProfileItem>? = null
    val whenOvertime = MutableLiveData<Boolean>()

    fun getData(mainIDExtra: String, lat: String, lng: String) {
        GlobalScope.launch(Dispatchers.Main) {
            whenLoading.postValue(true)

            try {
                val model = getJobExtensionDetail(mainIDExtra, lat, lng)

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

    private suspend fun getJobExtensionDetail(mainIDExtra: String, lat: String, lng: String) =
        suspendCoroutine<ItemJobExtensionDetailResponse?> {
            val request = JobExtensionDetailRequest.build(mainIDExtra, lat, lng)
            apiManager.getJobExtensionDetail(request) { isError: Boolean, result: String ->
                try {
                    whenLoading.value = false
                    if (isError) {
                        if (result.equals("overtime")) {
                            whenOvertime.postValue(true)
                        } else {
                            it.resumeWithException(Exception(result))
                        }

                        return@getJobExtensionDetail
                    }
                    try {

                        val items = JsonMapperManager.getInstance()
                            .gson.fromJson(result, Array<ItemJobCollectionDetailResponse>::class.java)

                        if (items.isEmpty()) {
                            it.resumeWithException(Exception())
                            return@getJobExtensionDetail
                        }

                        val itemJobDetail = items.map {
                            ExtensionDetailItem(
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
                            ExtensionProfileItem(
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
                                    ExtensionProfileGuarantorItem(
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
                            ExtensionRemarkItem(
                                jOB_REMARK = it?.jOBREMARK ?: "",
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
                    } catch (e: Exception) {
                        Log.d("Error Connect API : ", e.stackTrace.toString())
                        whenLoading.postValue(false)
                    }
                } catch (e: Exception) {
                    Log.e("getJobExtensionDetail : ", e.stackTrace.toString())
                    whenLoading.postValue(false)
                }
            }

        }

    data class Model(
        var jobDetailList: List<ExtensionDetailItem> = listOf(),
        var jobRemarkList: List<ExtensionRemarkItem> = listOf(),
        var jobProfileList: List<ExtensionProfileItem> = listOf()
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