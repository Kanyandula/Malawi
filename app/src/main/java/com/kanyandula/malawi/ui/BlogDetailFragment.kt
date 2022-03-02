package com.kanyandula.malawi.ui


import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.kanyandula.malawi.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_blog_detail.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


@AndroidEntryPoint
class BlogDetailFragment : Fragment(R.layout.fragment_blog_detail) {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val  viewModel: BlogViewModel by viewModels()

     private val args by navArgs<BlogDetailFragmentArgs>()
     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
          super.onViewCreated(view, savedInstanceState)


         favoriteImage.setOnClickListener {

         }

              val blogs = args.blogs
         Glide.with(this).load(blogs.image).into(imageView)
          textTitle.apply {
              text  = blogs.title
          }


          textDescription.apply {
              text = blogs.desc
              movementMethod = ScrollingMovementMethod()

          }




     }

}