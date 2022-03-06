package com.kanyandula.malawi.ui.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kanyandula.malawi.R
import com.kanyandula.malawi.data.Blog
import com.kanyandula.malawi.interfaces.AddFragmentViewModelInterface
import com.kanyandula.malawi.utils.Constants.BLOG_REF
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_blog.*
import kotlinx.android.synthetic.main.fragment_post_blog.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddBlogFragment : Fragment(R.layout.fragment_add_blog) , AddFragmentViewModelInterface{

    @OptIn(ExperimentalCoroutinesApi::class)
    private val  viewModel: AddBlogFragmentViewModel by viewModels()

    @Inject
    lateinit var databaseAuth: FirebaseAuth


    private lateinit var loadingSection: ConstraintLayout
    private lateinit var noTitleMessage: String
    private lateinit var blogAddedMessage: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObservers()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun initView() {

        loadingSection = addMovie_loadingSection
        blogAddedMessage = "Blog Added"
        addBlog_Button.setOnClickListener {
           val title = inputTittle.text.toString()
           val desc = inputDescription.text.toString()
           val date = Calendar.getInstance().time.toString()
           val userName = databaseAuth.currentUser.toString()
           val uid = databaseAuth.uid.toString()

           val blog = Blog(title,desc,date, userName, uid)

           viewModel.addToDatabase(blog)
       }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun setObservers() {
       viewModel.loading.observe(viewLifecycleOwner){ updateView(it, loadingSection  )}
        viewModel.validationResult.observe(
            viewLifecycleOwner
        ){ validationResult(it, requireContext(), noTitleMessage) }
        viewModel.addingToDatabaseResult.observe(
            viewLifecycleOwner
        ){
            view?.let { it1 ->
                addingToDatabaseResult(
                    it,
                    requireContext(),
                    blogAddedMessage,
                    BLOG_REF,
                    it1
                )
            }

        }
    }


}