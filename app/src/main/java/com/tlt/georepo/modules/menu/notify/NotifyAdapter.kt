package com.tlt.georepo.modules.menu.notify

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tlt.georepo.R
import com.tlt.georepo.common.extension.gone
import com.tlt.georepo.common.extension.ifTrue
import com.tlt.georepo.common.extension.visible
import kotlinx.android.synthetic.main.item_notify.view.*

class NotifyAdapter (private val listener: Listener) : RecyclerView.Adapter<NotifyAdapter.ViewHolder>() {

    private val items = ArrayList<Notify>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notify, parent, false)
        return ViewHolder(view,listener)

    }


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindByType1(items[position])
    }

    override fun getItemViewType(position: Int) = items[position].type

    fun updateItems(items: List<Notify>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(private val view: View,
                     private val listener: Listener) : RecyclerView.ViewHolder(view) {
        fun bindByType1(item: Notify) {
            view.txt_notify_type_1_detail.text = item.detail
            view.txt_notify_type_1_datetime.text = item.datetime
            view.btn_notify_type_1_more.setOnClickListener { onMoreMenuClick(it, item) }

            item.isPromotion().ifTrue {
                view.ic_notify_type_1.setImageResource(R.drawable.geo_mini)
            }


             view.btn_notify_type_1_see_more.visible()
             view.btn_notify_type_1_see_more.setOnClickListener { onSeemoreDetailClick(it, item) }

        }

        private fun canNotNavigation(navigation: String) = navigation.contains("tlt://etc") && navigation.contains("\"flagCheckBox\":\"N\"")

        private val onSeemoreDetailClick = { v: View, item: Notify ->
            listener.onSeemoreClicked(item)
        }

        private val onMoreMenuClick = { v: View, item: Notify ->
            PopupMenu(v.context, v)
                .apply {
                    setOnMenuItemClickListener {
                        onRemoveClick(item)
                        true
                    }
                    menuInflater.inflate(R.menu.item_notify_menu, menu)
                }
                .show()
        }

        private val onRemoveClick = { item: Notify ->
            listener.onRemoveClicked(item)
            true
        }
    }

    interface Listener {
        fun onRemoveClicked(notify: Notify)
        fun onSeemoreClicked(notify: Notify)
    }
}