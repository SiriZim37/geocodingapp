package com.tlt.georepo.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.widget.Toast
import com.tlt.georepo.R
//import com.tlt.georepo.modules.web.WebActivity

object ExternalAppUtils {

    fun openMailApp(context: Context) = try {
        val intent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_EMAIL)
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.open_email_app)))
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "Not Found Email App.", Toast.LENGTH_SHORT).show()
    }

    fun openDialApp(context: Context?, tel: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$tel")
        context?.startActivity(intent)
    }

    fun openGoogleDirection(context: Context?,
                            lat: Double,
                            lng: Double) {
        val uri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$lat,$lng")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context?.startActivity(intent)
    }

    fun openFacebook(context: Context?, personalId : String) {
        // Build the intent
        val uri = Uri.parse("fb://page/$personalId")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        // Verify it resolves
        val activities: List<ResolveInfo> = context?.packageManager?.queryIntentActivities(intent, 0) ?: listOf()
        val isIntentSafe: Boolean = activities.isNotEmpty()

        // Start an activity if it's safe
//        if (isIntentSafe) {
//            context?.startActivity(intent)
//        } else {
//            WebActivity.start(context, "Toyota Leasing Thailand", "https://www.facebook.com/$personalId/")
//        }
    }

    fun openByLink(context : Context?, url : String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context?.startActivity(intent)
    }

    fun openBankDeepLink(context : Context?, deepUrl : String , url : String  ) {
        try {
            val uri = Uri.parse(deepUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context?.startActivity(intent)
        }catch ( e : Exception){
            openByLink(context ,url )
        }

    }

}