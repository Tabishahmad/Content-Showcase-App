package com.example.fitpeo.presentation.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fitpeo.common.byName
import com.example.fitpeo.common.loadImageWithPicasso
import com.example.fitpeo.data.repository.model.AlbumDTO
import com.example.fitpeo.databinding.ImageRowBinding
import com.example.fitpeo.domain.model.Album

class ImageListAdapter : PagingDataAdapter<Album,ImageListAdapter.ImageHolder>(ItemDiffCallback()) {

    private var layoutInflater: LayoutInflater? = null
    var imageClickListener: ItemClickListener? = null

    class ImageHolder(val b: ImageRowBinding) : RecyclerView.ViewHolder(b.root) {
       fun setImage(thumbnailURL:String?) {
            b.iv.byName(b.iv.context,thumbnailURL)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val b = ImageRowBinding.inflate(layoutInflater!!, parent, false)
        return ImageHolder(b)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val book = getItem(position)
        println("pageNumber $position")
        holder.setImage(book?.posterImage)
//        holder.b.card.setOnClickListener {
//            imageClickListener?.onItemClick(it, book, position)
//        }
    }

//    override fun getItemCount(): Int {
//        return items.size
//    }


    fun setClickListener(listener: ItemClickListener) {
        this.imageClickListener = listener
    }

    fun interface ItemClickListener {
        fun onItemClick(view: View, any: Any, index: Int)
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }
    }
    fun updateList(newList: List<Album>) {
//        val diffCallback = RecycleViewDiffUtil(items, newList)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//        items = newList
//        diffResult.dispatchUpdatesTo(this)

    }
}