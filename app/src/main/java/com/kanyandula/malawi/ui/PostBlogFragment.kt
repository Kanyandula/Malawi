package com.kanyandula.malawi.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kanyandula.malawi.R
import com.kanyandula.malawi.adapters.BlogAdapter
import com.kanyandula.malawi.adapters.UserPostAdapter
import com.kanyandula.malawi.data.Blog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_post_blog.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


@AndroidEntryPoint
class PostBlogFragment : Fragment(R.layout.fragment_post_blog),  OnItemClickAction  {

    private val  viewModel: PostBlogViewModel by viewModels()
    lateinit var userPostAdapter : UserPostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        viewModel.fetchMovies()
        setHasOptionsMenu(true)

        setupFloatingActionButton()
    }


    private fun setupFloatingActionButton() {
        addButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_postBlogFragment_to_addBlogFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sign_out, menu)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {

            R.id.menuItem_sign_out -> {
                viewModel.signOut()
                view?.findNavController()?.navigate(R.id.action_postBlogFragment_to_signInFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }





        @OptIn(ExperimentalCoroutinesApi::class)
        private fun setObservers() {
        viewModel.blogs.observe(viewLifecycleOwner) { updateView(it) }
    }

    private fun updateView(blogs: List<Blog>) {
        movies_loadingSection.visibility = View.INVISIBLE
        if (blogs.isEmpty()) {
            blog_list.visibility = View.INVISIBLE
            movies_noMoviesLabel.visibility = View.VISIBLE
        } else {
            if (viewModel.itemDeleted.value == true) {
                userPostAdapter.dataSetChanged(blogs)
            } else {
                blog_list.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                userPostAdapter = UserPostAdapter(requireContext(), blogs, this)
                blog_list.adapter = userPostAdapter
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onItemLongClicked(id: String?) {
        viewModel.deleteMovie(id)
    }

}

interface OnItemClickAction {

    fun onItemLongClicked(id: String?)
}