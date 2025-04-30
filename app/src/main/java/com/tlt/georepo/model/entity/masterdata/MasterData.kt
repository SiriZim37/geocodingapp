package com.tlt.georepo.model.entity.masterdata

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject

open class MasterData : RealmObject() {
    @SerializedName("SUBMIT_JOB")
    var submitJob: RealmList<SubmitJob?>? = null
    @SerializedName("COMPANY")
    var company: RealmList<Company?>? = null
    @SerializedName("Termscon")
    var termscon: RealmList<Termscon?>? = null
    @SerializedName("Disclosure")
    var disClosure: RealmList<Disclosure?>? = null
    @SerializedName("REJECT_JOB")
    var reject_job: RealmList<RejectJob?>? = null
    @SerializedName("PROMISE_JOB")
    var promise: RealmList<PromiseLimit?>? = null
    @SerializedName("HOLD_JOB")
    var hold: RealmList<HoldLimit?>? = null
}