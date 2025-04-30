package com.tlt.georepo.common.extension

import android.support.design.widget.TextInputLayout
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.widget.FrameLayout

fun TextInputLayout.enableErrorMessage(message: String) {
    error = message
    isErrorEnabled = true
}

fun TextInputLayout.disableErrorMessage() {
    error = null
    isErrorEnabled = false
}

fun TextInputLayout.enableClearErrorWhenTextChanged() {
    val frameLayout = getChildAt(0) as FrameLayout
    val edittext = frameLayout.getChildAt(0) as AppCompatEditText

    edittext.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(message: CharSequence?, start: Int, before: Int, count: Int) {
            disableErrorMessage()
        }
    })
}