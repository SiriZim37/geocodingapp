//package com.tlt.georepo.view
//
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
//
//
//abstract class InfiniteScrollListener(private val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {
//
//    private var page = 1
//    private var lastItemPosition = 0
//
//    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
//        super.onScrolled(recyclerView, dx, dy)
//
//        if (canLoadMoreItems()) {
//            page += 1
//            lastItemPosition = layoutManager.findLastVisibleItemPosition()
//            onLoadMore(lastItemPosition, page)
//        }
//    }
//
//    private fun canLoadMoreItems(): Boolean {
//        val visibleItemsCount = layoutManager.childCount
//        val totalItemsCount = layoutManager.itemCount
//        val pastVisibleItemsCount = layoutManager.findFirstVisibleItemPosition()
//        return visibleItemsCount + pastVisibleItemsCount >= totalItemsCount
//                && lastItemPosition != layoutManager.findLastVisibleItemPosition()
//    }
//
//    abstract fun onLoadMore(lastItemPosition: Int, page: Int)
//}