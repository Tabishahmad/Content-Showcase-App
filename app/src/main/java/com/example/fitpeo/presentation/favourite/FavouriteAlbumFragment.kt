package com.example.fitpeo.presentation.favourite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fitpeo.R
import com.example.fitpeo.common.ALBUM_OBJ
import com.example.fitpeo.common.hide
import com.example.fitpeo.common.show
import com.example.fitpeo.databinding.FragmentBookFavouriteBinding
import com.example.fitpeo.domain.model.Album
import com.example.fitpeo.presentation.core.base.BaseFragment
import com.example.fitpeo.presentation.list.AlbumListViewModel
import com.example.fitpeo.presentation.list.ImageListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FavouriteAlbumFragment : BaseFragment<AlbumListViewModel,FragmentBookFavouriteBinding>(
    R.layout.fragment_book_favourite
),ImageListAdapter.ItemClickListener {
    override val viewModel: AlbumListViewModel by activityViewModels()

    override fun init() {
        viewModel.fetchList()
    }

    private fun hideProgressBar(){
        binding.progressBar.hide()
    }

    private fun setupRecyclersView(list:List<Album>){
        hideProgressBar()
        if(list.isEmpty()){
            binding.rv.hide()
            binding.nodata.show()
        }else {
            binding.rv.layoutManager = GridLayoutManager(requireContext(), 3)
            binding.rv.setData(list)
            binding.rv.setItemClickListener(this)
        }
    }

    override fun onItemClick(view: View, any: Any, index: Int) {
        navigateToDetail(any as Album)
    }
    private fun navigateToDetail(album: Album){
        val b = Bundle()
        b.putParcelable(ALBUM_OBJ,album)
        findNavController().navigate(R.id.bookDetailFragment,b)
    }

    override fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.getAllFavouriteAlbums().collect{
                setupRecyclersView(it)
            }
        }
    }
}