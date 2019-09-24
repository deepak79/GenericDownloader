package com.mv.genericdownloader.ui.main.fragment.pinwall.adapter

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mv.genericdownloader.R
import com.mv.genericdownloader.model.response.DataResponse
import com.mv.genericdownloaderlib.core.GenericDownloadManager
import com.mv.genericdownloaderlib.interfaces.IResourceRequestCallBack
import com.mv.genericdownloaderlib.model.BaseResource
import com.mv.genericdownloaderlib.model.ImageResource
import com.mv.genericdownloaderlib.model.ResourceTypes

class MenuImagesAdapter(var mContext: Context, var mList: List<DataResponse>) :
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

        var img: ImageView

        init {
            itemView.setOnClickListener { v -> setOnItemClick(adapterPosition) }
            img = itemView.findViewById(R.id.img)
        }

        private fun setOnItemClick(adapterPosition: Int) {
            selectedPosition = adapterPosition
            notifyDataSetChanged()
        }

        fun bind(position: Int) {
            GenericDownloadManager(
                mList[position].user.profileImage.medium,
                ResourceTypes.IMAGE, object : IResourceRequestCallBack<BaseResource> {
                    override fun onSuccess(data: BaseResource) {
                        img.setImageBitmap((data as ImageResource).getBitmap())
                    }

                    override fun onFailure(error: String?) {
                        Log.e("@@@@", "Failure $error")
                    }
                })
        }
    }
}