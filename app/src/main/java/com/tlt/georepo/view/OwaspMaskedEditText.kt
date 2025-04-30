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
import com.tlt.georepo.R
import com.tlt.georepo.common.extension.isPlaceHolder

class OwaspMaskedEditText : AppCompatEditText {

    private var selfChange: Boolean = false

    var mask: String? = null
        set(value) {
            field = value
            format(text)
        }

    val rawText get() = unFormat(text)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet) {
        blockContextMenu()
        attrs.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.OwaspMaskedEditText, 0, 0)
            mask = typedArray.getString(R.styleable.OwaspMaskedEditText_tlt_mask)
            typedArray.recycle()
        }
    }

    fun setMaskFormat(mask: String) {
        this.mask = mask
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

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        if (text.isNullOrEmpty() || selfChange) {
            return
        }
        format(text)
        setCursorPosition(start, lengthBefore, lengthAfter)
    }

    private fun format(source: CharSequence?) {
        if (source.isNullOrEmpty() || mask.isNullOrEmpty()) {
            return
        }

        selfChange = true

        val builder = StringBuilder()
        val textLength = source.length
        var textIndex = 0

        mask?.forEach {
            if (textIndex >= textLength) {
                return@forEach
            }

            var c = source[textIndex]
            if (it.isPlaceHolder()) {
                if (it.isLetterOrDigit()) {
                    builder.append(c)
                    textIndex++
                } else {
                    for (i in textIndex until textLength) {
                        c = source[i]
                        if (c.isLetterOrDigit()) {
                            builder.append(c)
                            textIndex = i + 1
                            break
                        }
                    }
                }
            } else {
                builder.append(it)
                if (c == it) {
                    textIndex++
                }
            }
        }
        setText(builder)

        selfChange = false
    }

    private fun unFormat(source: CharSequence?): String? {
        if (source.isNullOrEmpty() || mask.isNullOrEmpty()) {
            return null
        }

        val builder = StringBuilder()
        val textLength = source.length
        mask?.forEachIndexed { index, char ->
            if (index >= textLength) {
                return@forEachIndexed
            }

            val c = source[index]
            if (char.isPlaceHolder()) {
                builder.append(c)
            }
        }
        return builder.toString()
    }

    private fun setCursorPosition(start: Int, lengthBefore: Int, lengthAfter: Int) {
        if (text.isNullOrEmpty()) {
            return
        }

        val end = text!!.length
        val cursor = when {
            lengthBefore > lengthAfter -> start
            lengthAfter > 1 -> end
            start < end -> findNextPlaceHolderPosition(start, end)
            else -> end
        }
        setSelection(cursor)
    }

    private fun findNextPlaceHolderPosition(start: Int, end: Int): Int {
        mask?.let {
            for (i in start until end) {
                val mask = it[i]
                val char = this!!.text!![i]
                if (mask.isPlaceHolder() && char.isLetterOrDigit()) {
                    return i + 1
                }
            }
        }
        return start + 1
    }


}