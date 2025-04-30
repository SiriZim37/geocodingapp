package com.tlt.georepo.common.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri

fun Activity.callPhone(number: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null))
    startActivity(intent)
}

fun Context.callPhone(number: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null))
    startActivity(intent)
}