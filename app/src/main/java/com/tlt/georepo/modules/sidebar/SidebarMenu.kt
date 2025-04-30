package com.tlt.georepo.dataclass

data class SidebarMenu(
    val title: String,
    val type: Int = Type.MENU,
    val function: () -> Unit
) {
    constructor(type: Int = Type.DIVIDER) : this("", type, {})

    object Type {
        const val EMPTY = 0
        const val MENU = 1
        const val DIVIDER = 2
    }
}