package com.kanyandula.malawi.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kanyandula.malawi.R
import com.kanyandula.malawi.adapters.BlogAdapter
import com.kanyandula.malawi.data.models.Blog
import com.kanyandula.malawi.utils.Constants
import com.kanyandula.malawi.utils.Resource
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(R.layout.fragment_home) {



    private val  viewModel: BlogViewModel by activityViewModels()
    lateinit var  blogAdapter: BlogAdapter

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



        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = true
            getBlogs()
        }



        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getResponseUsingLiveData().observe(viewLifecycleOwner, Observer { response ->

                showProgressBar()
                blogAdapter.differ.submitList(response.Blog)
                val totalPages = response.totalResults / Constants.QUERY_PAGE_SIZE + 2
                isLastPage = viewModel.blogPage == totalPages
                if (isLastPage) {
                    list_view.setPadding(0, 0, 0, 0)
                }


            })


        }

    }



    private fun getBlogs(){
        viewModel.getResponseUsingLiveData().observe(viewLifecycleOwner, {
            blogAdapter.differ.submitList(it.Blog)
            swipe_refresh.isRefreshing = false
            val totalPages = it.totalResults / Constants.QUERY_PAGE_SIZE + 2
            isLastPage = viewModel.blogPage == totalPages
            if (isLastPage){
                list_view.setPadding(0, 0, 0, 0)
            }


        })
    }


    private fun getResponseUsingLiveData() {

        viewModel.getResponseUsingLiveData().observe(viewLifecycleOwner, {
            blogAdapter.differ.submitList(it.Blog)
            val totalPages = it.totalResults / Constants.QUERY_PAGE_SIZE + 2
            isLastPage = viewModel.blogPage == totalPages
            if (isLastPage){
                list_view.setPadding(0, 0, 0, 0)
            }


        })
    }


    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }


    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                        isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

    }

    private fun setupRecyclerView() {
        blogAdapter = BlogAdapter()
        list_view.apply {
            adapter = blogAdapter
            layoutManager = LinearLayoutManager(context)
            (layoutManager as LinearLayoutManager).reverseLayout = true
            (layoutManager as LinearLayoutManager).stackFromEnd = true
            addOnScrollListener(this@HomeFragment.scrollListener)

        }

    }



    }