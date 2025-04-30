package com.tlt.georepo.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.tlt.georepo.common.base.BaseActivity
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.support.v7.app.AlertDialog
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import com.tlt.georepo.R
import com.tlt.georepo.util.ExternalAppUtils
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.geo_deeplink_wb.*

class GeoDeepLinkWebViewActivity  : BaseActivity() ,  AdvancedWebView.Listener  {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.geo_deeplink_wb)
        initInstances()
        webview.loadUrl("https://tltportal.azurewebsites.net")
    }


    private fun initInstances() {
        try{
            webview.webViewClient = getWebClient()
        }catch (e : Exception){
            e.printStackTrace()
        }
        toolbar.setOnHambergerMenuClickListener {

            finish()
        }

    }

    private fun getWebClient() = object : WebViewClient() {

        override fun onReceivedSslError(view: WebView?,
                                        handler: SslErrorHandler?,
                                        error: SslError?) {

//            handler?.proceed()
            val message = error?.let { getSslErrorMessage(it) }
            AlertDialog.Builder(this@GeoDeepLinkWebViewActivity)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("OK") { dialog, id ->
                    handler?.proceed()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    handler?.cancel()
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun getSslErrorMessage(error: SslError): String {
        when (error.primaryError) {
            SslError.SSL_DATE_INVALID -> return "The certificate date is invalid."
            SslError.SSL_EXPIRED -> return "The certificate has expired."
            SslError.SSL_IDMISMATCH -> return "The certificate hostname mismatch."
            SslError.SSL_INVALID -> return "The certificate is invalid."
            SslError.SSL_NOTYETVALID -> return "The certificate is not yet valid"
            SslError.SSL_UNTRUSTED -> return "The certificate is untrusted."
            else -> return "SSL Certificate error."
        }
    }


    private fun isPaymentSuccess(url: String?): Boolean {

        if (url != null) {
            ExternalAppUtils.openBankDeepLink(this, url, url)
        }

        return url?.contains("response=success", true) == true
    }

    private fun isPaymentCancel(url: String?): Boolean {
        return url?.contains("response=cancel", true) == true
    }

    override fun onPause() {
        webview.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        webview.onResume()
    }

    override fun onDestroy() {
        webview.onDestroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        webview.onActivityResult(requestCode, resultCode, intent)
    }

    override fun onBackPressedSupport() {
        if (!webview.onBackPressed()) {
            return
        }
        super.onBackPressedSupport()
    }


    override fun onPageFinished(url: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onExternalPageRequest(url: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

        companion object {
            private val STROKE_WIDTH = 5f
            private val HALF_STROKE_WIDTH = STROKE_WIDTH / 2

            fun open(context: Context) {
                val intent = Intent(context, GeoDeepLinkWebViewActivity::class.java)
                context.startActivity(intent)
            }
        }
}
