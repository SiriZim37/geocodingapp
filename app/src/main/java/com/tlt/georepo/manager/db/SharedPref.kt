package com.tlt.georepo.manager.db

import android.content.Context
import android.preference.PreferenceManager
import com.tlt.georepo.util.LocaleManager

object SharedPref {
    private val INITIAL_KEY = "INITIAL_KEY"
    fun getisInitialPref(mContext: Context): Boolean {
        val mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        return mPreferences.getBoolean(INITIAL_KEY, false)
    }

     fun setIsInitialPref(mContext: Context, isInitial: Boolean) {
        val mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        mPreferences.edit().putBoolean(INITIAL_KEY, isInitial).commit()
    }


}