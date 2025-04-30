package com.tlt.georepo.view

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.widget_toolbar.view.*
import com.tlt.georepo.R
import com.tlt.georepo.common.extension.gone
import com.tlt.georepo.common.extension.ifTrue
import com.tlt.georepo.common.extension.visible


class ToolbarWidget : FrameLayout {

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.widget_toolbar, this, true)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ToolbarWidget, 0, 0)
            val toolbarSize = typedArray.getInt(R.styleable.ToolbarWidget_toolbarSize, 2)
            val title = typedArray.getString(R.styleable.ToolbarWidget_title) ?: ""
            val iconTitle = typedArray.getDrawable(R.styleable.ToolbarWidget_iconTitle)
            val navigationIcon = typedArray.getDrawable(R.styleable.ToolbarWidget_navigationIcon)
                    ?: ContextCompat.getDrawable(context, R.drawable.ic_arrow_left)
            val menuIcon = typedArray.getDrawable(R.styleable.ToolbarWidget_menuIcon)
            val menuTitle = typedArray.getString(R.styleable.ToolbarWidget_menuTitle)
            val isEnableNavigationIcon = typedArray.getBoolean(R.styleable.ToolbarWidget_enableNavigationIcon, true)

            typedArray.recycle()

            changeToolbarSize(ToolbarSize.getByValue(toolbarSize))

            widget_toolbar_txt_title.text = title

            iconTitle?.let {
                widget_toolbar_txt_title.setCompoundDrawablesWithIntrinsicBounds(it, null, null, null)
                widget_toolbar_txt_title.compoundDrawablePadding = 30
            }

            menuIcon?.let {
                widget_toolbar_menu_right.setImageDrawable(menuIcon)
                widget_toolbar_menu_right.visible()
            }

            menuTitle?.let {
                widget_toolbar_txt_right.text = it
                widget_toolbar_txt_right.visible()
            }

            isEnableNavigationIcon.ifTrue {
                widget_toolbar_navigation.visible()
                widget_toolbar_navigation.setImageDrawable(navigationIcon)
            }
        }

        initDefaultEvent()
    }

    private fun initDefaultEvent() {
        setOnHambergerMenuClickListener {
            val activity = context as Activity
            activity.onBackPressed()
        }
    }

    fun setTitle(title: String,
                 iconDrawableRes: Int = android.R.color.transparent,
                 iconDrawableDirection: IconDrawableDirection = IconDrawableDirection.LEFT) {
        widget_toolbar_txt_title.text = title
        setIconDrawableBy(
                iconDrawableRes = iconDrawableRes,
                direction = iconDrawableDirection
        )
    }

    fun getTitle() = widget_toolbar_txt_title.text.toString()

    fun setOnHambergerMenuClickListener(callback: () -> Unit) {
      // AnalyticsManager.sideBarStartTime = Calendar.getInstance().time
        widget_toolbar_navigation.setOnClickListener {
            callback.invoke()
        }
    }

    fun setOnTitleClickListener(callback: () -> Unit) {
        widget_toolbar_txt_title.setOnClickListener {
            callback.invoke()
        }
    }

    fun setOnRightMenuIconClickListener(callback: () -> Unit) {
        widget_toolbar_menu_right.setOnClickListener {
            callback.invoke()
        }
    }

    fun setOnRightMenuTitleClickListener(callback: () -> Unit) {
        widget_toolbar_txt_right.setOnClickListener {
            callback.invoke()
        }
    }

    fun showBackLeftButton() {
        widget_toolbar_navigation.visible()
        widget_toolbar_navigation.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_left))
    }

    fun onBackLeftButton() {
        widget_toolbar_navigation.visible()
        widget_toolbar_navigation.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_left))
    }

    fun hideTextRightMenu() {
        widget_toolbar_txt_right.gone()
    }

    fun showTextRightMenu() {
        widget_toolbar_txt_right.visible()
    }

    fun showIconRightMenu(@DrawableRes drawableRes: Int) {
        val menuIcon = ContextCompat.getDrawable(context, drawableRes)
        widget_toolbar_menu_right.setImageDrawable(menuIcon)
        widget_toolbar_menu_right.visible()
    }

    fun hideIconRightMenu() {
        widget_toolbar_menu_right.gone()
    }

    fun changeToolbarSize(toolbarSize: ToolbarSize) {
        val toolbarHeight = when (toolbarSize) {
            ToolbarSize.MEDIUM -> resources.getDimension(R.dimen.toolbar_medium_height)
            ToolbarSize.LARGE -> resources.getDimension(R.dimen.toolbar_large_height)
            else -> resources.getDimension(R.dimen.toolbar_normal_height)
        }

        layout_toolbar.apply {
            layoutParams.height = toolbarHeight.toInt()
            layoutParams = layoutParams
        }
    }

    private fun setIconDrawableBy(
            iconDrawableRes: Int = android.R.color.transparent,
            direction: IconDrawableDirection = IconDrawableDirection.LEFT
    ) {
        val drawableMap = mutableMapOf<IconDrawableDirection, Drawable?>(
                IconDrawableDirection.LEFT to null,
                IconDrawableDirection.TOP to null,
                IconDrawableDirection.RIGHT to null,
                IconDrawableDirection.BOTTOM to null
        )
        drawableMap[direction] = ContextCompat.getDrawable(context, iconDrawableRes)

        widget_toolbar_txt_title.setCompoundDrawablesWithIntrinsicBounds(
                drawableMap[IconDrawableDirection.LEFT],
                drawableMap[IconDrawableDirection.TOP],
                drawableMap[IconDrawableDirection.RIGHT],
                drawableMap[IconDrawableDirection.BOTTOM]
        )

        if (iconDrawableRes == android.R.color.transparent) {
            widget_toolbar_txt_title.compoundDrawablePadding = 0
        } else {
            widget_toolbar_txt_title.compoundDrawablePadding = 30
        }
    }

    enum class IconDrawableDirection(val value: Int) {
        LEFT(0),
        TOP(1),
        RIGHT(2),
        BOTTOM(3)
    }

    enum class ToolbarSize(val value: Int) {
        NORMAL(0),
        MEDIUM(1),
        LARGE(2);

        companion object {
            fun getByValue(value: Int) = ToolbarSize.values()[value]
        }
    }
}