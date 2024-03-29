package com.example.firebasechatapp.ui.app_components

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.firebasechatapp.R
import com.example.firebasechatapp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var mainProgressBar: ProgressBar
    lateinit var navController: NavController
    lateinit var toolbar: Toolbar
    private val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.main_toolbar)
        model.toggleMode()

        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.chatsFragment,
                R.id.usersFragment,
                R.id.settingsFragment,
            )
        )
        navController.addOnDestinationChangedListener(this)
        // setupActionBarWithNavController(navController, appBarConfiguration)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
        mainProgressBar = findViewById(R.id.main_progressBar)

        model.activateMode.observe(this, EventObserver {
            if (it) {
                AppCompatDelegate
                    .setDefaultNightMode(
                        AppCompatDelegate
                            .MODE_NIGHT_YES
                    )
            } else {
                AppCompatDelegate
                    .setDefaultNightMode(
                        AppCompatDelegate
                            .MODE_NIGHT_NO
                    )
            }
        })
    }

    fun showGlobalProgressBar(show: Boolean) {
        if (show) mainProgressBar.visibility = View.VISIBLE
        else mainProgressBar.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        model.goOffline()
    }

    override fun onStart() {
        super.onStart()
        model.goOnline()
    }

    override fun onSupportNavigateUp(): Boolean {
        //return navController.navigateUp() || super.onSupportNavigateUp()
        // onBackPressed()
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        model.checkIfNewMessageReceived()

        model.isNewMessage.observeForever {
            if (it != null && it) {
                nav_view.getOrCreateBadge(R.id.chatsFragment)
            } else {
                nav_view.removeBadge(R.id.chatsFragment)
            }
        }

        when (destination.id) {
            R.id.firstFragment -> {
                nav_view.visibility = View.GONE
                toolbar.visibility = View.GONE
            }
            R.id.chatsFragment -> {
                nav_view.visibility = View.VISIBLE
                toolbar.visibility = View.VISIBLE

            }
            R.id.usersFragment, R.id.settingsFragment -> {
                nav_view.visibility = View.VISIBLE
                toolbar.visibility = View.VISIBLE
            }
            else -> {
                nav_view.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        model.isNewMessage.removeObservers(this)
    }
}