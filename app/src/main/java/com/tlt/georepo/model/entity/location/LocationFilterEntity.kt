package com.tlt.georepo.model.entity.location

import io.realm.RealmObject

open class LocationFilterEntity(
        var dealerName: String = "",
        var provinceIndex: Int = -1,
        var amphurIndex: Int = -1,
        var isOfficeChecked: Boolean = false,
        var isShowRoomChecked: Boolean = false,
        var isServiceCenterChecked: Boolean = false,
        var isRepairServiceChecked: Boolean = false,
        var isNearly: Boolean = false
) : RealmObject()