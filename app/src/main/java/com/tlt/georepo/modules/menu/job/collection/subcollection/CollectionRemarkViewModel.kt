package com.tlt.georepo.modules.menu.job.collection.subcollection

import android.arch.lifecycle.MutableLiveData
import com.tlt.georepo.common.base.BaseViewModel
import com.tlt.georepo.modules.menu.location.detail.LocationDetailViewModel

class CollectionRemarkViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<LocationDetailViewModel.Model>()


}