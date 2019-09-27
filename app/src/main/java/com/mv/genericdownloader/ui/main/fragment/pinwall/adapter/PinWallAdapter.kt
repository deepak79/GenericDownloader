package com.mv.genericdownloader.ui.main.fragment.pinwall.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.mv.genericdownloader.R
import com.mv.genericdownloader.model.response.DataResponse
import com.mv.genericdownloader.ui.detail.DetailActivity
import com.mv.genericdownloaderlib.core.GenericDownloadManager
import com.mv.genericdownloaderlib.enums.ResourceTypes
import com.mv.genericdownloaderlib.interfaces.IResourceRequestCallBack
import com.mv.genericdownloaderlib.model.BaseResource
import com.mv.genericdownloaderlib.model.ImageResource


class PinWallAdapter(var mList: MutableList<DataResponse>) :
    RecyclerView.Adapter<BaseViewHolder>() {
    var selectedPosition = 0
    private val VIEW_TYPE_LOADING = 0
    private val VIEW_TYPE_NORMAL = 1
    private var isLoaderVisible = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        when (viewType) {
            VIEW_TYPE_LOADING -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.row_loading,
                    parent,
                    false
                )
                return ProgressVH(view)
            }
            VIEW_TYPE_NORMAL -> {
                return PinWallVH(
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

    fun clear() {
        mList.clear()
        notifyDataSetChanged()
    }

    /**
     * ViewHolder for PinWalll
     * */
    inner class PinWallVH(itemView: View) : BaseViewHolder(itemView) {
        var img: ImageView
        var progress_bar: ContentLoadingProgressBar
        var btn_cancel_request: Button
        var btn_retry_request: Button
        override fun clear() {

        }

        init {
            itemView.setOnClickListener {
                setOnItemClick(adapterPosition)
            }
            img = itemView.findViewById(R.id.img)
            progress_bar = itemView.findViewById(R.id.progress_bar)
            btn_cancel_request = itemView.findViewById(R.id.btn_cancel_request)
            btn_retry_request = itemView.findViewById(R.id.btn_retry_request)
        }

        private fun setOnItemClick(adapterPosition: Int) {
            selectedPosition = adapterPosition
            val intent = Intent(itemView.context!!, DetailActivity::class.java)
            intent.putExtra("URL", mList[adapterPosition].user.profileImage.large)
            itemView.context.startActivity(intent)
            notifyDataSetChanged()
        }

        override fun onbind(position: Int) {
            super.onbind(position)
            val mGenericDownloadManager = GenericDownloadManager(
                mList[position].user.profileImage.large,
                ResourceTypes.IMAGE, object : IResourceRequestCallBack<BaseResource> {
                    override fun onSuccess(data: BaseResource) {
                        progress_bar.visibility = GONE
                        img.setImageBitmap((data as ImageResource).getBitmap())
                    }

                    override fun onFailure(error: String?) {
                        progress_bar.visibility = GONE
                        Log.e("@@@@", "Failure $error")
                    }
                })
            btn_cancel_request.setOnClickListener {
                progress_bar.visibility = GONE
                mGenericDownloadManager.onCancel()
            }
            btn_retry_request.setOnClickListener {
                progress_bar.visibility = VISIBLE
                mGenericDownloadManager.onRetry()
            }
        }
    }

    /**
     * ViewHolder for Progressbar
     * */
    inner class ProgressVH(itemView: View) : BaseViewHolder(itemView) {

        var progress_bar: ContentLoadingProgressBar
        override fun clear() {

        }

        init {
            progress_bar = itemView.findViewById(R.id.progress_bar)
        }
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position = mList.size - 1
        mList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addLoading() {
        isLoaderVisible = true
        mList.add(DataResponse())
        notifyItemInserted(mList.size - 1)
    }

    fun addItems(postItems: MutableList<DataResponse>) {
        mList.addAll(postItems)
        removeLoading()
        notifyDataSetChanged()
    }
}