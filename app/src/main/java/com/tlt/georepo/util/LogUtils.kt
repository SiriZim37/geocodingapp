package com.tlt.georepo.util

import android.util.Log
import com.tlt.georepo.common.extension.ifTrue

object LogUtils {
    fun log(tag: String = "", message: String = "") {
        AppUtils.isDevEnvironment().ifTrue {
            Log.d(tag, message)
        }
    }
}