package com.mv.genericdownloader.ui.main.fragment.pinwall.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mv.genericdownloader.R
import com.mv.genericdownloader.model.response.DataResponse

class MenuImagesAdapter(var mContext: Context, var mList: List<DataResponse.User.ProfileImage>) :
    RecyclerView.Adapter<MenuImagesAdapter.MenuImageVH>() {
    var selectedPosition = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuImageVH {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_images, parent, false)
        return MenuImageVH(view)
    }


    override fun getItemCount(): Int {
        if (mList.isEmpty()) {
            return 0
        }
        return mList.size
    }

    override fun onBindViewHolder(holder: MenuImageVH, position: Int) {
        holder.bind(position)
    }

    inner class MenuImageVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var img_menu: ImageView

        init {
            itemView.setOnClickListener { v -> setOnItemClick(adapterPosition) }
            img_menu = itemView.findViewById(R.id.img_menu)
        }

        private fun setOnItemClick(adapterPosition: Int) {
            selectedPosition = adapterPosition
            notifyDataSetChanged()
        }

        fun bind(position: Int) {
//            Glide.with(itemView.context).load(mList[position].image)
//                .placeholder(R.drawable.img_placeholder)
//                .into(img_menu)
        }
    }
}