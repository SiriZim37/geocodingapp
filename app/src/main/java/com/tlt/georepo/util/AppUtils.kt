package com.tlt.georepo.util

import android.content.Context
import android.content.Intent
import com.tlt.georepo.BuildConfig
import com.tlt.georepo.manager.ContextManager

object AppUtils {
//    fun isDebug() = BuildConfig.DEBUG
    fun isDevEnvironment() = BuildConfig.DEBUG
//    fun isDevEnvironment() = BuildConfig.DEBUG || BuildConfig.IS_AUTOMATE_TEST_MODE
//    fun isLocalhostEnvironment() = BuildConfig.IS_LOCALHOST_MODE
//    fun isStaffApp() = BuildConfig.IS_STAFF_APP

    fun dpToPx(dp: Int): Int {
        val density = ContextManager.getInstance()
                .getApplicationContext()
                .resources
                .displayMetrics
                .density
        return Math.round(dp.toFloat() * density)
    }

    fun shareTextContent(context: Context, text: String = "") {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        context.startActivity(Intent.createChooser(sendIntent, "Share content using"))
    }
}