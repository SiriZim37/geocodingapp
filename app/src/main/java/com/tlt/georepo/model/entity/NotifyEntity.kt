package com.tlt.georepo.model.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class NotifyEntity(
        @PrimaryKey
        var notifyKey: String = "",
        var title: String = "",
        var jobId: String = "",
        var messageType: String = "",
        var image: String = "",
        var media: String = "",
        var message: String = "",
        var description: String = "",
        var iconColor: String = "R",
        var navigation: String = "",
        var flagCheckbox: String = "N",
        var expDateTime: Date = Date(),
        var datetime: Date = Date()
) : RealmObject() {

    fun transformToRealm(state: NotifyEntity) {
        this@NotifyEntity.notifyKey = state.notifyKey
        this@NotifyEntity.title = state.title
        this@NotifyEntity.jobId = state.jobId
        this@NotifyEntity.expDateTime = state.expDateTime
        this@NotifyEntity.flagCheckbox = state.flagCheckbox
        this@NotifyEntity.messageType = state.messageType
        this@NotifyEntity.datetime = state.datetime
        this@NotifyEntity.description = state.description
        this@NotifyEntity.image = state.image
        this@NotifyEntity.media = state.media
        this@NotifyEntity.message = state.message
        this@NotifyEntity.iconColor = state.iconColor
        this@NotifyEntity.navigation = state.navigation
    }

    fun transform(): NotifyEntity {
        return NotifyEntity().apply {
            this.notifyKey = this@NotifyEntity.notifyKey
            this.title = this@NotifyEntity.title
            this.jobId = this@NotifyEntity.jobId
            this.expDateTime = this@NotifyEntity.expDateTime
            this.flagCheckbox = this@NotifyEntity.flagCheckbox
            this.messageType = this@NotifyEntity.messageType
            this.datetime = this@NotifyEntity.datetime
            this.description = this@NotifyEntity.description
            this.image = this@NotifyEntity.image
            this.media = this@NotifyEntity.media
            this.message = this@NotifyEntity.message
            this.iconColor = this@NotifyEntity.iconColor
            this.navigation = this@NotifyEntity.navigation
        }
    }
}