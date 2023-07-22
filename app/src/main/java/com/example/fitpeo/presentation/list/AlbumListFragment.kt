package com.example.fitpeo.presentation.list

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
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
    lateinit var adapter : ImageListAdapter
    override fun init() {
        setHasOptionsMenu(true)
        viewModel.fetchList()
        adapter = ImageListAdapter()
    }
    private fun hideProgressBar(){
        binding.albumprogressBar.hide()
    }
    private fun showProgressBar(){
        binding.albumprogressBar.show()
    }
    private fun setupRecyclersView(pagingData:PagingData<Album>){
        hideProgressBar()
        binding.albumrv.layoutManager = GridLayoutManager(requireContext(), getColumnCount())
        adapter.setClickListener(this)
        binding.albumrv.adapter = adapter
        lifecycleScope.launch {
            adapter.submitData(pagingData)
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
//        navigateToDetail(any as Album)
        requireContext().getString(R.string.NA_detail).showCustomToast(requireContext(), Toast.LENGTH_LONG)
    }
    private fun navigateToDetail(album: Album){
        val b = Bundle()
        b.putParcelable(ALBUM_OBJ,album)
        findNavController().navigate(R.id.bookDetailFragment,b)
    }
    private fun navigateToFavourite(){
        requireContext().getString(R.string.NA_favourite).showCustomToast(requireContext(), Toast.LENGTH_LONG)
//        findNavController().navigate(R.id.favBookFragment)
    }
    private fun handleSearchView(searchView: SearchView){
        searchView.queryHint = requireContext().getString(R.string.search_album)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank() || newText.length >= 3) {
                    viewModel.setSearchQuery(newText)
                }
                return true
            }
        })
    }
    override fun observeViewModel() {
        performCoroutineTask {
            viewModel.uiStateFlow.observe(viewLifecycleOwner){ viewState->
                when(viewState){
                    is ViewState.Loading ->
                        showProgressBar()
                    is ViewState.Success -> {
                        hideProgressBar()
                        setupRecyclersView(viewState.result)
                    }
                    is ViewState.Failure -> {
                        hideProgressBar()
                        viewState.failMessage.let {
                            it.showCustomToast(requireContext())
                        }
                    }
                }
            }
            viewModel.searchLiveData.observe(viewLifecycleOwner) { viewState ->
                when (viewState) {
                    is ViewState.Loading ->{}
                    is ViewState.Success -> {
                        hideProgressBar()
                        setupRecyclersView(viewState.result)
                    }
                    is ViewState.Failure -> {
                        hideProgressBar()
                        viewState.failMessage.let {
                            it.showCustomToast(requireContext())
                        }
                    }
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu_fragment, menu)

        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as SearchView

        // Set up the SearchView
        handleSearchView(searchView)

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