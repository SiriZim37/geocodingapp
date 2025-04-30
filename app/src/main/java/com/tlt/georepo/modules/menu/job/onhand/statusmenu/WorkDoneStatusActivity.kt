package com.tlt.georepo.modules.menu.job.onhand.statusmenu

import android.app.Activity
import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import com.sangcomz.fishbun.define.Define
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseActivity
import com.tlt.georepo.common.dialog.NormalDialogFragment
import com.tlt.georepo.common.extension.*
import com.tlt.georepo.manager.db.MasterDataManager
import com.tlt.georepo.manager.security.ImageManager
import com.tlt.georepo.modules.main.MainMenuActivity
import com.tlt.georepo.modules.menu.job.onhand.JobOnHandBtnSheetActivity
import com.tlt.georepo.util.CalendarUtils
import com.tlt.georepo.util.LocaleManager
import kotlinx.android.synthetic.main.activity_onhand_job_workdone.*
import kotlinx.android.synthetic.main.fragment_dialog_alert_temp.view.btn_yes
import kotlinx.android.synthetic.main.fragment_dialog_alert_temp.view.txt_status
import kotlinx.android.synthetic.main.widget_toolbar.view.*
import org.jetbrains.anko.enabled
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class WorkDoneStatusActivity : BaseActivity(), NormalDialogFragment.Listener {

    private var REAL_LOCATION  : String = ""
    private var REAL_LAT  : String = ""
    private var REAL_LNG : String = ""
    private var CRRENT_LAT  : String = ""
    private var CRRENT_LNG : String = ""
    private var actionCode : String = ""
    private var LIMIT_DATE = "3"
    private var LIMIT_DATE_PROMISE = "3"
    var ptp = Calendar.getInstance()
    var strPromisePay = ""
    private var masterActionCode : ArrayList<WorkDoneStatusViewModel.SubmitJob>? = null
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(WorkDoneStatusViewModel::class.java)
    }

    private val newAddress by lazy {
        intent.getStringExtra("NEW_ADDRESS") ?: ""
    }
    private val newLat by lazy {
        intent.getStringExtra( "NEW_LAT") ?: ""
    }
    private val newLng by lazy {
        intent.getStringExtra( "NEW_LNG") ?: ""
    }


    private val mainID by lazy {
        intent.getStringExtra("MAIN_ID") ?: ""
    }

    private val dataAddress by lazy {
        intent.getStringExtra("DATA_ADDRESS") ?: ""
    }

    private val dataLat by lazy {
        intent.getStringExtra("DATA_LAT") ?: ""
    }

    private val dataLng by lazy {
        intent.getStringExtra("DATA_LNG") ?: ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onhand_job_workdone)


        initInstances()
        initViewModel()


    }

    override fun onPause() {
        super.onPause()
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })


        viewModel.whenSendToApiFailure.observe(this, Observer {
            showToast("Send Contact Fail.")
        })

        viewModel.whenSendToApiSuccess.observe(this, Observer {
            showSuccessDialog()
        })


        viewModel.whenSubmitLoaded.observe(this, Observer {
            it?.let{
                masterActionCode = it
                spinner_jobDetail.setItems(it.map { it.MS_DESC })
            }


        })

        viewModel.whenOvertime.observe(this , Observer {
            showOvertimeDialog()
        })



        viewModel.whenDataLoadedLocationSuccess.observe(this, Observer {
            it?.let {

                CRRENT_LAT = it.latitude.toString()
                CRRENT_LNG = it.longitude.toString()

                // ---- Set Location For sync --- //
                if(!"".equals(newAddress)) {
                    REAL_LOCATION = newAddress
                }else{
                    REAL_LOCATION = dataAddress
                }
                if(!"".equals(newLat) && !"".equals(newLng) ) {
                    REAL_LAT = newLat
                    REAL_LNG = newLng
                }else {
                    REAL_LAT = dataLat
                    REAL_LNG = dataLng
                }
                editDay.enabled = false
                edittext_location_detail.setText(REAL_LOCATION)
                var language = "TH"
                if ("en".equals(LocaleManager.getLanguagePref(this))){
                    language = "EN"
                }else  language = "TH"
                viewModel.getDefaultData(language)
            }

        })

        viewModel.whenSubmitApiSuccess.observe(this, Observer {
            it?.let {
                try {
                    viewModel.uploadImageSendJob(it , mainID , CRRENT_LAT , CRRENT_LNG)

                } catch (e: Exception) {
                    e.message
                }
            }

        })

        viewModel.whenSubmitSendImageApiSuccess.observe(this, Observer {
            showSubMitSuccessDialog()
        })

        btn_go_to_map1.setOnClickListener{
            saveStateImage(isSaveImage = true)
            GeoLocationActivity.Open(this@WorkDoneStatusActivity , mainID , dataAddress , dataLat , dataLng)
        }

        btn_confirm.setOnClickListener{
            try {
               if(!edittext_location_detail.text!!.equals("") &&
                   !"".equals(spinner_jobDetail.selectedItem.toString())) {
                   txt_dayforpay.setTextColor(Color.BLACK)
                       var des = spinner_jobDetail.selectedItem.toString()
                       var datePromiseHold = ""

                          for (item  in this!!.masterActionCode!!) {
                               if( (item.MS_DESC).equals(des))
                                   actionCode = item.MS_VALUE
                           }
                           if("".equals(actionCode) ){
                               showPopupRequireFieldDialog()
                               return@setOnClickListener
                           }
                           if((actionCode.equals("WAIT") || actionCode.equals("HOLD") || actionCode.equals("RETURN"))
                               && editDay.text.toString().equals(getString(R.string.dd_mm_year))) {
                               txt_dayforpay.setTextColor(Color.RED)
                               showPopupRequireFieldDialog()
                               return@setOnClickListener
                           }

                           if(editDay.text.toString().equals(getString(R.string.dd_mm_year))) {
                               datePromiseHold = ""
                           }else{
                               datePromiseHold = editDay.text.toString()
                           }

                           saveStateImage(isSaveImage = true)

                           val item = WorkDoneStatusViewModel.SendJobModel(
                               actionCode =  actionCode ,
                               actionRemark = edittext_detail.text.toString()?: "",
                               latitde = CRRENT_LAT,
                               longtitde = CRRENT_LNG,
                               mainID = mainID,
                               realAddress = REAL_LOCATION,
                               paymentDay = datePromiseHold?: "",
                               realLat = REAL_LAT ,
                               realLng = REAL_LNG
                           )
                          viewModel.sentWorkDone(item)
               }else {
                   showPopupRequireFieldDialog()
               }
            }catch (e : Exception){

            }
        }

        spinner_jobDetail.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val ps = position - 1
                var res = parent!!.selectedItem.toString()
                editDay.enabled = false
                editDay.text = getString(R.string.dd_mm_year)
                try{
                    if (masterActionCode!!.size != 0){
                        for (item  in masterActionCode!!) {
                            if((item.MS_DESC).equals(res) && item.MS_VALUE.equals("WAIT")){
                                actionCode = "WAIT"
                                editDay.enabled = true
                                txt_dayforpay.text = getString(R.string.txt_promise_header)
                            }
                            if((item.MS_DESC).equals(res) && item.MS_VALUE.equals("HOLD")){
                                actionCode = "HOLD"
                                editDay.enabled = true
                                txt_dayforpay.text = getString(R.string.txt_hold_header)
                            }
                            if((item.MS_DESC).equals(res) && item.MS_VALUE.equals("RETURN")){
                                actionCode = "RETURN"
                                editDay.enabled = true
                                txt_dayforpay.text = getString(R.string.txt_return_date)
                            }
                        }
                    }
                }catch (e : Exception){
                    e.message
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

    }
    private fun showSubMitSuccessDialog() {
        try{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.fragment_dialog_alert_temp, null)
            mDialogView.txt_status.text = getString(R.string.dia_req_status_successful_record)
            val mBuilder = AlertDialog.Builder(this!!).setView(mDialogView)
            val  mAlertDialog = mBuilder.show()
            mDialogView.btn_yes.setOnClickListener {
                mAlertDialog.dismiss()
                if(actionCode.equals("ONHAND")) {
                    JobOnHandBtnSheetActivity.Open(this)
                }else {
                    MainMenuActivity.open(this)
                }
            }
        }catch (e : Exception){
            e.message
        }
    }

    private fun initInstances() {

        viewModel.GetCurrentLocation()
        txt_dayforpay.text = getString(R.string.txt_promise_header)

        val HoldlimitDate = MasterDataManager.getInstance()
            .getHoldLimitDate()?.map {
                HoldLimit(it.hOLDELIMITDATE!!)
            }
        val PromiseLimitDate = MasterDataManager.getInstance()
            .getPromiseLimitDate()?.map {
                PromiseLimit(it.pROMISELIMITDATE!!)
            }
        if (HoldlimitDate != null) {
            LIMIT_DATE = HoldlimitDate.get(0).hOLDELIMITDATE
        }
        if (PromiseLimitDate != null) {
            LIMIT_DATE_PROMISE = PromiseLimitDate.get(0).pROMISELIMITDATE
        }

        toolbar.widget_toolbar_navigation.setOnClickListener{
            OnClickBacktoLastActivity()
        }

        attach_upload_widget.setOnAddImageListener {
            ImageManager.open(
                activity = this@WorkDoneStatusActivity,
                maxSelect = attach_upload_widget.maximumImage,
                selectImageUri = attach_upload_widget.imageUriList
            )
        }

        attach_upload_widget.onImageChanged {
            if (it.isEmpty()) {
//                 attach_btn.isEnabled = false
                return@onImageChanged
            }
//             attach_btn.isEnabled = true
        }

        editDay.setOnClickListener {
            DatePickerDialog(
                this@WorkDoneStatusActivity, promiseTopayListener,
                ptp.get(Calendar.YEAR),
                ptp.get(Calendar.MONTH),
                ptp.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

    }

    val promiseTopayListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        ptp.set(Calendar.YEAR, year)
        ptp.set(Calendar.MONTH, monthOfYear)
        ptp.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        editDay.text = sdf.format(ptp.time)
        strPromisePay = editDay.text.toString()


        try{
            ptp.set(Calendar.YEAR, year)
            ptp.set(Calendar.MONTH, monthOfYear)
            ptp.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            editDay.text = sdf.format(ptp.time)

            // check Hold limit

                val endStr = CalendarUtils.getCurrentDate()
                val simDF = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                val startDateValue  = simDF.parse(endStr)
                val endDateValue = simDF.parse(simDF.format(ptp.time))
            val differenceMillis = endDateValue!!.getTime() - startDateValue!!.getTime()
            val daysDifference = getUnitBetweenDates(startDateValue , endDateValue , TimeUnit.DAYS) //  (differenceMillis.toInt() / (1000 * 60 * 60 * 24))


            if(actionCode.equals("HOLD")){
                if(daysDifference > LIMIT_DATE.toInt() || daysDifference < 0 ){
                    val c = Calendar.getInstance()
                    c.add(Calendar.DATE, LIMIT_DATE.toInt())  // number of days to add
                    editDay.text  = sdf.format(c.time)

                }
            }else{
                if(daysDifference > LIMIT_DATE_PROMISE.toInt() || daysDifference < 0 ){
                    val c = Calendar.getInstance()
                    c.add(Calendar.DATE, LIMIT_DATE_PROMISE.toInt())  // number of days to add
                    editDay.text  = sdf.format(c.time)

                }
            }
            strPromisePay = editDay.text.toString()
        }catch ( e : Exception){
            e.message
        }
    }

    private fun getUnitBetweenDates(startDate: Date, endDate: Date, unit: TimeUnit): Long {
        val timeDiff = endDate.time - startDate.time
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS)
    }

    data class HoldLimit(
        val hOLDELIMITDATE: String = ""

    )

    data class PromiseLimit(
        val pROMISELIMITDATE: String = ""

    )


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            Define.ALBUM_REQUEST_CODE -> {
                val path = data!!.getParcelableArrayListExtra<Uri>(Define.INTENT_PATH)
                attach_upload_widget.galleryAdapter.addImageGallery(path)
                // Save to state
//                SaveStateImage()
            }
        }
    }

   fun saveStateImage(isSaveImage: Boolean = false){
       val items = mutableListOf<String>()
       val newImages = if (isSaveImage) {
           attach_upload_widget.getImageList()
       } else {
           listOf()
       }
       items.addAll(newImages)

       viewModel.saveDocumentState(newImages , CRRENT_LAT , CRRENT_LNG)
       viewModel.saveDocument(items , CRRENT_LAT , CRRENT_LNG)
//       finishWithResult()
   }

    private fun finishWithResult() {
        val imageList: ArrayList<String> = arrayListOf()
        val intent = Intent()
        attach_upload_widget.imageUriList.forEach {
            imageList.add(it.toString())
        }
        intent.putStringArrayListExtra(IMAGE_LIST, imageList)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onDialogConfirmClick() {
        finish()
    }

    override fun onDialogCancelClick() {

    }

    private fun showSuccessDialog() {
        NormalDialogFragment.show(
                fragmentManager = supportFragmentManager,
                description = getString(R.string.question_and_answer_sent_success),
                confirmButtonMessage = getString(R.string.refinance_diaglog_btn_confirm)
        )
    }

    private fun setupDataIntoViews() {

    }

    private fun watcherEnableConfirm() {
//        if (spinner_topic.isSelectHint()) {
//            btn_confirm.isEnabled = false
//            return
//        }
        btn_confirm.isEnabled = true
    }

    private fun showPopupRequireFieldDialog() {
        try{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.fragment_dialog_alert_temp, null)
            mDialogView.txt_status.text = getString(R.string.dia_req_status_please_fill_all_field)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)

            val  mAlertDialog = mBuilder.show()
            mDialogView.btn_yes.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }catch (e : Exception){
            e.message
        }

    }


    override fun onBackPressedSupport() {
        try {
            JobOnHandBtnSheetActivity.Open(this)
        }catch (e : Exception){
            e.message
        }
    }

    fun OnClickBacktoLastActivity(){
        try {
            JobOnHandBtnSheetActivity.Open(this)
        }catch (e : Exception){
            e.message
        }
    }

    private fun showOvertimeDialog() {
        try{
            val mDialogView = LayoutInflater.from( this).inflate(R.layout.fragment_dialog_alert_temp, null)
            mDialogView.txt_status.text = getString(R.string.txt_time)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
            val  mAlertDialog = mBuilder.show()
            mDialogView.btn_yes.setOnClickListener {
                mAlertDialog.dismiss()
                /* GO TO MAIN */
                MainMenuActivity.open(this)
            }
        }catch (e : Exception){
            e.message
        }
    }


    companion object {
        const val IMAGE_LIST = "imageList"
        
        fun start(context: Context? , mainID: String , realAddress :String , lat : String , lng : String ) {
            val intent = Intent(context, WorkDoneStatusActivity::class.java)
            intent.putExtra("MAIN_ID", mainID)
            intent.putExtra("DATA_ADDRESS", realAddress)
            intent.putExtra("DATA_LAT", lat)
            intent.putExtra("DATA_LNG", lng)
            context?.startActivity(intent)
        }

        fun OpenWithLocation(
            context: Context?,
            DesLocation: String,
            desLat : String  ,
            desLong : String ,
            mainID : String) {
            val intent = Intent(context, WorkDoneStatusActivity::class.java)
            intent.putExtra("NEW_ADDRESS", DesLocation)
            intent.putExtra("NEW_LAT", desLat)
            intent.putExtra("NEW_LNG", desLong)
            intent.putExtra("MAIN_ID", mainID)
            context?.startActivity(intent)
        }

    }


}
