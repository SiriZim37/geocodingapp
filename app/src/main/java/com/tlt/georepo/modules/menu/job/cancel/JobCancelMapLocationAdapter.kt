package com.tlt.georepo.modules.menu.job.cancel
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.google.android.gms.maps.MapView
import com.tlt.georepo.R

import java.util.ArrayList
import java.util.HashSet

class JobCancelMapLocationAdapter : RecyclerView.Adapter<JobCancelMapLocationViewHolder>() {
    var mapViews = HashSet<MapView>()
        protected set
    protected var mJobCancelMapLocations: ArrayList<JobCancelMapLocation>? = null

    fun setMapLocations(jobCancelMapLocations: ArrayList<JobCancelMapLocation>) {
        mJobCancelMapLocations = jobCancelMapLocations
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): JobCancelMapLocationViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.map_activity_list_cancle_item, viewGroup, false)
        val viewHolder = JobCancelMapLocationViewHolder(viewGroup.context, view)

        mapViews.add(viewHolder.mapView)

        return viewHolder
    }

    override fun onBindViewHolder(viewHolderJobCancel: JobCancelMapLocationViewHolder, position: Int) {
        val mapLocation = mJobCancelMapLocations!![position]

        viewHolderJobCancel.itemView.tag = mapLocation
        viewHolderJobCancel.title.text = mapLocation.id_no
        viewHolderJobCancel.name.text = mapLocation.name
        viewHolderJobCancel.locate.text = mapLocation.locate
        viewHolderJobCancel.status.text = mapLocation.status
        viewHolderJobCancel.datetime.text = mapLocation.datetime
        viewHolderJobCancel.canceldetail.text = mapLocation.canceldetail
        if(mapLocation.status == "CANCEL")
            viewHolderJobCancel.status.setBackgroundResource(R.color.red)

        viewHolderJobCancel.description.text = mapLocation.center.latitude.toString() + " " + mapLocation.center.longitude

        viewHolderJobCancel.setMapLocation(mapLocation)
    }

    override fun getItemCount(): Int {
        return if (mJobCancelMapLocations == null) 0 else mJobCancelMapLocations!!.size
    }
}
