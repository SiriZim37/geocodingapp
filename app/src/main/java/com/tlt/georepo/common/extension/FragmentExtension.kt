package com.tlt.georepo.common.extension

import android.support.v4.app.Fragment
import android.widget.Toast

fun Fragment.showToast(message: String? = "") {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}