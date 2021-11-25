package com.kanyandula.malawi.ui


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.kanyandula.malawi.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BlogFragment : Fragment(R.layout.fragment_blog) {
     //lateinit var viewModel: BlogViewModel
     //private val args by navArgs<BlogFragmentArgs>()

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
          super.onViewCreated(view, savedInstanceState)


             // val blogs = args.blogs
//          Glide.with(this).load(blogs.image).into(imageView)
//          textTitle.text = blogs.title
//          textDescription.text = blogs.desc
//          textDescription.movementMethod = ScrollingMovementMethod()


     }

}