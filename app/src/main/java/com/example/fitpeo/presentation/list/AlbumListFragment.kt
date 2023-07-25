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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitpeo.R
import com.example.fitpeo.common.*
import com.example.fitpeo.databinding.FragmentBookListBinding
import com.example.fitpeo.domain.model.Album
import com.example.fitpeo.presentation.core.ViewState
import com.example.fitpeo.presentation.core.base.BaseFragment
import com.example.fitpeo.presentation.favourite.SearchSuggestionsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AlbumListFragment : BaseFragment<AlbumListViewModel,FragmentBookListBinding>(
    R.layout.fragment_book_list
),ImageListAdapter.ItemClickListener {
    override val viewModel: AlbumListViewModel by activityViewModels()
    lateinit var adapter : ImageListAdapter
    lateinit var currentList : ArrayList<Album>
    private val myCoroutineScope: CoroutineScope by lazy { lifecycleScope }
    lateinit var searchView: SearchView
    private var isQuerySetProgrammatically = false

    override fun init() {
        setHasOptionsMenu(true)
        viewModel.fetchList()
        adapter = ImageListAdapter()
        currentList = ArrayList()
    }
    private fun hideProgressBar(){
        binding.albumprogressBar.hide()
    }
    private fun showProgressBar(){
        binding.albumprogressBar.show()
    }
    private fun setupRecyclersView(pagingData: PagingData<Album>) {
        hideProgressBar()
        binding.albumrv.layoutManager = GridLayoutManager(requireContext(), getColumnCount())
        adapter.setClickListener(this)
        binding.albumrv.adapter = adapter

        myCoroutineScope.launch {
            adapter.loadStateFlow.collect { loadStates ->
                // Check if the initial load state is REFRESH to ensure the data is loaded
                if (loadStates.refresh is LoadState.NotLoading) {
                    currentList = adapter.snapshot().items as ArrayList<Album>
                }
            }
        }

        myCoroutineScope.launch {
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
    override fun initWithBinding(){
        handleShowHintList()
    }
    private fun handleShowHintList(){
        val adapter = SearchSuggestionsAdapter(){
            isQuerySetProgrammatically = true
            searchView.setQuery(it,false)
            binding.suggestionsRecyclerView.visibility = View.GONE
        }
        binding.suggestionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.suggestionsRecyclerView.adapter = adapter

        searchSuggestionsLiveData.observe(viewLifecycleOwner){suggestions->
            if (suggestions.isNotEmpty()) {
                // Show the RecyclerView with search suggestions
                binding.suggestionsRecyclerView.visibility = View.VISIBLE
                adapter.clear()
                adapter.submitList(suggestions)
            } else {
                // Hide the RecyclerView if there are no suggestions
                binding.suggestionsRecyclerView.visibility = View.GONE
            }
        }
    }
    private val searchSuggestionsLiveData = MutableLiveData<List<String>>()

    fun findSearchSuggestions(queryText: String) {
        if (queryText.isNullOrBlank()) {
            binding.suggestionsRecyclerView.visibility = View.GONE
        } else {
            val matchingAlbums = currentList.filter { album ->
                album.name.contains(queryText, ignoreCase = true)
            }
            val albumNames = matchingAlbums.map { it.name }.distinct()
            searchSuggestionsLiveData.postValue(albumNames)
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
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {


                if (newText.isNullOrBlank() || newText.length >= 3) {
                    viewModel.setSearchQuery(newText)
                }
                if (isQuerySetProgrammatically) {
                    // The query was set programmatically, do not process further
                    isQuerySetProgrammatically = false
                    return true
                }
                if (newText != null) {
                    findSearchSuggestions(newText)
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
        searchView = searchItem.actionView as SearchView

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