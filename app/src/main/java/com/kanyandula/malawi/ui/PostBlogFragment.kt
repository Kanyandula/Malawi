package com.kanyandula.malawi.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.kanyandula.malawi.R
import com.kanyandula.malawi.adapters.BlogAdapter
import com.kanyandula.malawi.adapters.UserPostAdapter
import com.kanyandula.malawi.data.Blog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_post_blog.*


@AndroidEntryPoint
class PostBlogFragment : Fragment(R.layout.fragment_post_blog),  OnItemClickAction  {

    private val  viewModel: PostBlogViewModel by viewModels()
    lateinit var userPostAdapter : UserPostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        viewModel.fetchMovies()

        addButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_signInFragment_to_postBlogFragment)
        }
    }



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

    override fun onItemLongClicked(id: String?) {
        viewModel.deleteMovie(id)
    }

}

interface OnItemClickAction {

    fun onItemLongClicked(id: String?)
}