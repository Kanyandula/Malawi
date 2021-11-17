package com.kanyandula.malawi.ui


import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kanyandula.malawi.R
import com.kanyandula.malawi.adapters.BlogAdapter
import com.kanyandula.malawi.utils.Resource
import com.kanyandula.malawi.utils.Status
import kotlinx.android.synthetic.main.fragment_book_mark.*


class BookMarkFragment : Fragment(R.layout.fragment_book_mark) {


     private val newViewModel: NewViewModel by activityViewModels()
    lateinit var  blogAdapter: BlogAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        blogAdapter = BlogAdapter()



    }







}


