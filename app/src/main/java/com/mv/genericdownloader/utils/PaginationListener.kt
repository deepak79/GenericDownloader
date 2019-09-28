package com.mv.genericdownloader.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

abstract class PaginationListener
    (
    private val layoutManager: GridLayoutManager,
    val mFloatingActionButton: FloatingActionButton
) : RecyclerView.OnScrollListener() {

    abstract val isLoading: Boolean

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (dy > 0 || dy < 0 && mFloatingActionButton.isShown) {
            mFloatingActionButton.hide()
        }
        if (!isLoading) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
            ) {
                loadMoreItems()
            }
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE)
        {
            mFloatingActionButton.show();
        }
        super.onScrollStateChanged(recyclerView, newState)
    }

    protected abstract fun loadMoreItems()
}