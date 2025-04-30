package com.tlt.georepo.view

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.TextView


class OwaspEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        blockContextMenu()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun blockContextMenu() {
        this.customSelectionActionModeCallback = BlockedActionModeCallback()
        this.isLongClickable = false
        this.setOnTouchListener { v, event ->
            this.clearFocus()
            return@setOnTouchListener false
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            this.setInsertionDisabled()
        }
        return super.onTouchEvent(event)
    }

    private fun setInsertionDisabled() {
        try {
            val editorField = TextView::class.java.getDeclaredField("mEditor")
            editorField.isAccessible = true
            val editorObject = editorField.get(this)

            val editorClass = Class.forName("android.widget.Editor")
            val mInsertionControllerEnabledField = editorClass.getDeclaredField("mInsertionControllerEnabled")
            mInsertionControllerEnabledField.isAccessible = true
            mInsertionControllerEnabledField.set(editorObject, false)
        } catch (ignored: Exception) {
            // ignore exception here
        }

    }

    private inner class BlockedActionModeCallback : ActionMode.Callback {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean = false
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean = false
        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false
        override fun onDestroyActionMode(mode: ActionMode?) {}
    }
}