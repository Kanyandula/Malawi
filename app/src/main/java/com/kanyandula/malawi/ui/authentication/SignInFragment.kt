package com.kanyandula.malawi.ui.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.kanyandula.malawi.R
import com.kanyandula.malawi.`object`.ValidationObject
import com.kanyandula.malawi.ui.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val  viewModel: SignInFragmentViewModel by viewModels()




    private lateinit var email: String
    private lateinit var password: String

    private var message = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setObservers()
        viewModel.checkUserId()

    }

    private fun initView() {


        signIn_buttonOk.setOnClickListener() {
            email = signIn_email.text.toString()
            password = signIn_password.text.toString()
            viewModel.signIn(email, password)
        }

        signIn_signUp.setOnClickListener {

        }

    }

    private fun setObservers() {
        viewModel.userId.observe(viewLifecycleOwner) { updateFragment(it) }
        viewModel.loading.observe(viewLifecycleOwner) { updateView(it) }
        viewModel.validationResult.observe(viewLifecycleOwner) { validationResult(it) }
        viewModel.signingInStatus.observe(viewLifecycleOwner) { signingInResult(it) }
    }



    private fun updateFragment(userId: String?) {
        if (userId != "null") {
            goToPostFragment()
        } else {
            signIn_loadingSection.visibility = View.INVISIBLE
            signIn_section.visibility = View.VISIBLE
        }
    }

    private fun goToPostFragment() {
        view?.let { Navigation.findNavController(it).navigate(R.id.action_signInFragment_to_postBlogFragment) }
    }

    private fun toast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    private fun updateView(loading: Boolean) {
        if (loading) {
            signIn_loadingSection.visibility = View.VISIBLE
        } else {
            signIn_loadingSection.visibility = View.INVISIBLE
        }
    }

    private fun validationResult(validationResult: Int) {
        when (validationResult) {
            ValidationObject.EMPTY_VALUES -> message =
                getString(R.string.enter_both_email_and_password)
            ValidationObject.INVALID_EMAIL -> message =
                getString(R.string.your_email_address_is_invalid)
        }
        toast(message)
    }

    private fun signingInResult(signingUpResult: Boolean) {
        if (signingUpResult) goToPostFragment()
        else {
            val message = getString(R.string.failed_to_sign_in)
            toast(message)
        }
    }


}

