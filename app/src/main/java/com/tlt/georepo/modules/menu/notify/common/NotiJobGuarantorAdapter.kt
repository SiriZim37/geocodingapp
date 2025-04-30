package com.tlt.georepo.modules.menu.notify.common

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tlt.georepo.R
import com.tlt.georepo.common.extension.loadImageByBase64
import kotlinx.android.synthetic.main.item_guarantor.view.*

class NotiJobGuarantorAdapter (private val listener: Listener) : RecyclerView.Adapter<NotiJobGuarantorAdapter.ViewHolder>() {

    private val items = ArrayList<NotiJobProfileGuarantorItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_guarantor, parent, false)
        return ViewHolder(view,listener)

    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindByType1(items[position])
    }

    fun updateItems(items: List<NotiJobProfileGuarantorItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(private val view: View,
                     private val listener: Listener) : RecyclerView.ViewHolder(view) {
        fun bindByType1(item: NotiJobProfileGuarantorItem) {
            view.txt_cust_name.text = item.cUSTOMER_NAME
            view.txt_type_rec.text = "( " + item.rECORDTYPE +" )"
            view.txt_mobile_home.text = item.iDCARD
            if (!item.iDPIC.isEmpty()) {
                view.cust_profile.loadImageByBase64(item.iDPIC)
            }
            view.txt_mobile_home.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)
            view.txt_mobile_home.text = item.tELHOME
            view.txt_mobile_phone.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)
            view.txt_mobile_phone.text = item.tELMOBILE
        }
    }

    interface Listener {
    }
}