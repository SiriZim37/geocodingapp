package com.tlt.georepo.modules.menu.job.onhand.statusmenu

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.tlt.georepo.common.base.BaseViewModel
import com.tlt.georepo.common.extension.ifTrue
import com.tlt.georepo.common.livedata.SingleLiveData
import com.tlt.georepo.manager.LocalizeManager
import com.tlt.georepo.manager.LocationManager
import com.tlt.georepo.manager.api.GeoApiManager
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.manager.db.DocumentManager
import com.tlt.georepo.manager.db.MasterDataManager
import com.tlt.georepo.manager.db.UserManager
import com.tlt.georepo.model.request.SubmitImageRequest
import com.tlt.georepo.model.request.SubmitJobRequest
import com.tlt.georepo.util.CalendarUtils
import com.tlt.georepo.util.LocaleManager
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class WorkDoneStatusViewModel : BaseViewModel() {

    val whenImageUploadFail = SingleLiveData<Boolean>()
    val whenImageUploading = SingleLiveData<Boolean>()
    val whenSendToApiFailure = MutableLiveData<String>()
    val whenSendToApiSuccess = MutableLiveData<Boolean>()
    private val userManager = UserManager.getInstance()
    val whenSubmitLoaded = MutableLiveData<ArrayList<SubmitJob>>()
    val whenSubmitApiSuccess = MutableLiveData<String>()
    val whenSubmitSendImageApiSuccess = MutableLiveData<Boolean>()
    private val submitJobList = ArrayList<SubmitJob>()
    val  whenDataLoadedLocationSuccess =MutableLiveData<LatLng>()
    private val apiManager by lazy { GeoApiManager.getInstance() }
    val whenOvertime = MutableLiveData<Boolean>()
    var isOvertime: Boolean = false
    private val topics by lazy {
        MasterDataManager.getInstance().getSubMitList()
    }

    fun getData() {

    }

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

    fun getDefaultData(language : String) {
        setSubmitMaster(language)
    }

    fun setSubmitMaster(language : String) {

        if (submitJobList.isEmpty()) {
            if(language == "TH"){
                val submitMaster = MasterDataManager.getInstance()
                    .getSubMitList()?.map {
                        SubmitJob(it.mSVALUE!!, it.mSDESCTH.toString())
                    }
                submitJobList.addAll(submitMaster!!)
                whenSubmitLoaded.postValue(submitJobList)
//                whenSubmitLoaded.postValue(submitJobList.map { it.MS_DESC })
            } else{
                val submitMaster = MasterDataManager.getInstance()
                    .getSubMitList()?.map {
                        SubmitJob(it.mSVALUE!!, it.mSDESCEN!!)
                    }
                submitJobList.addAll(submitMaster!!)
                whenSubmitLoaded.postValue(submitJobList)
//                whenSubmitLoaded.postValue(submitJobList.map { it.MS_DESC})
            }
       }
    }

    enum class Status {
        CUSTOMER,
        NON_CUSTOMER
    }


    data class SubmitJob(
        val MS_VALUE: String = "",
        val MS_DESC: String = ""
    )



    fun sentWorkDone( data : SendJobModel ) {

        GlobalScope.launch(Dispatchers.Main) {
            whenLoading.postValue(true)
            try {

                val request = SubmitJobRequest.build(
                    actionCode = data.actionCode ?: "",
                    actionRemark = data.actionRemark ?: "",
                    lat = data.latitde,
                    lng = data.longtitde,
                    mainId = data.mainID,
                    realAddress = data.realAddress ?: "" ,
                    paymentDay = data.paymentDay  ?: "" ,
                    realLat = data.realLat ?: "" ,
                    realLng = data.realLng ?: ""
                )

                apiManager.submitJob(request) { isError: Boolean, result: String ->
                    whenLoading.postValue(false)
                    if (isError) {
//                        isOvertime = DatabaseManager.getInstance().getOvertime().isOverTime
                        Log.e("whenOvertime", result)
                        if (result.equals("overtime")) {
                            whenOvertime.postValue(true)
                        }
                        return@submitJob
                    }
                    whenSubmitApiSuccess.postValue(result)
                    return@submitJob
                }

            }catch (e : Exception) {
                e.message
                whenLoading.postValue(false)
            }
        }

    }

    data class SendJobModel(
        val actionCode : String = "" ,
        val actionRemark: String = "" ,
        val latitde:String ="" ,
        val longtitde:String ="" ,
        val mainID : String = "",
        val realAddress:String ="" ,
        val realLat:String ="" ,
        val realLng:String = "" ,
        val paymentDay:String =""
    )

    fun uploadImageSendJob( actionID : String  , mainID: String , lat : String , lng : String ) {
        GlobalScope.launch(Dispatchers.Main) {
            val uri = DocumentManager.getSendJobDocuments()
            val seqId = ""

            if (uri?.isEmpty() != false) {
                whenSubmitSendImageApiSuccess.postValue(true)
                return@launch
            }
           var imageName = ( CalendarUtils.getCurrentDateByPattern("yyyyMMddHHmmss") ).toString()
            val requestList = withContext(Dispatchers.Default) {
                uri.map {
                    SubmitImageRequest.buildForSendJob(
                         lat = lat
                        ,lng = lng
                        ,mainID= mainID
                        ,actionID= actionID
                        ,nameImg = imageName
                        ,imageBase64= it.base64
                        ,imgLat= it.lat
                        ,imgLng= it.lng
                        ,countImg= uri.size.toString()
                    )
                }
            }
            withContext(Dispatchers.Default) { upload(requestList) }
//            DocumentManager.saveSendJobDocs(listOf() , lat , lng )
        }
    }

    private suspend fun upload(requests: List<SubmitImageRequest>) = suspendCoroutine<Boolean> { suspend ->
        GlobalScope.launch(Dispatchers.Main) {
            whenImageUploading.postValue(true)

            requests.map {
                GlobalScope.async { uploadToApi(it) }
            }.forEach {
                val isError = it.await()

                isError.ifTrue {
                    whenImageUploading.postValue(false)
                    whenImageUploadFail.postValue(true)
                    suspend.resume(false)
                    return@launch
                }
            }
//            whenImageUploading.postValue(false)
            whenSubmitSendImageApiSuccess.postValue(true)
            suspend.resume(true)
        }
    }

    private suspend fun uploadToApi(request: SubmitImageRequest) = suspendCoroutine<Boolean> {
        apiManager.submitImage(request) { isError: Boolean, result: String ->
            it.resume(isError)
        }
    }

    fun getDocumentState() = DocumentManager.getSendJobDocumentsState() ?: listOf()

    fun saveDocument(base64List: List<String> = listOf() , lat: String , lng: String) {
        DocumentManager.saveSendJobDocs( base64List = base64List , lat = lat , lng =  lng)
    }

    fun saveDocumentState(base64List: List<String> = listOf() , lat: String , lng: String) {
        DocumentManager.saveStateSendJobDocs(base64List , lat , lng)
    }




}