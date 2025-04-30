package com.tlt.georepo.modules.menu.job

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.sangcomz.fishbun.BaseActivity
import com.tlt.georepo.R
import kotlinx.android.synthetic.main.activity_job_total.*

class JobTotalActivity : BaseActivity() , JobTotalViewAdapter.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(JobTotalViewModel::class.java)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_total)

        initInstance()
        initViewModel()

        viewModel.getDealerShowroomData()

    }

    private fun initInstance() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = JobTotalViewAdapter(listener = this)

        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
            viewModel.getDealerShowroomData()
        }
    }

    private fun initViewModel() {
        viewModel.whenDealerShowroomList.observe(this, Observer {
            val adapter = recycler_view.adapter as JobTotalViewAdapter
            adapter.updateItems(it!!)
        })
    }

    override fun onNewsClick(position: Int, item: Job) {

//            JobTotalActivity.start(this, item.ShowroomCode)
      }

    companion object {

        fun Open(context: Context?){
            val intent = Intent(context, JobTotalActivity::class.java)
            context?.startActivity(intent)
        }
    }

}