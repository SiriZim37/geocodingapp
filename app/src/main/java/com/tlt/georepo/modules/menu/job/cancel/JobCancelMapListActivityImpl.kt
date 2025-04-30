package com.tlt.georepo.modules.menu.job.cancel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tlt.georepo.modules.cancel.JobCancelMapActivity
import kotlinx.android.synthetic.main.activity_job_total.*

import java.util.ArrayList
import java.util.Arrays

class JobCancelMapListActivityImpl : JobCancelMapListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.setTitle("Cancel Job")
        mRecyclerView.setHasFixedSize(true)
        initInstance()
    }

    private fun initInstance() {
        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
            //viewModel.getDealerShowroomData()
        }
    }

    override fun createMapListAdapter(): JobCancelMapLocationAdapter {
        val mapLocations = ArrayList<JobCancelMapLocation>(LIST_LOCATIONS.size)
        mapLocations.addAll(Arrays.asList(*LIST_LOCATIONS))

        val adapter = JobCancelMapLocationAdapter()
        adapter.setMapLocations(mapLocations)

        return adapter
    }

    override fun showMapDetails(view: View) {
        val mapLocation = view.tag as JobCancelMapLocation

        val intent = Intent(this, JobCancelMapActivity::class.java)
        intent.putExtra(JobCancelMapActivity.EXTRA_LATITUDE, mapLocation.center.latitude)
        intent.putExtra(JobCancelMapActivity.EXTRA_LONGITUDE, mapLocation.center.longitude)

        startActivity(intent)
    }

    companion object {

        private val LIST_LOCATIONS = arrayOf(
             JobCancelMapLocation("1" ,"นาย พัฒนาD ระบบ", 50.854509, 4.376678,"Brussels", "CANCEL" , "4-05-2019 08:30" , "Moved Out")
            )


        fun Open(context: Context?) {
            val intent = Intent(context, JobCancelMapListActivityImpl::class.java)
            context?.startActivity(intent)
        }

    }


}
