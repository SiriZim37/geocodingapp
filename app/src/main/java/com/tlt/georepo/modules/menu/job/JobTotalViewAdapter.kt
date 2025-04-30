package com.tlt.georepo.modules.menu.job

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tlt.georepo.R
import kotlinx.android.synthetic.main.item_total_job_recview.view.*


class JobTotalViewAdapter (private val items: ArrayList<Job> = arrayListOf(),
                           private val listener: Listener
) : RecyclerView.Adapter<JobTotalViewAdapter.ViewHolder>() {

//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_total_job_recview, parent, false)
//        return ViewHolder(view)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_total_job_recview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    fun updateItems(items: List<Job>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            val item = items[adapterPosition]
            view.title.text = item.ShowroomCode
            view.status.text = item.ShowroomName

        }

        private val onNewsClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onNewsClick(adapterPosition, item)
        }

        init {
            itemView.card_view.setOnClickListener(onNewsClick)
        }
    }

    interface Listener {
        fun onNewsClick(position: Int, item: Job)
    }
}