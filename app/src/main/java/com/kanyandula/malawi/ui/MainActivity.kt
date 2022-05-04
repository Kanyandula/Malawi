package com.kanyandula.malawi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.kanyandula.malawi.R
import com.kanyandula.malawi.utils.ConnectionLiveData
import com.kanyandula.malawi.utils.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var  navController: NavController

    @Inject
    lateinit var connectivityManager: com.kanyandula.malawi.utils.ConnectivityManager

    override fun onStart() {
        super.onStart()
        connectivityManager.registerConnectionObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterConnectionObserver(this)
    }
    private lateinit var  connectionLiveData: ConnectionLiveData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connectionLiveData = ConnectionLiveData(this)

//        connectionLiveData.observe(this) { isNetworkAvailable ->
//
//            if (isNetworkAvailable) {
//
//                Snackbar.make(View(this@MainActivity), "No network", Snackbar.LENGTH_SHORT).show()
//            } else {
//
//                Snackbar.make(View(this@MainActivity), "Success", Snackbar.LENGTH_SHORT).show()
//            }
//
//
//        }
//






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