package com.tlt.georepo.modules.menu.job.onhand.topmenu.subdetail

import android.arch.lifecycle.MutableLiveData
import com.tlt.georepo.common.base.BaseViewModel
import com.tlt.georepo.modules.menu.location.detail.LocationDetailViewModel

class SubRemarkViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<LocationDetailViewModel.Model>()


}