//package com.example.fitpeo.presentation.list
//
//import androidx.recyclerview.widget.DiffUtil
//import com.example.fitpeo.domain.model.Album
//
//class RecycleViewDiffUtil(
//    private val oldList: List<Album>,
//    private val newList: List<Album>
//) : DiffUtil.Callback() {
//
//    override fun getOldListSize(): Int {
//        return oldList.size
//    }
//
//    override fun getNewListSize(): Int {
//        return newList.size
//    }
//
//    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        // Compare unique identifiers of the items
//        return oldList[oldItemPosition].name == newList[newItemPosition].name &&
//                oldList[oldItemPosition].id == newList[newItemPosition].id &&
//                oldList[oldItemPosition].posterImage == newList[newItemPosition].posterImage
//    }
//
//    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        // Compare the contents of the items
//        return oldList[oldItemPosition] == newList[newItemPosition]
//    }
//}
