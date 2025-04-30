package com.tlt.georepo.modules.menu.notify

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class NotifyMainViewModel : ViewModel() {

    val whenLoading = MutableLiveData<Boolean>()

}