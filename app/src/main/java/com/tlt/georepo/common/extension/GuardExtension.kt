package com.tlt.georepo.common.extension

inline fun <T> T.ifNull(block: T.() -> Unit): T {
    if (this == null) block(); return this
}

inline fun String.ifEmpty(block: () -> Unit): String {
    if (this.isEmpty()) block(); return this
}

inline fun String.ifNotEmpty(block: () -> Unit): String {
    if (this.isNotEmpty()) block(); return this
}

inline fun Boolean.ifTrue(block: () -> Unit): Boolean {
    if (this) block(); return this
}

inline fun Boolean.ifFalse(block: () -> Unit): Boolean {
    if (!this) block(); return this
}