package com.tlt.georepo.manager

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.StringRes

class ContextManager private constructor() {

    private lateinit var context: Context

    companion object {
        @SuppressLint("StaticFieldLeak")
        private val contextInstance = ContextManager()

        fun getInstance(): ContextManager {
            return contextInstance
        }
    }

    fun setApplicationContext(context: Context) {
        this.context = context
    }

    fun getApplicationContext(): Context {
        return context
    }

    fun getStringByRes(@StringRes res : Int) = context.getString(res)
}