package com.tlt.georepo.modules.sidebar

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tlt.georepo.dataclass.SidebarMenu
import com.tlt.georepo.R
import kotlinx.android.synthetic.main.item_sidebar_menu.view.*

class SidebarMenuAdapter(private val sidebarList: ArrayList<SidebarMenu>) :
    RecyclerView.Adapter<SidebarMenuAdapter.MenuViewHolder>() {
    var context : Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return when (viewType) {
            SidebarMenu.Type.MENU -> {
                context = parent.context
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sidebar_menu, parent, false)
                MenuViewHolder(view)
            }
            else -> {
                context = parent.context
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sidebar_divider, parent, false)
                OtherViewHolder(view)
            }
        }
    }

    override fun getItemCount() = sidebarList.size

    override fun getItemViewType(position: Int) = sidebarList[position].type

    override fun onBindViewHolder(holder: MenuViewHolder, positiom: Int) {
        when (getItemViewType(positiom)) {
            SidebarMenu.Type.MENU -> holder.bind(sidebarList[positiom])
        }
    }

    open inner class MenuViewHolder(open val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(menu: SidebarMenu) {

            itemView.tv.text = menu.title
            if(menu.title == context!!.resources.getString(R.string.setting_home)){
                itemView.icon_front.setBackgroundResource(R.drawable.home)
            }else if(menu.title == context!!.resources.getString(R.string.setting_setting)){
                itemView.icon_front.setBackgroundResource(R.drawable.settings)
            }else if(menu.title == context!!.resources.getString(R.string.setting_logout)){
                itemView.icon_front.setBackgroundResource(R.drawable.exit)
            }

            itemView.layout.setOnClickListener {
                menu.function.invoke()
            }
        }
    }

    inner class OtherViewHolder(override val view: View) : MenuViewHolder(view) {
        fun bind() {}
    }
}