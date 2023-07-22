package com.example.fitpeo.common

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.fitpeo.R
import com.example.fitpeo.data.repository.model.AlbumDTO
import com.example.fitpeo.domain.model.Album
import com.squareup.picasso.Picasso


//fun ImageView.loadImageWithGlide(context: Context, imageURL: String?) {
//    Glide.with(context)
//        .load(imageURL)
//        .thumbnail(Glide.with(context).load(R.raw.load))
//        .into(this)
//}
fun ImageView.loadImageWithPicasso(context: Context, imageURL: String?) {
    Picasso.get()
        .load(imageURL)
        .placeholder(R.drawable.loading)
        .error(R.drawable.error_img)
        .into(this);
}
fun ImageView.byName(context: Context,resourceName: String?){
    val resourceId = getResourceIdByName(context, resourceName?.removeJpgExtension())


    if (resourceId != 0) {
        this.setImageResource(resourceId)
    }
}
private fun getResourceIdByName(context: Context, resourceName: String?): Int {
    return context.resources.getIdentifier(
        resourceName,
        "drawable",
        context.packageName
    )
}
private fun String.removeJpgExtension(): String {
    return this.replace(".jpg", "")
}


fun String.showCustomToast(context: Context, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(context, this, duration).show()
}
fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}
fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}
//fun List<AlbumDTO>?.toAlbumList(): List<Album> {
//    return this?.map { dto ->
//        Album(dto.albumId, dto.id, dto.title, dto.url, dto.thumbnailUrl)
//    } ?: emptyList()
//}