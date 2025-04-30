package com.tlt.georepo.manager.db

import org.greenrobot.eventbus.EventBus
import com.tlt.georepo.common.eventbus.NotifyEvent
import com.tlt.georepo.common.eventbus.PushPopupEvent
import com.tlt.georepo.common.extension.ifTrue
import com.tlt.georepo.common.extension.toDateByPattern
import com.tlt.georepo.common.extension.toDatetime
import com.tlt.georepo.manager.JsonMapperManager
import com.tlt.georepo.model.entity.NotifyEntity
import com.tlt.georepo.model.response.ItemNotifyPushResponse
import com.tlt.georepo.model.response.NotificationJsonResponse

object NotifyManager {

    fun saveNotify(data: MutableMap<String, String>, primaryKey: String): ItemNotifyPushResponse {
        val json = JsonMapperManager.getInstance()
                .gson.toJson(data)
        val setNotifyPushResponse = JsonMapperManager.getInstance()
                .gson.fromJson(json, ItemNotifyPushResponse::class.java)

        val item = NotificationJsonResponse(
                title = setNotifyPushResponse.title ?: "",
                message = setNotifyPushResponse.msg ?: "",
                imageUrl = setNotifyPushResponse.image ?: "",
                flagCheckBox = setNotifyPushResponse.flagChkbox ?: "",
                notifyType = setNotifyPushResponse.msgType ?: "",
            jobId = setNotifyPushResponse.jobId ?: "" ,
            expDatetime = setNotifyPushResponse.expDateTime ?: ""
            )

        setNotifyPushResponse.navigation!!.isEmpty().ifTrue {
            setNotifyPushResponse.navigation = "tlt://etc?"
        }

        val entity = NotifyEntity().apply {
            notifyKey = primaryKey
            title = setNotifyPushResponse.title ?: ""
            jobId = setNotifyPushResponse.jobId ?: ""
            flagCheckbox = setNotifyPushResponse.flagChkbox ?: ""
            expDateTime = setNotifyPushResponse.expDateTime?.toDateByPattern()!!
            messageType = setNotifyPushResponse.msgType ?: ""
            datetime = setNotifyPushResponse.dateTime?.toDateByPattern()!!
            description = setNotifyPushResponse.msg ?: ""
            image = setNotifyPushResponse.image ?: ""
            media = setNotifyPushResponse.media ?: ""
            message = setNotifyPushResponse.msg ?: ""
            iconColor = setNotifyPushResponse.iconColor ?: ""
            navigation = "${setNotifyPushResponse.navigation}&jsonString=${item.toJsonString()}".replace("+A16", "")
        }

        setNotifyPushResponse.navigation = entity.navigation

        DatabaseManager.getInstance().saveNotify(entity)
        UserManager.getInstance().addUnreadNotification()

        return setNotifyPushResponse
    }


    fun getNotifyList() = DatabaseManager.getInstance()
            .getNotifyList()
//            .filter { it.sequenceId.isEmpty() }

    fun getPushPopupLastest() = DatabaseManager.getInstance()
            .getNotifyList()
            .first { it.jobId.isNotEmpty() }

    fun getLastest() = DatabaseManager.getInstance()
            .getNotifyList()
            .first()

    fun isShowPushPopup(): Boolean {
        DatabaseManager.getInstance()
                .getNotifyList()
                .firstOrNull()?.let {
                    return it.jobId.isNotEmpty()
                }

        return false
    }

    fun showPushPopupImmediately() {
        EventBus.getDefault().post(PushPopupEvent())
    }

    fun triggerNotifyImmediately() {
        EventBus.getDefault().post(NotifyEvent())
    }

    fun clearPushPopupData() {
        DatabaseManager.getInstance().deletePushPopupNotify()
    }
}