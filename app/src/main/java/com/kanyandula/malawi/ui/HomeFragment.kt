package com.kanyandula.malawi.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kanyandula.malawi.utils.exhaustive
import com.kanyandula.malawi.utils.showSnackbar
import com.kanyandula.malawi.R
import com.kanyandula.malawi.adapters.BlogAdapter
import com.kanyandula.malawi.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {


    private val  viewModel: BlogViewModel by viewModels()
    lateinit var blogAdapter : BlogAdapter



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()


        blogAdapter.setOnItemClickListener {
            viewModel.addToRecentlyViedBlogs(it)
            val bundle = Bundle().apply {
                putSerializable("blogs",it)
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_blogFragment,
                bundle
            )
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.fetchBlogPost.collect{
                val result = it ?: return@collect
                swipe_refresh.isRefreshing = result is Resource.Loading
                list_view.isVisible = !result.data.isNullOrEmpty()
                text_view_error.isVisible = result.error != null && result.data.isNullOrEmpty()
                button_retry.isVisible = result.error != null && result.data.isNullOrEmpty()
                text_view_error.text = getString(
                    R.string.could_not_refresh,
                    result.error?.localizedMessage
                        ?: getString(R.string.unknown_error_occurred)
                )

                blogAdapter.differ.submitList(result.data) {
                    if (viewModel.pendingScrollToTopAfterRefresh){
                        list_view.scrollToPosition(0)
                        viewModel.pendingScrollToTopAfterRefresh = false
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.events.collect{ event ->

                when(event){
                    is BlogViewModel.Event.ShowErrorMessage ->
                        showSnackbar(
                            getString(
                                R.string.could_not_refresh
                            )
                        )

                    else -> {}
                }.exhaustive

            }

        }


        swipe_refresh.setOnRefreshListener {
            viewModel.onManualRefresh()
        }

        button_retry.setOnClickListener {
            viewModel.onManualRefresh()
        }


    }



    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    private fun setupRecyclerView() {
        blogAdapter = BlogAdapter()
        list_view.apply {
            adapter = blogAdapter
            layoutManager = LinearLayoutManager(context)
            (layoutManager as LinearLayoutManager).reverseLayout = true
            (layoutManager as LinearLayoutManager).stackFromEnd = true


        }

    }


}
