package com.tlt.georepo.modules.menu.job.extension.subextension

import android.arch.lifecycle.MutableLiveData
import com.tlt.georepo.common.base.BaseViewModel
import com.tlt.georepo.modules.menu.location.detail.LocationDetailViewModel

class ExtensionDetailViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<LocationDetailViewModel.Model>()

}
