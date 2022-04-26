package com.kanyandula.malawi.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kanyandula.malawi.R
import com.kanyandula.malawi.adapters.BlogAdapter
import com.kanyandula.malawi.databinding.FragmentBookMarkBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.lang.ref.WeakReference

@AndroidEntryPoint
class BookMarkFragment : Fragment(R.layout.fragment_book_mark) {

    private val viewModel: BookMarkViewModel by viewModels()

    private var currentBinding: FragmentBookMarkBinding? = null
    private val binding get() = currentBinding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentBinding = FragmentBookMarkBinding.bind(view)


        val blogAdapter = BlogAdapter(



            onItemClick = {
                viewModel.addToRecentlyViedBlogs(it)
                val bundle = Bundle().apply {
                    putSerializable("blogs", it)
                }
                findNavController().navigate(
                    R.id.action_bookMarkFragment_to_blogFragment,
                    bundle
                )
            },

            onBookmarkClick  = { blog ->

                viewModel.onBookMarkClick(blog)
            },


        )

        blogAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        binding.apply {
            listView.apply {
                adapter = blogAdapter
                layoutManager = LinearLayoutManager(requireContext())
                //layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.bookmarks.collect {
                    val bookmarks = it ?: return@collect

                    blogAdapter.submitList(bookmarks)
                    textViewNoBookmarks.isVisible = bookmarks.isEmpty()
                    listView.isVisible = bookmarks.isNotEmpty()

                }
            }
        }


        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_bookmarks, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.action_delete_all_bookmarks -> {
                viewModel.onDeleteAllBookmarks()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        currentBinding = null
    }



}


