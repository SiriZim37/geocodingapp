package com.tlt.georepo.modules.nonuser

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import android.content.ActivityNotFoundException
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.webkit.WebView
import android.webkit.WebViewClient
import com.tlt.georepo.BuildConfig
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseActivity
import com.tlt.georepo.modules.nonuser.OTPActivity
import com.tlt.georepo.modules.pincode.SetupPincodeActivity
import com.tlt.georepo.util.LocaleManager
import kotlinx.android.synthetic.main.terms_and_condition.*


class Disclosure : BaseActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleManager.setNewLocale(this, LocaleManager.getLanguagePref(this))
        setContentView(R.layout.terms_and_condition)
        loadDisclosure()
    }

    private fun loadDisclosure() {
        val mWebView = findViewById<WebView>(R.id.webview_termsandcons)
        val webSettings = mWebView.settings
        webSettings.javaScriptEnabled = true
        mWebView.loadUrl(BuildConfig.DISCLOSURE_URL)
        mWebView.webViewClient = HelloWebViewClient()
        WebView.setWebContentsDebuggingEnabled(false)
        disagree.visibility = View.VISIBLE
        check_accept.isChecked = true
        txt_termsandcons.text = resources.getString(R.string.i_agree_on_disclosure_information)

        check_accept.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                check_not_accept.isChecked = false
            } else {
            }
        }

        check_not_accept.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                check_accept.isChecked = false
            } else {

            }
        }
        bt_terms_next.setOnClickListener {

            OTPActivity.open("",this@Disclosure)
        }
    }
    private fun callService() {

    }
    private inner class HelloWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (Uri.parse(url).host == BuildConfig.DISCLOSURE_URL) {
                return false
            }
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            Log.e("onPageStarted", url)
        }

        override fun onPageFinished(view: WebView, url: String) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url)
            Log.e("onPageFinished", url)
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview_termsandcons.canGoBack()) {
            webview_termsandcons.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressedSupport() {
        super.onBackPressedSupport()
        val intent = Intent(this, TermsAndConditions::class.java)
        startActivity(intent)
    }

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, Disclosure::class.java)
            context.startActivity(intent)
        }
    }
}