package com.tlt.georepo.modules.menu.job

import com.tlt.georepo.common.base.BaseViewModel
import com.tlt.georepo.common.livedata.SingleLiveData
import com.tlt.georepo.manager.JsonMapperManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import com.tlt.georepo.manager.api.GeoApiManager
import com.tlt.georepo.model.request.GetDealerShowroomRequest
import com.tlt.georepo.model.response.GetDealerShowroomResponse

import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.ArrayList

class JobTotalViewModel : BaseViewModel() {


    val whenDealerShowroomList = SingleLiveData<ArrayList<Job>>()

//    private val apiManager by lazy { GeoApiManager.getInstance() }
//     fun getDealerShowroomData() {
//         GlobalScope.launch(Dispatchers.Main) {
//             try {
//                 whenLoading.postValue(true)
//
//                 val getNewsResponse = getDealerShowroom()
//                 val jobDetail = getNewsResponse?.result?.map {
//                     Job(
//                         ShowroomCode = it?.showroomCode ?: "",
//                         ShowroomName = it?.showroomName ?: "",
//                         DealerCode = it?.dealerCode ?: "",
//                         Latitude = it?.latitude ?: "",
//                         Longtitude = it?.longtitude ?: ""
//                     )
//                 }
//                 val model = Model(jobDetail ?: listOf())
//
////                 whenDataLoaded.postValue(model)
//             } catch (e: Exception) {
//                 e.printStackTrace()
//             } finally {
//                 whenLoading.postValue(false)
//             }
//         }
//     }

    fun getDealerShowroomData() {
        GeoApiManager.getInstance()
            .getDealerShowroom(GetDealerShowroomRequest.build()) { isError, result ->
                whenLoading.value = false

                if (isError) {
                    whenDealerShowroomList.value = ArrayList()
                    return@getDealerShowroom
                }
                val res = JSONObject(result).getJSONArray("Result")
                val getDealerShowroomResponse = JsonMapperManager.getInstance()
                    .gson.fromJson(res.toString(), Array<GetDealerShowroomResponse>::class.java)

                val jobDetail =   getDealerShowroomResponse?.toList()?.map {
                    Job(
                        it?.showroomCode ,
                        it?.showroomName ,
                        it?.dealerCode ,
                        it?.latitude ,
                        it?.longtitude
                    )
                }
                whenDealerShowroomList.value = ArrayList(jobDetail)
            }

    }

//    private suspend fun getDealerShowroom() = suspendCoroutine<GetDealerShowroomResponse?> {
//        apiManager.getDealerShowroom(GetDealerShowroomRequest.build()) { isError: Boolean, result: String ->
//            if (isError) {
//                it.resumeWithException(Exception(result))
//                return@getDealerShowroom
//            }
//
//            val getDealerShowroomResponse = JsonMapperManager.getInstance()
//                .gson.fromJson(result, Array<GetDealerShowroomResponse>::class.java)
//
//            if (getDealerShowroomResponse.isEmpty()
//                || getDealerShowroomResponse.firstOrNull()?.result?.isEmpty() == true) {
//                it.resumeWithException(Exception())
//                return@getDealerShowroom
//            }
//
//            it.resume(getDealerShowroomResponse.firstOrNull())
//        }
//    }


}


