package com.tlt.georepo.common.extension

import android.app.Activity
import android.support.annotation.StringRes
import android.widget.Toast

fun Activity.showToast(message: String = "") {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.showToast(@StringRes resId: Int) {
    val message = getString(resId)
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.getHostByDeeplink(): String? {
    return intent?.data?.host
}

fun Activity.getQueryParameterByDeeplink(key: String): String? {
    return intent?.data?.getQueryParameter(key)
}