package tlt.th.co.toyotaleasing.modules.deeplink

import android.os.Bundle
import com.google.gson.Gson
import com.tlt.georepo.Fragment.MainJobFragment
import  com.tlt.georepo.common.base.BaseActivity
import  com.tlt.georepo.common.extension.getHostByDeeplink
import  com.tlt.georepo.common.extension.getQueryParameterByDeeplink
import  com.tlt.georepo.manager.db.DatabaseManager
import  com.tlt.georepo.manager.db.UserManager
import  com.tlt.georepo.model.response.NotificationJsonResponse
import com.tlt.georepo.modules.menu.notify.NotifyMainActivity

class DeeplinkActivity : BaseActivity() {

    private val JSON_STRING = "jsonString"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val host = getHostByDeeplink()
        UserManager.getInstance().minusUnreadNotification()
        val jsonString = getQueryParameterByDeeplink(JSON_STRING)
        val item = Gson().fromJson(jsonString, NotificationJsonResponse::class.java)
        NotifyMainActivity.startByDeeplinkDialog(
            context = this,
            item = item,
            isShowButtonDialog = false,
            isShowDialog = true)

    }

    private fun isAppForegroundState() = DatabaseManager.getInstance().isAppStateForeground()

}
