package com.tlt.georepo.modules.location.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_pulljob_location.view.*
import com.tlt.georepo.R

class JobRequestLocationAdapter(private val items: ArrayList<JobRequestLocationViewModel.JobPullCollection>,
                                private val listener: Listener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_pulljob_location, parent, false)
        return ShowRoomViewHolder(view)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ShowRoomViewHolder
        viewHolder.bind()

    }

    override fun getItemViewType(position: Int): Int {
        return 0

    }

    override fun getItemCount(): Int = items.size

    fun update(items: List<JobRequestLocationViewModel.JobPullCollection>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }


    inner class ShowRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            val item = items[adapterPosition]

            itemView.txt_cust_name.text = item.cUSTOMERNAME
            itemView.txt_cust_address.text = item.rEALADDRESS
            itemView.txt_reg_no.text = item.rEGISTERNO  + " ( " + item.mODEL_DESC + " / "  + item.cOLOR_DESC +" ) "
            itemView.txt_inst_amt.text = item.iNSTALLMENTDUEAMT
            itemView.txt_unpaid_term.text = item.oVERDUE_ITEM
            itemView.txt_overdue_day.text = item.oVERDUE_DAYS
            itemView.txt_cust_no_and_type_cust.text = item.cONTRACTNO + " ( " + item.rECORD_TYPE + " ) "
            itemView.txt_custhouse_location_distance.text = itemView.context.getString(R.string.location_distance, item.dISTANCT_KM)
        }

        private val onDetailClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onDetailClick(adapterPosition, item)
        }

        private val onDirectionClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onDirectionClick(adapterPosition, item)
        }

        init {
            itemView.custhouse_location_detail.setOnClickListener(onDetailClick)
            itemView.custhouse_location_direction.setOnClickListener(onDirectionClick)
        }
    }

    interface Listener {
        fun onDetailClick(index: Int, item: JobRequestLocationViewModel.JobPullCollection)
        fun onDirectionClick(index: Int, item: JobRequestLocationViewModel.JobPullCollection)
    }
}