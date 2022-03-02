package com.kanyandula.malawi.ui



import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kanyandula.malawi.utils.showSnackbar
import com.kanyandula.malawi.R
import com.kanyandula.malawi.adapters.BlogAdapter
import com.kanyandula.malawi.utils.Resource
import com.kanyandula.malawi.utils.exhaustive
import com.kanyandula.malawi.utils.onQueryTextSubmit
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.button_retry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {


    private val  viewModel: SearchViewModel by viewModels()
    lateinit var  blogAdapter : BlogAdapter



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupRecyclerView()

        blogAdapter.setOnItemClickListener {
            viewModel.addToRecentlyViedBlogs(it)
            val bundle = Bundle().apply {
                putSerializable("blogs",it)
            }
            findNavController().navigate(
                R.id.action_searchFragment_to_blogFragment,
                bundle
            )
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.searchResults.collectLatest { data ->
                val result = data ?: return@collectLatest
                swipe_refresh_layout.isRefreshing = result is Resource.Loading
                result_list.isVisible = !result.data.isNullOrEmpty()
                button_retry.isVisible = result.error != null && result.data.isNullOrEmpty()
                blogAdapter.differ.submitList(result.data){
                    if (viewModel.pendingScrollToTopAfterRefresh){
                        result_list.scrollToPosition(0)
                        viewModel.pendingScrollToTopAfterRefresh = false
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.events.collect{ event ->

                when(event){
                    is SearchViewModel.Event.ShowErrorMessage ->
                        showSnackbar(
                            getString(
                                R.string.could_not_refresh
                            )
                        )

                }.exhaustive

            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.hasCurrentQuery.collect { hasCurrentQuery ->
                text_view_instructions.isVisible = !hasCurrentQuery
                swipe_refresh_layout.isVisible = hasCurrentQuery

                if (!hasCurrentQuery){
                    result_list.isVisible = false
                }
            }
        }




    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView. onQueryTextSubmit(){ query ->
            val searchText = query.trim()
            viewModel.onSearchQuerySubmit(searchText)
            searchView.clearFocus()
        }


    }

    private fun subscribeToObservables(){
        viewModel.blogFeedLiveData.observe(this ){ response ->

            blogAdapter.differ.submitList(response)
        }
    }




    private fun setupRecyclerView() {
        blogAdapter = BlogAdapter()
        result_list.apply {
            adapter = blogAdapter
            layoutManager = LinearLayoutManager(context)

        }

    }



}
