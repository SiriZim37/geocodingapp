package com.tlt.georepo.modules.menu.job.onhand

import android.app.DatePickerDialog
import android.app.Dialog
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_sts_hold.*
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseDialogFragment
import com.tlt.georepo.manager.api.GeoApiManager
import com.tlt.georepo.manager.db.MasterDataManager
import com.tlt.georepo.model.entity.masterdata.PromiseLimit
import com.tlt.georepo.model.request.ActionJobRequest
import com.tlt.georepo.modules.menu.job.collection.JobCollectionMainMapActivity
import com.tlt.georepo.util.CalendarUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class JobHoldDialogFragment : BaseDialogFragment() {
    val whenDataLoadedSuccess = MutableLiveData<Boolean>()
    private val apiManager by lazy { GeoApiManager.getInstance() }
    private var LIMIT_DATE = "3"
    var ptp = Calendar.getInstance()
    var strPromisePay = ""
    private var mainID : String = ""
    private var lat : String= ""
    private var lng : String= ""
    val whenOvertime = MutableLiveData<Boolean>()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_sts_hold, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstances()
        initViewModel()



    }

    override fun onResume() {
        super.onResume()
        dialog.window.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    override fun onPause() {
        super.onPause()
    }

    private fun initViewModel() {
        whenDataLoadedSuccess.observe(this, Observer {
            JobCollectionMainMapActivity.Open(context!!)
        })
    }

    private fun initInstances() {


        val limitDate = MasterDataManager.getInstance()
            .getHoldLimitDate()?.map {
                HoldLimit(it.hOLDELIMITDATE!!)
            }

        if (limitDate != null) {
            LIMIT_DATE = limitDate.get(0).hOLDELIMITDATE
            txtQuestion.text = txtQuestion.context.getString(R.string.dil_msg_status_hold, LIMIT_DATE)
        }

        btn_no.setOnClickListener {
            fragmentManager?.let {
                dismiss()
            }
        }

        btn_yes.setOnClickListener {
            fragmentManager?.let {
                sendActionJob("HOLD" )
            }
        }

        val HoldListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
           try{
               ptp.set(Calendar.YEAR, year)
               ptp.set(Calendar.MONTH, monthOfYear)
               ptp.set(Calendar.DAY_OF_MONTH, dayOfMonth)
               val myFormat = "yyyy-MM-dd" // mention the format you need
               val sdf = SimpleDateFormat(myFormat, Locale.US)
               editHoldDay.text = sdf.format(ptp.time)

               // check Hold limit
               val endStr = CalendarUtils.getCurrentDate()
               val simDF = SimpleDateFormat("dd/MM/yyyy", Locale.US)
               val startDateValue  = simDF.parse(endStr)
               val endDateValue = simDF.parse(simDF.format(ptp.time))
//               val differenceMillis = endDateValue!!.getTime() - startDateValue!!.getTime()
               val daysDifference = getUnitBetweenDates(startDateValue , endDateValue ,TimeUnit.DAYS) //  (differenceMillis.toInt() / (1000 * 60 * 60 * 24))

               if(daysDifference > LIMIT_DATE.toInt() || daysDifference < 0 ){
                   val c = Calendar.getInstance()
                   c.add(Calendar.DATE, LIMIT_DATE.toInt())  // number of days to add
                   editHoldDay.text  = sdf.format(c.time)

               }
               strPromisePay = editHoldDay.text.toString()
           }catch ( e : Exception){
               e.message
           }
        }

        editHoldDay.setOnClickListener {
            DatePickerDialog(
                context, HoldListener,
                ptp.get(Calendar.YEAR),
                ptp.get(Calendar.MONTH),
                ptp.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        editHoldDay.text = CalendarUtils.getCurrentDateTime().split(" ")[0]
    }

    private fun getUnitBetweenDates(startDate: Date, endDate: Date, unit: TimeUnit): Long {
        val timeDiff = endDate.time - startDate.time
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS)
    }

    data class HoldLimit(
        val hOLDELIMITDATE: String = ""

    )

    fun sendActionJob( type : String ) {
        GlobalScope.launch(Dispatchers.Main) {
            try {

                val request = ActionJobRequest.build(
                    type = type ?: "",
                    main_id = arguments?.getString(MAINID,"") ?: "",
                    lat =  arguments?.getString(CRRENT_LAT,"")?:"",
                    lng =  arguments?.getString(CRRENT_LNG,"")?:"",
                    dateHold = editHoldDay.text.toString() ,
                    remark = edittext_detail.text.toString()?: ""
                )
                apiManager.setActionJob(request) { isError: Boolean, result: String ->
                    if (isError) {
                        if (result.equals("overtime")) {
                            whenOvertime.postValue(true)
                        }
                        return@setActionJob
                    }
                    whenDataLoadedSuccess.postValue(true)
                    dismiss()
                    return@setActionJob
                }
            }catch (e : Exception) {
                e.message
            }
        }

    }

    companion object {
        private val TAG = this::class.java.simpleName!!
        val CRRENT_LAT = "CRRENT_LAT"
        val CRRENT_LNG = "CRRENT_LNG"
        val MAINID = "MAINID"
        fun newInstance() = JobHoldDialogFragment()

        fun show(fragmentManager: FragmentManager, mainID: String, lat: String, lng: String ) {
            newInstance().apply {
                arguments = Bundle().apply {
                    putString(MAINID, mainID)
                    putString(CRRENT_LAT, lat)
                    putString(CRRENT_LNG, lng)
                }
                show(fragmentManager,TAG)
            }
        }
    }

}