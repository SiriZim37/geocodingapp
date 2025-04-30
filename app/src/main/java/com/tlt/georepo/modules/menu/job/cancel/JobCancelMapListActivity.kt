package com.tlt.georepo.modules.menu.job.cancel
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.sangcomz.fishbun.BaseActivity
import com.tlt.georepo.R
//import com.tlt.georepo.modules.menu.job.total.JobTotalMainMapActivity

abstract class JobCancelMapListActivity : BaseActivity() {

    protected var mListAdapterJobCancel: JobCancelMapLocationAdapter? = null
    protected lateinit var mRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_activity_list)

        mRecyclerView = findViewById(R.id.card_list) as RecyclerView

        // Determine the number of columns to display, based on screen width.
        val rows = getResources().getInteger(R.integer.map_grid_cols)
        val layoutManager = GridLayoutManager(this, rows, GridLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = layoutManager

        mListAdapterJobCancel = createMapListAdapter()

        // Delay attaching Adapter to RecyclerView until we can ensure that we have correct
        // Google Play service version (in onResume).
    }

    protected abstract fun createMapListAdapter(): JobCancelMapLocationAdapter

    override fun onLowMemory() {
        super.onLowMemory()

        if (mListAdapterJobCancel != null) {
            for (m in mListAdapterJobCancel!!.mapViews) {
                m.onLowMemory()
            }
        }
    }

    protected override fun onPause() {
        super.onPause()

        if (mListAdapterJobCancel != null) {
            for (m in mListAdapterJobCancel!!.mapViews) {
                m.onPause()
            }
        }
    }

    protected override fun onResume() {
        super.onResume()

        val resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)

        if (resultCode == ConnectionResult.SUCCESS) {
            mRecyclerView.adapter = mListAdapterJobCancel
        } else {
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1).show()
        }

        if (mListAdapterJobCancel != null) {
            for (m in mListAdapterJobCancel!!.mapViews) {
                m.onResume()
            }
        }
    }

    protected override fun onDestroy() {
        if (mListAdapterJobCancel != null) {
            for (m in mListAdapterJobCancel!!.mapViews) {
                m.onDestroy()
            }
        }

        super.onDestroy()
    }

    /**
     * Show a full mapView when a mapView card is selected. This method is attached to each CardView
     * displayed within this activity's RecyclerView.
     *
     * @param view The view (CardView) that was clicked.
     */
    abstract fun showMapDetails(view: View)




}
