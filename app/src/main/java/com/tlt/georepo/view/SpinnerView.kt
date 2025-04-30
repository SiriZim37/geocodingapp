package com.tlt.georepo.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatSpinner
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.tlt.georepo.R


class SpinnerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : AppCompatSpinner(context, attrs, defStyleAttr) {

    private val items = mutableListOf<String>()
    private var defaultHint = "Select.."
    private val disablePositionSet = mutableSetOf(0)

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.SpinnerView, 0, 0)
            defaultHint = typedArray.getString(R.styleable.SpinnerView_hint) ?: defaultHint

            items.add(defaultHint)
            setAdapter()

            typedArray.recycle()
        }
    }

    private fun setAdapter() {
        adapter = Adapter(context, R.layout.item_spinner, items)
    }

    fun setItems(newItems: List<String>) {
        items.clear()
        items.add(defaultHint)
        items.addAll(newItems)
        setAdapter()
    }

    fun isSelectHint() = selectedItemPosition == 0

    fun getSelectedPositionWithoutHint() = selectedItemPosition - 1

    fun getSelectedValueWithoutHint() = items.getOrNull(getSelectedPositionWithoutHint())

    fun setTextColorByPosition(position: Int, textView: TextView) {
        if (disablePositionSet.contains(position)) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.text_disable))
        } else {
            textView.setTextColor(ContextCompat.getColor(context, R.color.text_normal))
        }
    }

    private val onItemSelected = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val textView = view as TextView
            setTextColorByPosition(position, textView)
        }

        override fun onNothingSelected(parent: AdapterView<*>) {

        }
    }

    inner class Adapter(context: Context?, resource: Int, objects: MutableList<String>?) : ArrayAdapter<String>(context, resource, objects) {
        override fun isEnabled(position: Int): Boolean {
            if (disablePositionSet.contains(position)) {
                return false
            }

            return true
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val textView = super.getDropDownView(position, convertView, parent) as TextView
            setTextColorByPosition(position, textView)
            return textView
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val textView1 = super.getView(position, convertView, parent) as TextView
            if (disablePositionSet.contains(position)) {
                textView1.setTextColor(ContextCompat.getColor(parent!!.context, R.color.grey))
            } else {
                textView1.setTextColor(ContextCompat.getColor(parent!!.context, R.color.brownish_grey))
            }
            return textView1
        }
    }
}