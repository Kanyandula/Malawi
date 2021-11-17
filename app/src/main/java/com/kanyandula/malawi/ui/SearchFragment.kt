package com.kanyandula.malawi.ui


import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.kanyandula.malawi.R
import com.kanyandula.malawi.adapters.BlogAdapter
import kotlinx.android.synthetic.main.fragment_home.*


class SearchFragment : Fragment(R.layout.fragment_search) {


    lateinit var mSearchText : EditText
    lateinit var mRecyclerView : RecyclerView
    lateinit var  blogAdapter : BlogAdapter

    lateinit var mDatabase : DatabaseReference




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setHasOptionsMenu(true)
        

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {

                    searchView.clearFocus() // hide the keyboard

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })


    }






}