package com.tlt.georepo.view

import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import com.tlt.georepo.R

class TLTTabLayout : TabLayout {

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr)

    fun changeTabsFont() {
        val vg = this.getChildAt(0) as ViewGroup
        val tabsCount = vg.childCount
        for (j in 0 until tabsCount) {
            val vgTab = vg.getChildAt(j) as ViewGroup
            val tabChildsCount = vgTab.childCount
            for (i in 0 until tabChildsCount) {
                val tabViewChild = vgTab.getChildAt(i)
                val tf = ResourcesCompat.getFont(context, R.font.rsu_bold)
                if (tabViewChild is TextView) {
                    tabViewChild.typeface = tf
                }
            }
        }
    }
}