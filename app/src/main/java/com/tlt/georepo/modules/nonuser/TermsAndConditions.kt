package com.tlt.georepo.modules.nonuser

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
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
import com.tlt.georepo.util.LocaleManager
import kotlinx.android.synthetic.main.terms_and_condition.*


class TermsAndConditions : BaseActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.terms_and_condition)
        loadTermsAndCons()
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(TermsAndConsViewModel::class.java)
    }


    private fun loadTermsAndCons() {
        val mWebView = findViewById<WebView>(R.id.webview_termsandcons)
        val webSettings = mWebView.settings
        webSettings.javaScriptEnabled = true
        mWebView.loadUrl(BuildConfig.POLICY_URL)
        mWebView.webViewClient = HelloWebViewClient()
        WebView.setWebContentsDebuggingEnabled(false)
        disagree.visibility = View.GONE
        var agree= viewModel.setLanguage(R.string.agree_terms_and_conditions)
        var btNext  = viewModel.setLanguage(R.string.next)
        bt_terms_next.text = btNext
        tv_agree.text = agree
        setEnable()
        check_accept.setOnCheckedChangeListener { buttonView, isChecked ->
            bt_terms_next.isEnabled = isChecked
        }
        bt_terms_next.setOnClickListener {
            if (check_accept.isChecked) {
                apiTermsAndCons()
            }
        }
    }

    private fun apiTermsAndCons() {
//        Disclosure.open(this@TermsAndConditions)
        OTPActivity.open("",this@TermsAndConditions)
    }

    private fun setEnable() {
        bt_terms_next.isEnabled = check_accept.isChecked
    }

    private inner class HelloWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (Uri.parse(url).host == BuildConfig.POLICY_URL) {
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
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)

    }

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, TermsAndConditions::class.java)
            context.startActivity(intent)
        }
    }
}