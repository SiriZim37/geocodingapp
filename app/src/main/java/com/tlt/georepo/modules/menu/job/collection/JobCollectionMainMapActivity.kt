package com.tlt.georepo.modules.menu.job.collection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseActivity
import com.tlt.georepo.common.extension.ifFalse
import com.tlt.georepo.manager.BusManager
import com.tlt.georepo.modules.location.main.JobCollectionLocationFragment
import com.tlt.georepo.modules.main.MainMenuActivity
import com.tlt.georepo.modules.pincode.IsExitDialogFragment
import kotlinx.android.synthetic.main.map_activity_map.*


class JobCollectionMainMapActivity : BaseActivity(),
    IsExitDialogFragment.Listener {

    private val defaultMenuPosition by lazy {
        intent.getIntExtra(MENU_POSITION_EXTRA, INSTALLMENT_MENU_POSITION)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_activity_map)

        initInstances()
    }

    private fun initInstances() {


        isSavedInstanceStateNotNull().ifFalse {
            loadMultipleRootFragment(content_container.id, defaultMenuPosition,
                findFragment(JobCollectionLocationFragment::class.java)
                    ?: JobCollectionLocationFragment.newInstance()
            )
        }

    }


    override fun onIsExitCancelClicked() {

    }

    override fun onIsExitConfirmClicked() {
        finishAffinity()
    }

    override fun onDestroy() {
        super.onDestroy()
        BusManager.unsubscribe(this)
    }


    fun OnClickBacktoLastActivity(){
        try {
            MainMenuActivity.open(this)
        }catch (e : Exception){
            e.message
        }
    }

    override fun onBackPressedSupport() {
        OnClickBacktoLastActivity()
    }


    companion object {
        const val INSTALLMENT_MENU_POSITION = 0
        const val DATA_POSITION_EXTRA = "DATA_POSITION_EXTRA"
        private const val MENU_POSITION_EXTRA = "MENU_POSITION_EXTRA"

        const val MAP_POSITION = 0

        fun Open(context: Context) {
            val intent = Intent(context, JobCollectionMainMapActivity::class.java).apply {
                putExtra(MENU_POSITION_EXTRA, MAP_POSITION)
            }

            context.startActivity(intent)
        }


        fun startWithClearStack(context: Context?, position: Int = MAP_POSITION) {
            startWithClearStack(context, position, Bundle())
        }

        fun startWithClearStack(context: Context?,
                                position: Int = MAP_POSITION,
                                data: Bundle) {
            val intent = Intent(context, JobCollectionMainMapActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(MENU_POSITION_EXTRA, position)
                putExtra(DATA_POSITION_EXTRA, data)
            }
            context?.startActivity(intent)
        }

        fun startWithClearStackByDeeplink(context: Context?,
                                          position: Int = MAP_POSITION,
                                          data: Bundle) {
            val intent = Intent(context, JobCollectionMainMapActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(MENU_POSITION_EXTRA, position)
                putExtra(DATA_POSITION_EXTRA, data)
            }
            context?.startActivity(intent)
        }
    }


}
