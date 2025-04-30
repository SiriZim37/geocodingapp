package com.tlt.georepo.modules.menu.job.request.detail.subrequest

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tlt.georepo.R
import com.tlt.georepo.modules.menu.job.request.detail.common.RequestRemarkItem
import kotlinx.android.synthetic.main.item_collection_remark.view.*

class RequestRemarkAdapter (private val listener: Listener) : RecyclerView.Adapter<RequestRemarkAdapter.ViewHolder>() {

    private val items = ArrayList<RequestRemarkItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_request_remark, parent, false)
        return ViewHolder(view,listener)

    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindByType1(items[position])
    }

    fun updateItems(items: List<RequestRemarkItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(private val view: View,
                     private val listener: Listener) : RecyclerView.ViewHolder(view) {
        fun bindByType1(item: RequestRemarkItem) {
            view.txt_job_remark.text = item.jOB_REMARK
            view.txt_job_remark_date.text = item.jOB_REMARK_DATE
        }

    }

    interface Listener {
    }
}