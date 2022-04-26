package com.kanyandula.malawi.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kanyandula.malawi.utils.exhaustive
import com.kanyandula.malawi.utils.showSnackbar
import com.kanyandula.malawi.R
import com.kanyandula.malawi.adapters.BlogAdapter
import com.kanyandula.malawi.databinding.FragmentHomeBinding
import com.kanyandula.malawi.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import java.lang.ref.WeakReference

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val  viewModel: HomeViewModel by viewModels()
    private var currentBinding: FragmentHomeBinding? = null
    private val binding get() = currentBinding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentBinding = FragmentHomeBinding.bind(view)

        val blogAdapter = BlogAdapter(
            onItemClick = {
                viewModel.addToRecentlyViedBlogs(it)
                val bundle = Bundle().apply {
                    putSerializable("blogs", it)
                }
                findNavController().navigate(
                    R.id.action_homeFragment_to_blogFragment,
                    bundle
                )
            },
            onBookmarkClick = { blog ->
                viewModel.onBookmarkClick(blog)

            },




        )

        blogAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        binding.apply {
            listView.apply {
                adapter = blogAdapter
                layoutManager = LinearLayoutManager(requireContext())
                //layoutManager = LinearLayoutManager(context)
                (layoutManager as LinearLayoutManager).reverseLayout = true
                (layoutManager as LinearLayoutManager).stackFromEnd = true
                setHasFixedSize(true)
                itemAnimator?.changeDuration = 0
            }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.fetchBlogPost.collect {
                val result = it ?: return@collect
                swipeRefresh.isRefreshing = result is Resource.Loading
                listView.isVisible = !result.data.isNullOrEmpty()
                textViewError.isVisible = result.error != null && result.data.isNullOrEmpty()
                buttonRetry.isVisible = result.error != null && result.data.isNullOrEmpty()
                textViewError.text = getString(
                    R.string.could_not_refresh,
                    result.error?.localizedMessage
                        ?: getString(R.string.unknown_error_occurred)
                )

                Log.d("TAG", "${result.data}")
                println("${result.data}")

                blogAdapter.submitList(result.data) {
                    if (viewModel.pendingScrollToTopAfterRefresh) {
                        listView.scrollToPosition(0)
                        viewModel.pendingScrollToTopAfterRefresh = false
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.events.collect { event ->

                when (event) {
                    is HomeViewModel.Event.ShowErrorMessage ->
                        showSnackbar(
                            getString(
                                R.string.could_not_refresh
                            )
                        )
                }.exhaustive

            }

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



    override fun onDestroyView() {
        super.onDestroyView()
        currentBinding = null
    }


}
