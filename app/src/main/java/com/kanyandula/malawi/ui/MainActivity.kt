package com.kanyandula.malawi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.kanyandula.malawi.R
import com.kanyandula.malawi.repository.BlogRepository
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {


    lateinit var viewModel: BlogViewModel

    private lateinit var  navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val blogRepository = BlogRepository()
        val viewModelProviderFactory = BlogViewModelProviderFactory(application, blogRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(BlogViewModel::class.java)

       bottomNavigationView.setupWithNavController(blogNavHostFragment.findNavController())

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.blogNavHostFragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController )



    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}