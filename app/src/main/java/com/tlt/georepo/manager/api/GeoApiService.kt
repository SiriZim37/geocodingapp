package com.tlt.georepo.manager.api


import android.text.Layout
import com.tlt.georepo.model.request.*
import com.tlt.georepo.model.response.ExampleResponse
import com.tlt.georepo.model.response.ResponseFromService
import com.tlt.georepo.model.response.ResponseStatus
import retrofit2.Call
import retrofit2.http.*

interface GeoApiService {

    @POST("geo/GetDealerShowroom")
    fun GetDealerShowroom(@Body request: GetDealerShowroomRequest): Call<String>

    @GET("directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ): Call<Layout.Directions>

    @POST("Application/GetJobAll")
    fun getNews(@Body request: GetJobAllRequest): Call<String>

    @Headers("Content-Type: application/json") // Accept-Language:
    @POST("Application/RegisNonCustomer")
    fun postDataTesting(@Header("Authorization") lang: String, @Body requestBody: RegisterNonCustRequest):
            Call<ResponseFromService>

    @POST("application/regisnoncust")
    fun setRegistNoncust( @Body requestBody: RegisterNonCustRequest):
            Call<String>


    @POST("Application/SetPINRegis")
    fun setPINRegis(@Body request: SetPINRegisRequest): Call<String>

    @POST("Application/LoginByDevice")
    fun setLogin(@Body request: LoginByDeviceRequest): Call<String>


    @POST("application/Register")
    fun postRegist(@Body requestBody: RegistPost):
            Call<String>

    @POST("application/SendOTP")
    fun postSendOTP(@Body requestBody: SendOTPPost):
            Call<String>

    @POST("application/SendOTP")
    fun postSendOTPForgot(@Body requestBody: SendOTPRequest):
            Call<String>


    @POST("application/VerifyOTP")
    fun postVerifyOTP(@Body requestBody: SendOTPPost):
            Call<String>

    @POST("Application/GetBanner")
    fun postBanners(@Header("Authorization") lang: String):
            Call<ResponseStatus>

    @POST("application/PullJobCollection")
    fun getPullJobCollection(@Body request: PullJobCollectionRequest): Call<String>

    @POST("application/PullJobCollectionDetail")
    fun getPullJobCollectionDetail(@Body request: PullJobCollectionDetailRequest): Call<String>


    @POST("application/JobCollection")
    fun getJobCollection(@Body request: JobCollectionRequest): Call<String>


    @POST("application/JobCollectionDetail")
    fun getJobCollectionDetail(@Body request: JobCollectionDetailRequest): Call<String>


    @POST(" application/ActionJob")
    fun setActionJob(@Body request: ActionJobRequest): Call<String>


    @POST(" application/MyJobOnHand")
    fun getJobOnHand(@Body request: JobOnHandRequest): Call<String>


    @POST(" application/MyJobOnHandDetail")
    fun getJobOnHandDetail(@Body request: JobOnHandDetailRequest): Call<String>


    @POST("application/CheckMasterLoad")
    fun getMasterData(@Body request: GetMasterDataRequest): Call<String>


    @POST("application/UpdateMasterLoad")
    fun updateMasterData(@Body request: UpdateMasterDataRequest): Call<String>


    @POST("application/SubmitJob")
    fun submitJob(@Body request: SubmitJobRequest): Call<String>

    @POST("application/RegisProfile")
    fun getRegistProfile(@Body request: RegistProfileRequest): Call<String>

    @POST("application/SubmitImage")
    fun submitImage(@Body request: SubmitImageRequest): Call<String>

    @POST("application/JobExtension")
    fun getJobExtension(@Body request: JobExtensionRequest): Call<String>


    @POST("application/JobExtensionDetail")
    fun getJobExtensionDetail(@Body request: JobExtensionDetailRequest): Call<String>


    @POST("application/GetDataDirection")
    fun getDataDirection(@Body request: GetDataDirectionRequest): Call<String>

    @POST("application/SetDataLocation")
    fun setDataLocation(@Body request: SetDataLocationRequest): Call<String>

    @POST("application/SubmitRegisterImage")
    fun uploadImages(@Body request: UploadImagesRequest): Call<String>

    @POST("application/RegisProfileUpdate")
    fun setUpdateUserProfile( @Body requestBody: UpdateUserProfileRequest):
            Call<String>


    @GET("todos/")
    fun getData():
            Call<List<ExampleResponse>>

}