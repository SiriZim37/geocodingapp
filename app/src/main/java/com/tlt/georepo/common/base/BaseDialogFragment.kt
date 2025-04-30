package com.tlt.georepo.common.base

import android.support.v7.app.AppCompatDialogFragment

open class BaseDialogFragment : AppCompatDialogFragment() {

    override fun onResume() {
        super.onResume()

      // AnalyticsManager.dialogStartTime = Calendar.getInstance().time
    }

    override fun onPause() {
        super.onPause()
      // AnalyticsManager.dialogEndTime = Calendar.getInstance().time
    }

}