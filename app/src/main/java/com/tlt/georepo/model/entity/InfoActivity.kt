package com.tlt.georepo.modules.main


import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.sangcomz.fishbun.BaseActivity
import com.tlt.georepo.R
import com.tlt.georepo.manager.ContextManager
import com.tlt.georepo.manager.LocalizeManager
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.modules.nonuser.RegisterActivity
import com.tlt.georepo.modules.pincode.AuthenPincodeActivity
import kotlinx.android.synthetic.main.splashscreen_layout.*
import android.annotation.SuppressLint
import android.arch.lifecycle.ProcessLifecycleOwner
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDelegate
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.github.ajalt.reprint.core.Reprint
import com.google.android.gms.maps.GoogleMap
import com.jakewharton.threetenabp.AndroidThreeTen
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.scottyab.rootbeer.RootBeer
import com.tlt.georepo.common.eventbus.FCMTokenRefreshedEvent
import com.tlt.georepo.model.entity.CurrentLocationNonCust
import io.fabric.sdk.android.Fabric
import me.yokeyword.fragmentation.Fragmentation
import org.greenrobot.eventbus.Subscribe


class InfoActivity : BaseActivity() {

    private lateinit var googleMap: GoogleMap
    private val SPLASH_TIME_OUT: Long = 5000 // 5 sec
    val status = 0
    private var CRRENT_LAT: String = ""
    private var CRRENT_LNG: String = ""
    private var firstShow = false
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(InfoViewModel::class.java)
    }


    private fun checkPermission(success: () -> Unit, failure: () -> Unit) {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        success.invoke()
                        return
                    }

                    failure.invoke()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen_layout)

        checkRooted()
        initViewModel()
    }

    private fun checkRooted() {
        val rootbeer = RootBeer(this)
        if (rootbeer.isRootedWithoutBusyBoxCheck) {
            AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.alert_dialog_root_title))
                .setMessage(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.alert_dialog_root_description))
                .setPositiveButton(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.dialog_button_ok)) { dialog, id ->
                    dialog.dismiss()
                    checkRooted()
                }
                .show()
        } else {
            initInstances()
        }
    }

    @Subscribe
    fun onFCMTokenRefreshed(event: FCMTokenRefreshedEvent) {

    }


    @SuppressLint("MissingPermission")
    fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap!!

    }

    private fun initInstances() {

        checkPermission(success =
        {
            viewModel.GetCurrentLocation()
            progress()
            Log.e("checkPermission", "success")
        }, failure = {
            Toast.makeText(this, "Please allow to access your location!!", Toast.LENGTH_SHORT).show()
        })

    }

    private fun initViewModel() {

//        val myTrace = FirebasePerformance.getInstance().newTrace("regis_fetch")

//        viewModel.whenUserIsCustomer.observe(this, Observer {
//            AuthenPincodeActivity.open(this@InfoActivity)
//        })
//        viewModel.whenUserIsNotCustomer.observe(this, Observer {
//            RegisterActivity.open(this@InfoActivity)
//        })
//
//        viewModel.whenLoadCustomer.observe(this, Observer {
//            viewModel.checkStatusLoginUser()
//        })

        viewModel.whenLoadMaster.observe(this, Observer {
          try {
              val userRegis = DatabaseManager.getInstance().getUserInfo()
              if (userRegis.flagLogin) {
                  AuthenPincodeActivity.open(this@InfoActivity)
              }
              else {
                  RegisterActivity.open(this@InfoActivity)
              }
          } catch (e: Exception) {
              e.printStackTrace()
              Log.e("whenLoadMaster", "false")
          }
        })

        viewModel.whenDataLoadedLocationSuccess.observe(this, Observer {
            it?.let {
                CRRENT_LAT = it.latitude.toString()
                CRRENT_LNG = it.longitude.toString()
                val item = CurrentLocationNonCust().apply {
                    id = "1"
                    latitude = CRRENT_LAT
                    longitude = CRRENT_LNG
                }
                DatabaseManager.getInstance().setCurrent(item)
                viewModel.getIntialByApi(CRRENT_LAT, CRRENT_LNG)
            }
        })

    }

    private fun progress() {
        var progressStatus = 0;
        val handler: Handler = Handler()
        Thread(Runnable {
            while (progressStatus < 100) {
                if (status == 200) {
                    progressStatus = 100
                } else {
                    progressStatus += 1
                }
                try {
                    Thread.sleep(200)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                handler.post(Runnable {
                    progressBarHorizontal.progress = progressStatus
                    if (progressStatus == 100) {
                    }
                })
            }
        }).start()
    }


}
