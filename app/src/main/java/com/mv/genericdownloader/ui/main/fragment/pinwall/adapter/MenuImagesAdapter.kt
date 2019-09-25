package com.mv.genericdownloader.ui.main.fragment.pinwall.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mv.genericdownloader.R
import com.mv.genericdownloader.model.response.DataResponse
import com.mv.genericdownloaderlib.core.GenericDownloadManager
import com.mv.genericdownloaderlib.enums.ResourceTypes
import com.mv.genericdownloaderlib.interfaces.IResourceRequestCallBack
import com.mv.genericdownloaderlib.model.BaseResource
import com.mv.genericdownloaderlib.model.ImageResource


class MenuImagesAdapter(var mContext: Context, var mList: List<DataResponse>) :
    RecyclerView.Adapter<BaseViewHolder>() {
    var selectedPosition = 0
    private val VIEW_TYPE_LOADING = 0
    private val VIEW_TYPE_NORMAL = 1
    private val isLoaderVisible = false
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

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        //https://androidwave.com/pagination-in-recyclerview/
        holder.onBind(position)
    }

    inner class MenuImageVH(itemView: View) : BaseViewHolder(itemView) {
        var img: ImageView
        override fun clear() {

        }

        init {
            itemView.setOnClickListener {
                setOnItemClick(adapterPosition)
            }
            img = itemView.findViewById(R.id.img)
        }

        private fun setOnItemClick(adapterPosition: Int) {
            selectedPosition = adapterPosition
            notifyDataSetChanged()
        }

        fun bind(position: Int) {
            GenericDownloadManager(
                mList[position].user.profileImage.large,
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

    inner class ProgressVH(itemView: View) : BaseViewHolder(itemView) {
        var img: ImageView
        override fun clear() {

        }

        init {
            img = itemView.findViewById(R.id.img)
        }

        fun bind(position: Int) {
            GenericDownloadManager(
                mList[position].user.profileImage.large,
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