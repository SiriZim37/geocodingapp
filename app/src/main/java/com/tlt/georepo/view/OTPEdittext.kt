package com.tlt.georepo.view

import android.content.Context
import android.util.AttributeSet

class OTPEdittext @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : TLTEdittext(context, attrs, defStyleAttr) {

    private val MIN_LENGTH = 0
    private val MAX_LENGTH = 6
    private var listener: Listener? = null

    fun getRealText(): String {
        return text.toString().replace(" ", "")
    }

    fun addNumber(number: String) {
        if (getRealText().length == MAX_LENGTH) {
            return
        }

        val newText = getRealText() + number
        val result = convertFormat(newText)

        setText(result)

        if (newText.length == MAX_LENGTH) {
            listener?.let { listener!!.onOTPComplete(getRealText()) }
            return
        }
    }

    fun deleteNumber() {
        if (getRealText().length == MIN_LENGTH) {
            return
        }

        val newText = getRealText().substring(0, getRealText().lastIndex)
        val result = convertFormat(newText)

        setText(result)
    }

    fun addAllNumber(numbers: String) {
        numbers.forEach {
            addNumber("$it")
        }
    }

    fun clearAllNumber() {
        setText("")
    }

    fun setListener(callback: Listener) {
        listener = callback
    }

    private fun convertFormat(text: String): String {
        val result = text.split("").map { "$it   " }
                .reduce { previous, current -> previous + current }
                .trim()
        return "  $result  "
    }

    interface Listener {
        fun onOTPComplete(otp: String)
    }
}