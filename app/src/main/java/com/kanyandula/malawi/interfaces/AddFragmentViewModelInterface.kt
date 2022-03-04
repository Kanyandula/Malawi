package com.kanyandula.malawi.interfaces

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import com.kanyandula.malawi.R
import com.kanyandula.malawi.ui.PostBlogFragment
import com.kanyandula.malawi.utils.Constants.EMPTY_VALUES


interface AddFragmentViewModelInterface {

    fun initView()
    fun setObservers()

    fun updateView(loading: Boolean, loadingSection: ConstraintLayout) {
        if (loading) {
            loadingSection.visibility = View.VISIBLE
        } else {
            loadingSection.visibility = View.INVISIBLE
        }
    }

    fun validationResult(validationResult: Int, context: Context, message: String) {
        when (validationResult) {
            EMPTY_VALUES -> Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun addingToDatabaseResult(
        addingToDatabaseResult: Boolean,
        context: Context,
        message: String,
        category: String,
        view: View,


    ) {
        if (addingToDatabaseResult) {
            Toast.makeText(context, message, Toast.LENGTH_LONG)
                .show()
            Navigation.findNavController(view).navigate(R.id.action_addBlogFragment_to_postBlogFragment)
        } else {
            Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG).show()
        }
    }
}