package com.tlt.georepo.common.extension

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatRadioButton
import android.support.v7.widget.AppCompatTextView
import android.text.Html

fun AppCompatTextView.setText(prefix: String, value: String, concurrency: String, @ColorRes valueColor: Int) {
    this.text = Html.fromHtml("$prefix <font color='" +
            ContextCompat.getColor(context, valueColor) + "'>$value</font> $concurrency")
}

fun AppCompatTextView.setTextWithHtml(text: String) {
    this.text = Html.fromHtml(text)!!
}

fun AppCompatRadioButton.setText(prefix: String, value: String, concurrency: String, @ColorRes valueColor: Int) {
    this.text = Html.fromHtml("$prefix <font color='" +
            ContextCompat.getColor(context, valueColor) + "'>$value</font> $concurrency")
}

fun AppCompatTextView.setDrawableStart(@DrawableRes drawableRes: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawableRes, 0, 0, 0)
}

fun AppCompatTextView.setDrawableEnd(@DrawableRes drawableRes: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableRes, 0)
}

fun AppCompatTextView.setDrawableTop(@DrawableRes drawableRes: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, drawableRes, 0, 0)
}

fun AppCompatTextView.setDrawableBottom(@DrawableRes drawableRes: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, drawableRes)
}
