package com.mv.genericdownloader.ui.main.fragment.pinwall.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.mv.genericdownloader.R
import com.mv.genericdownloader.model.response.DataResponse
import com.mv.genericdownloaderlib.core.GenericDownloadManager
import com.mv.genericdownloaderlib.enums.ResourceTypes
import com.mv.genericdownloaderlib.interfaces.IResourceRequestCallBack
import com.mv.genericdownloaderlib.model.BaseResource
import com.mv.genericdownloaderlib.model.ImageResource


class MenuImagesAdapter(var mList: MutableList<DataResponse>) :
    RecyclerView.Adapter<BaseViewHolder>() {
    var selectedPosition = 0
    private val VIEW_TYPE_LOADING = 0
    private val VIEW_TYPE_NORMAL = 1
    private var isLoaderVisible = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        when (viewType) {
            VIEW_TYPE_LOADING -> {
                return ProgressVH(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.row_loading,
                        parent,
                        false
                    )
                )
            }
            VIEW_TYPE_NORMAL -> {
                return MenuImageVH(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.row_images,
                        parent,
                        false
                    )
                )
            }
            else -> return ProgressVH(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.row_loading,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int {
        if (mList.isEmpty()) {
            return 0
        }
        return mList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onbind(position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            if (position == mList.size - 1) VIEW_TYPE_LOADING else VIEW_TYPE_NORMAL
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    fun addLoading() {
        isLoaderVisible = true
        val dataResponse = DataResponse()
        dataResponse.user.profileImage.large =
            "https://images.unsplash.com/profile-fb-1464533812-a91a557e646d.jpg?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=faces\\u0026fit=crop\\u0026h=128\\u0026w=128\\u0026s=512955d67915413e3a20fb8fdbfcdc76"
        mList.add(dataResponse)
        notifyItemInserted(mList.size - 1)
    }

    fun clear() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position = mList.size - 1
        val item = getItem(position)
        mList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItem(position: Int): DataResponse {
        return mList[position]
    }


    fun addItems(postItems: MutableList<DataResponse>) {
        mList.addAll(postItems)
        notifyDataSetChanged()
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

        override fun onbind(position: Int) {
            super.onbind(position)
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
        var progress_bar: ContentLoadingProgressBar
        override fun clear() {

        }

        init {
            progress_bar = itemView.findViewById(R.id.progress_bar)
        }
    }
}