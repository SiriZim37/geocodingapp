package com.tlt.georepo.modules.menu.notify.subnotify

import android.arch.lifecycle.MutableLiveData
import com.tlt.georepo.common.base.BaseViewModel
import com.tlt.georepo.modules.menu.location.detail.LocationDetailViewModel

class NotiJobMainViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<LocationDetailViewModel.Model>()


}