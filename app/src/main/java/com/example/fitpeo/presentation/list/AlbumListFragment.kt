package com.example.fitpeo.presentation.list

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fitpeo.R
import com.example.fitpeo.common.*
import com.example.fitpeo.databinding.FragmentBookListBinding
import com.example.fitpeo.domain.model.Album
import com.example.fitpeo.presentation.core.ViewState
import com.example.fitpeo.presentation.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AlbumListFragment : BaseFragment<AlbumListViewModel,FragmentBookListBinding>(
    R.layout.fragment_book_list
),ImageListAdapter.ItemClickListener {
    override val viewModel: AlbumListViewModel by activityViewModels()

    override fun init() {
        setHasOptionsMenu(true)
//        viewModel.fetchList()
    }
    private fun hideProgressBar(){
        binding.albumprogressBar.hide()
    }
    private fun showProgressBar(){
        binding.albumprogressBar.show()
    }
    private fun setupRecyclersView(list:List<Album>){
        hideProgressBar()
        binding.albumrv.layoutManager = GridLayoutManager(requireContext(), getColumnCount())
        var adapter = ImageListAdapter()
        lifecycleScope.launch {
            viewModel.fetchList().collect {
                adapter.submitData(it)
            }
        }
    }
    private fun getColumnCount(): Int {
        return if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            5 // Number of columns in landscape orientation
        } else {
            3 // Number of columns in portrait orientation
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
    private fun navigateToFavourite(){
        findNavController().navigate(R.id.favBookFragment)
    }
    private fun addSearchView(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                TODO("Not yet implemented")
            }
        })
    }
    override fun observeViewModel() {
        hideProgressBar()
        binding.albumrv.layoutManager = GridLayoutManager(requireContext(), getColumnCount())
        var adapter = ImageListAdapter()
        binding.albumrv.adapter = adapter
        lifecycleScope.launch {
            viewModel.fetchList().collect {pagingData->
                adapter.submitData(pagingData)
            }
        }
//        performCoroutineTask {
//            viewModel.getviewStateFlow().collect{ viewState->
//                when(viewState){
//                    is ViewState.Loading ->
//                        showProgressBar()
//                    is ViewState.Success -> {
//                        setupRecyclersView(viewState.result)
//                    }
//                    is ViewState.Failure -> {
//                        hideProgressBar()
//                        viewState.failMessage.let {
//                            it.showCustomToast(requireContext())
//                        }
//                    }
//                }
//            }
//        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite -> {
                navigateToFavourite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}