package com.tlt.georepo.modules.menu.job.request.detail.subrequest

import android.arch.lifecycle.MutableLiveData
import com.tlt.georepo.common.base.BaseViewModel
import com.tlt.georepo.modules.menu.location.detail.LocationDetailViewModel

class RequestRemarkViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<LocationDetailViewModel.Model>()


}