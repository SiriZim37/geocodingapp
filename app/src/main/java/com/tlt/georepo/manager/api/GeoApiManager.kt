package com.tlt.georepo.manager.api

import com.tlt.georepo.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.tlt.georepo.model.request.*
import java.util.concurrent.TimeUnit

class GeoApiManager private constructor() {
   private val service: GeoApiService
// private val serviceLocalhost: GeoApiService

    init {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
                .addInterceptor(GeoApiRequestInterceptor())
                 .addInterceptor(loggingInterceptor)
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        service = retrofit.create(GeoApiService::class.java)
    }

    fun getDealerShowroom(request: GetDealerShowroomRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.GetDealerShowroom(request).enqueue(commonCallback(callback))
    }


    fun getPullJobCollection(request: PullJobCollectionRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getPullJobCollection(request).enqueue(commonCallback(callback))
    }

    fun getPullJobCollectionDetail(request: PullJobCollectionDetailRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getPullJobCollectionDetail(request).enqueue(commonCallback(callback))
    }

    fun getJobCollection(request: JobCollectionRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getJobCollection(request).enqueue(commonCallback(callback))
    }

    fun getJobCollectionDetail(request: JobCollectionDetailRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getJobCollectionDetail(request).enqueue(commonCallback(callback))
    }


    fun getJobExtension(request: JobExtensionRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getJobExtension(request).enqueue(commonCallback(callback))
    }

    fun getJobExtensionDetail(request: JobExtensionDetailRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getJobExtensionDetail(request).enqueue(commonCallback(callback))
    }

    fun setPinRegister(request: SetPINRegisRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.setPINRegis(request).enqueue(commonCallback(callback))
    }

    fun setLogin(request: LoginByDeviceRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.setLogin(request).enqueue(commonCallback(callback))
    }

    fun getRegistNonCust(request: RegisterNonCustRequest, callback: (isError: Boolean, result: String) -> Unit) {
       service.setRegistNoncust(request).enqueue(commonCallback(callback))
   }


    fun getSendOTP(request: SendOTPPost, callback: (isError: Boolean, result: String) -> Unit) {
        service.postSendOTP(request).enqueue(commonCallback(callback))
    }

    fun getSendOTPForgot(request: SendOTPRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.postSendOTPForgot(request).enqueue(commonCallback(callback))
    }


    fun getRegister(request: RegistPost, callback: (isError: Boolean, result: String) -> Unit) {
        service.postRegist(request).enqueue(commonCallback(callback))
    }
    fun getVerifyOTP(request: SendOTPPost, callback: (isError: Boolean, result: String) -> Unit) {
        service.postVerifyOTP(request).enqueue(commonCallback(callback))
    }


    fun getJobOnHandDetail(request: JobOnHandDetailRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getJobOnHandDetail(request).enqueue(commonCallback(callback))
    }

    fun getJobOnHand(request: JobOnHandRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getJobOnHand(request).enqueue(commonCallback(callback))
    }

    fun setActionJob(request: ActionJobRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.setActionJob(request).enqueue(commonCallback(callback))
    }


    fun getMasterData(request: GetMasterDataRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getMasterData(request).enqueue(commonCallback(callback))
    }


    fun updateMasterData(request: UpdateMasterDataRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.updateMasterData(request).enqueue(commonCallback(callback))
    }


    fun submitJob(request: SubmitJobRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.submitJob(request).enqueue(commonCallback(callback))
    }

    fun getRegistProfile(request: RegistProfileRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getRegistProfile(request).enqueue(commonCallback(callback))
    }

    fun submitImage(request: SubmitImageRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.submitImage(request).enqueue(commonCallback(callback))
    }


    fun getDataDirection(request: GetDataDirectionRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getDataDirection(request).enqueue(commonCallback(callback))
    }

    fun uploadImages(request: UploadImagesRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.uploadImages(request).enqueue(commonCallback(callback))
    }

    fun setDataLocation(request: SetDataLocationRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.setDataLocation(request).enqueue(commonCallback(callback))
    }

    fun getUpdateUserProfile(request: UpdateUserProfileRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.setUpdateUserProfile(request).enqueue(commonCallback(callback))
    }
    private fun commonCallback(callback: (isError: Boolean, result: String) -> Unit,
                               isErrorInSomeCase: (result: String) -> Boolean = { false }): GeoApiCallback<String> {
        return object : GeoApiCallback<String>() {
            override fun onSuccess(jsonResult: String) {
                val isError = isErrorInSomeCase(jsonResult)
                callback(isError, jsonResult)
            }

            override fun
                    onFailure(message: String, isWorkable: Boolean) {
                callback(true, message)
            }
        }
    }


    companion object {
        private val httpManagerInstance = GeoApiManager()
        fun getInstance() = httpManagerInstance
    }
}