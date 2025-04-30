package com.tlt.georepo.view

import android.annotation.TargetApi
import android.content.Context
import android.net.Uri
import android.os.Build
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.item_gallery_view.view.*
import kotlinx.android.synthetic.main.view_recyclerview.view.*
import com.tlt.georepo.R
import com.tlt.georepo.common.extension.loadImageByBase64
import com.tlt.georepo.util.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddGalleryWidget : FrameLayout {

    var maximumImage = 5

    val imageUriList: ArrayList<Uri> = arrayListOf()
    lateinit var imageBase64UriList: MutableList<String>

    private lateinit var onImageAddedListener: ((Int) -> Unit)
    private lateinit var onImageChangedListener: (base64List: List<String>) -> Unit

    val galleryAdapter by lazy {
        GalleryAdapter()
    }

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        inflate(context, R.layout.view_recyclerview, this)
        val a = context.obtainStyledAttributes(attrs,
                R.styleable.AddGalleryWidget)

        maximumImage = a.getInt(
                R.styleable.AddGalleryWidget_maximumImage,
                0)
        imageBase64UriList = MutableList(maximumImage) { "" }
        initRecyclerView()

        a.recycle()
    }

    fun setOnAddImageListener(onAdd: (Int) -> Unit) {
        onImageAddedListener = onAdd
    }

    fun getImageList(): List<String> {
        return imageBase64UriList.filter { it != "" }
    }

    fun onImageChanged(listener: (base64List: List<String>) -> Unit) {
        onImageChangedListener = listener
    }

    fun triggerImageChanged() {
        if (!this::onImageChangedListener.isInitialized) {
            return
        }

        onImageChangedListener.invoke(getImageList())
    }

    private fun initRecyclerView() {
        custom_view_rv.layoutManager = GridLayoutManager(this.context, 3)
        custom_view_rv.adapter = galleryAdapter
    }

    inner class GalleryAdapter : RecyclerView.Adapter<GalleryViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery_view, parent, false)
            return GalleryViewHolder(view)
        }

        override fun getItemCount(): Int {
            return maximumImage
        }

        override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
            if (imageBase64UriList[position] != "") {
                holder.loadImage(imageBase64UriList[position])
                holder.itemView.gallery_close_iv.visibility = View.VISIBLE
            } else {
                holder.loadDefaultImage()
                holder.itemView.gallery_close_iv.visibility = View.GONE
            }

            holder.itemView.setOnClickListener {
                if (!this@AddGalleryWidget::onImageAddedListener.isInitialized) {
                    return@setOnClickListener
                }

                onImageAddedListener.invoke(holder.adapterPosition)
            }

            holder.itemView.gallery_close_iv.setOnClickListener {
                removeImageGallery(holder.adapterPosition)
            }
        }

        fun addImageGallery(imgUriList: ArrayList<Uri> = arrayListOf()) {
            GlobalScope.launch(Dispatchers.Main) {
                clearGallery()

                imgUriList.forEach {
                    val base64 = ImageUtils.encodeToBase64(it)
                    addImageGallery(base64)
                    imageUriList.add(it)
                }
            }
        }

        fun addImageGallery(base64Image: String) {
            val position = imageBase64UriList.indexOfFirst { it == "" }
            imageBase64UriList[position] = base64Image
            notifyItemChanged(position)
            triggerImageChanged()
        }

        private fun clearGallery() {
            imageUriList.clear()

            for (position in 0 until maximumImage) {
                imageBase64UriList[position] = ""
            }
        }

        private fun removeImageGallery(position: Int) {
            imageBase64UriList[position] = ""

            imageUriList.getOrNull(position)?.let {
                imageUriList.removeAt(position)
            }

            notifyItemChanged(position)
            triggerImageChanged()
        }
    }

    inner class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun loadImage(base64Image: String) {
            itemView.gallery_iv.loadImageByBase64(base64Image)
        }

        fun loadDefaultImage() {
            itemView.gallery_iv.setImageResource(R.drawable.upload)
        }
    }
}