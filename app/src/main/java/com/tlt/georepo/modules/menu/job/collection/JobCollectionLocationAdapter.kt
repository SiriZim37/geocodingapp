package com.tlt.georepo.modules.location.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tlt.georepo.R
import com.tlt.georepo.common.extension.gone
import com.tlt.georepo.common.extension.invisible
import com.tlt.georepo.common.extension.visible
import kotlinx.android.synthetic.main.item_main_collection_location.view.*

class JobCollectionLocationAdapter(private val items: ArrayList<JobCollectionLocationViewModel.JobCollectionMainItem>,
                                   private val listener: Listener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_main_collection_location, parent, false)
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

    fun update(items: List<JobCollectionLocationViewModel.JobCollectionMainItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }


    inner class ShowRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            val item = items[adapterPosition]

              itemView.contractNo.text = item.bRANCH + "-" + item.cONTRACTNO
              itemView.cust_name.text = item.cUSTOMERNAME
              itemView.typeCustomer.text = item.rECORD_TYPE
              itemView.txt_reg_no.text = item.rEGISTERNO + " " + item.rEGISTERPROVINCE
              itemView.txt_unpaid_amt.text = item.iNSTALLMENTDUEAMT
              itemView.txt_unpaid_term.text = item.oVERDUE_ITEM
              itemView.destination.text = item.rEALADDRESS
              itemView.titleStatus.text = item.aCTIONCODE
              itemView.datetime.text = itemView.resources.getString(R.string.txt_lastupdate) + item.aCTIONDATE
              itemView.txt_id_card.text = item.iDCARD
              itemView.titleStatus.text = "( " + item.aCTIONCODE + " )"
              itemView.custhouse_location_direction.visible()
              itemView.custhouse_location_set_work.visible()
              itemView.custhouse_location_detail.visible()
              if (item.aCTIONCODE == "ACCEPT") {
                  itemView.titleStatus.setTextColor(R.color.greenbtn)
//             itemView.titleStatus.setBackgroundResource(R.color.blueList)
              } else if (item.aCTIONCODE == "EJECT") {
                  itemView.titleStatus.setTextColor(R.color.gray)
                  itemView.bg_item.setBackgroundResource(R.color.unactive)
                  itemView.txt_unpaid_amt.setTextColor(R.color.gray)
                  itemView.txt_unpaid_term.setTextColor(R.color.gray)
                  itemView.custhouse_location_direction.gone()
                  itemView.custhouse_location_set_work.gone()
                  itemView.custhouse_location_detail.gone()
              } else {
                  itemView.titleStatus.setTextColor(R.color.red)
//             itemView.titleStatus.setBackgroundResource(R.color.Gold)
              }
              itemView.txt_custhouse_location_distance.text =
                  itemView.context.getString(R.string.location_distance, item.dISTANCT_KM)


        }

        private val onDetailClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onDetailClick(adapterPosition, item)
        }

        private val onDirectionClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onDirectionClick(adapterPosition, item)
        }

        private val onWorkClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onWorkClick(adapterPosition, item)
        }

        init {
            itemView.custhouse_location_detail.setOnClickListener(onDetailClick)
            itemView.custhouse_location_direction.setOnClickListener(onDirectionClick)
            itemView.custhouse_location_set_work.setOnClickListener(onWorkClick)
        }
    }

    interface Listener {
        fun onDetailClick(index: Int, item: JobCollectionLocationViewModel.JobCollectionMainItem)
        fun onDirectionClick(index: Int, item: JobCollectionLocationViewModel.JobCollectionMainItem)
        fun onWorkClick(index: Int, item: JobCollectionLocationViewModel.JobCollectionMainItem)
    }
}