package com.kanyandula.malawi.ui



import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.kanyandula.malawi.R
import com.kanyandula.malawi.adapters.BlogArticleLoadStateAdapter
import com.kanyandula.malawi.adapters.BlogArticlePagingAdapter
import com.kanyandula.malawi.databinding.FragmentSearchBinding
import com.kanyandula.malawi.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search)   {


    private val  viewModel: SearchViewModel by viewModels()

    private var currentBinding: FragmentSearchBinding? = null
    private val binding get() = currentBinding!!
    private lateinit var mainBlogAdapter: BlogArticlePagingAdapter



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


         currentBinding = FragmentSearchBinding.bind(view)

        mainBlogAdapter = BlogArticlePagingAdapter(
            onItemClick = {
                viewModel.addToRecentlyViedBlogs(it)
                val bundle = Bundle().apply {
                    putSerializable("blogs", it)
                }
                findNavController().navigate(
                    R.id.action_searchFragment_to_blogFragment,
                    bundle
                )
            },
            onBookmarkClick = { blog ->
                viewModel.onBookMarkClick(blog)
            },


        )



        binding.apply {
            resultList.apply {
                adapter = mainBlogAdapter.withLoadStateFooter(
                    BlogArticleLoadStateAdapter(mainBlogAdapter::retry)
                )
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                itemAnimator?.changeDuration = 0
            }




//            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//                viewModel.searchBlogPost.observe(viewLifecycleOwner) { data ->
//                    Log.d("TAG", "$data")
//                    blogAdapter.submitData(viewLifecycleOwner.lifecycle,data)
//
//                }
//            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.searchResults.collectLatest { data ->
                    Log.d("TAG", "$data")
                    mainBlogAdapter.submitData(data)

                }
            }



            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.hasCurrentQuery.collect { hasCurrentQuery ->
                    textViewInstructions.isVisible = !hasCurrentQuery
                    swipeRefreshLayout.isEnabled = hasCurrentQuery

                    if (!hasCurrentQuery) {
                        resultList.isVisible = false
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                mainBlogAdapter.loadStateFlow
                     .distinctUntilChangedBy { it.source.refresh }
                    .filter { it.source.refresh is LoadState.NotLoading }
                    .collect {
                        if (viewModel.pendingScrollToTopAfterNewQuery) {
                            resultList.scrollToPosition(0)
                            viewModel.pendingScrollToTopAfterNewQuery = false
                        }
                        if (viewModel.pendingScrollToTopAfterRefresh && it.mediator?.refresh is LoadState.NotLoading) {
                            resultList.scrollToPosition(0)
                            viewModel.pendingScrollToTopAfterRefresh = false
                        }
                    }
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                mainBlogAdapter.loadStateFlow
                    .collect { loadState ->
                        when (val refresh = loadState.mediator?.refresh) {
                            is LoadState.Loading -> {
                                textViewError.isVisible = false
                                buttonRetry.isVisible = false
                                swipeRefreshLayout.isRefreshing = true
                                textViewNoResults.isVisible = false

                                resultList.showIfOrInvisible {
                                    !viewModel.newQueryInProgress &&  mainBlogAdapter.itemCount > 0
                                }

                                viewModel.refreshInProgress = true
                                viewModel.pendingScrollToTopAfterRefresh = true
                            }
                            is LoadState.NotLoading -> {
                                textViewError.isVisible = false
                                swipeRefreshLayout.isRefreshing = false
                                resultList.isVisible = mainBlogAdapter.itemCount > 0

                                val noCachedResults =
                                    mainBlogAdapter.itemCount < 1 && loadState.source.append.endOfPaginationReached

                                textViewError.isVisible = noCachedResults
                                buttonRetry.isVisible = noCachedResults

                                viewModel.refreshInProgress = false
                                viewModel.newQueryInProgress = false
                            }
                            is LoadState.Error -> {
                                swipeRefreshLayout.isRefreshing = false
                                textViewNoResults.isVisible = false
                                resultList.isVisible =  mainBlogAdapter.itemCount > 0

                                val noCachedResults =
                                    mainBlogAdapter.itemCount < 1 && loadState.source.append.endOfPaginationReached

                                textViewError.isVisible = noCachedResults
                                buttonRetry.isVisible = noCachedResults

                                val errorMessage = getString(
                                    R.string.could_not_load_search_results,
                                    refresh.error.localizedMessage
                                        ?: getString(R.string.unknown_error_occurred)
                                )
                                textViewError.text = errorMessage

                                if (viewModel.refreshInProgress) {
                                    showSnackbar(errorMessage)
                                }
                                viewModel.refreshInProgress = false
                                viewModel.newQueryInProgress = false
                                viewModel.pendingScrollToTopAfterRefresh = false
                            }
                        }
                    }
            }

            swipeRefreshLayout.setOnRefreshListener {
                mainBlogAdapter.refresh()
            }


        }

        setHasOptionsMenu(true)

    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView. onQueryTextSubmit{ query ->

            viewModel.onSearchQuerySubmit(query)


        }



    }





    override fun onDestroyView() {
        super.onDestroyView()
        binding.resultList.adapter = null
        currentBinding = null
    }



}
