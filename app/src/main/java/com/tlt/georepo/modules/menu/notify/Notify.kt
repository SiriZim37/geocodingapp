package com.tlt.georepo.modules.menu.notify

data class Notify(
        var id: String = "",
        var detail: String = "",
        var imageUrl: String = "",
        var link: String = "",
        var datetime: String = "",
        var iconType: String = "",
        var type: Int = 1,
        var year: String = "",
        var navigation : String = "") {

    fun isPromotion() = iconType == "G"

    object Type {
        val FIRST = 1
        val SECOND = 2
        val THRID = 3
        val FOUR = 4
    }
}