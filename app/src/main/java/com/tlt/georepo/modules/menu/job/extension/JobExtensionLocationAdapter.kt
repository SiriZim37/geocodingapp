package com.tlt.georepo.modules.location.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tlt.georepo.R
import kotlinx.android.synthetic.main.item_main_extension_location.view.*

class JobExtensionLocationAdapter(private val items: ArrayList<JobExtensionLocationViewModel.JobExtensionMainItem>,
                                  private val listener: Listener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_main_extension_location, parent, false)
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

    fun update(items: List<JobExtensionLocationViewModel.JobExtensionMainItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }


    inner class ShowRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            val item = items[adapterPosition]

            itemView.contractNo.text = item.bRANCH + "-"+  item.cONTRACTNO
            itemView.cust_name.text = item.cUSTOMERNAME
            itemView.typeCustomer.text = item.rECORD_TYPE
            itemView.txt_reg_no.text = item.rEGISTERNO + " " + item.rEGISTERPROVINCE
            itemView.txt_unpaid_amt.text = item.iNSTALLMENTDUEAMT
            itemView.txt_unpaid_term.text = item.oVERDUE_ITEM
            itemView.destination.text = item.rEALADDRESS
            itemView.datetime.text = "Last update : "+ item.aCTIONDATE
            itemView.txt_id_card.text = item.iDCARD
            itemView.titleStatus.text =  item.aCTIONREMARK
            itemView.titlePromiseDate.text = itemView.context.getString(R.string.txtpromise_payday, item.pROMISE_DATE)
            itemView.txt_custhouse_location_distance.text = itemView.context.getString(R.string.location_distance, item.dISTANCT_KM)


        }

        private val onDetailClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onDetailClick(adapterPosition, item)
        }


        private val onCompleteClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onCompleteClick(adapterPosition, item)
        }

        init {
            itemView.custhouse_location_detail.setOnClickListener(onDetailClick)
            itemView.custhouse_location_set_complete.setOnClickListener(onCompleteClick)
        }
    }

    interface Listener {
        fun onDetailClick(index: Int, item: JobExtensionLocationViewModel.JobExtensionMainItem)
        fun onCompleteClick(index: Int, item: JobExtensionLocationViewModel.JobExtensionMainItem)
    }
}