package com.kanyandula.malawi.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kanyandula.malawi.R
import com.kanyandula.malawi.adapters.BlogAdapter


class BookMarkFragment : Fragment(R.layout.fragment_book_mark) {


    private lateinit var  blogAdapter: BlogAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        blogAdapter = BlogAdapter()


    }









}


