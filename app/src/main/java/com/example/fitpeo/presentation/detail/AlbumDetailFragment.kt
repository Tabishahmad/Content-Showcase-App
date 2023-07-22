package com.example.fitpeo.presentation.detail

import androidx.fragment.app.viewModels
import com.example.fitpeo.R
import com.example.fitpeo.common.*
import com.example.fitpeo.databinding.FragmentBookDetailBinding
import com.example.fitpeo.domain.model.Album
import com.example.fitpeo.presentation.core.base.BaseFragment
import com.example.fitpeo.presentation.list.AlbumListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumDetailFragment : BaseFragment<AlbumListViewModel, FragmentBookDetailBinding>(
    R.layout.fragment_book_detail
) {
    override val viewModel: AlbumListViewModel by viewModels()
    lateinit var album: Album

    override fun initWithBinding() {
        try {
            album = requireNotNull(arguments?.getParcelable(ALBUM_OBJ)) { "Missing album object argument" }
            binding.album = album
            handleResponseSuccess(album)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun hideProgressBar(){
        binding.progressBar.hide()
    }
    private fun showProgressBar(){
        binding.progressBar.show()
    }

    private fun handleResponseSuccess(album: Album) {

        setFavoriteAlbumResource(album)
        setText(album.name)
        setImage(album.posterImage)
        hideProgressBar()
    }

    private fun setText(string: String?) {
        binding.textView.setText(string)
    }
    private fun setImage(string: String?){
        binding.imageView.setByName(requireContext(),string)
    }

    private fun setFavoriteAlbumResource(album: Album) {
        viewModel.isFavoriteAlbum(album) { boolean ->
            println("handleFav " + boolean)
            val imageRes = if (boolean) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            binding.favoriteButton.setImageResource(imageRes)
        }
    }

}