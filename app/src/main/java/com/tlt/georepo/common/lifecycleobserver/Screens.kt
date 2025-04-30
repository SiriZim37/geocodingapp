package com.tlt.georepo.common.lifecycleobserver

import com.tlt.georepo.modules.main.MainMenuActivity
import com.tlt.georepo.modules.menu.job.collection.JobCollectionMainMapActivity
import com.tlt.georepo.modules.menu.job.collection.subcollection.CollectionMainDetailActivity
import com.tlt.georepo.modules.menu.job.extension.JobExtensionMainMapActivity
import com.tlt.georepo.modules.menu.job.extension.subextension.ExtensionMainDetailActivity
import com.tlt.georepo.modules.menu.job.onhand.JobOnHandBtnSheetActivity
import com.tlt.georepo.modules.menu.job.onhand.statusmenu.CancelStatusActivity
import com.tlt.georepo.modules.menu.job.onhand.statusmenu.WorkDoneStatusActivity
import com.tlt.georepo.modules.menu.job.request.detail.subrequest.RequestMainDetailActivity

object


Screens {

    val mainActivity = setOf(
            MainMenuActivity::class.java.simpleName,
            JobOnHandBtnSheetActivity::class.java.simpleName,
            JobCollectionMainMapActivity::class.java.simpleName,
            JobExtensionMainMapActivity::class.java.simpleName,
            WorkDoneStatusActivity::class.java.simpleName ,
            RequestMainDetailActivity::class.java.simpleName ,
            CollectionMainDetailActivity::class.java.simpleName ,
            ExtensionMainDetailActivity::class.java.simpleName ,
            CancelStatusActivity::class.java.simpleName
        )
}