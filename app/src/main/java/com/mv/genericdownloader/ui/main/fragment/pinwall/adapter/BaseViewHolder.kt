package com.mv.genericdownloader.ui.main.fragment.pinwall.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var currentPosition: Int = 0
        private set

    open fun onbind(position: Int) {
        currentPosition = position
    }
}