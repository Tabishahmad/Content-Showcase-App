package com.example.fitpeo.presentation.list

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitpeo.domain.model.Album

class ImageRecyclerview : RecyclerView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        layoutManager = GridLayoutManager(context, 3)
        adapter = ImageListAdapter()
    }

    private fun getMAdapter(): ImageListAdapter {
        return adapter as ImageListAdapter
    }

    fun setData(list: List<Album>) {
        getMAdapter().updateList(list)
    }


    fun setItemClickListener(listener: ImageListAdapter.ItemClickListener) {
        getMAdapter().setClickListener(listener)
    }

}


