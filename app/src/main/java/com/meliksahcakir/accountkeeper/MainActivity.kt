package com.meliksahcakir.accountkeeper

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.meliksahcakir.accountkeeper.login.LoginActivity
import com.meliksahcakir.accountkeeper.utils.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private val sharedPreferenceListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == PREF_NIGHT_MODE) {
                AppCompatDelegate.setDefaultNightMode(Preferences.nightMode)
            }
        }

    private lateinit var viewModel: MainViewModel
    private lateinit var host: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        host = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        bottomNavigationView.setupWithNavController(host.navController)
        host.navController.addOnDestinationChangedListener(this)
        Preferences.preferences.registerOnSharedPreferenceChangeListener(sharedPreferenceListener)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory((application as AccountKeeperApplication).accountRepository)
        ).get(MainViewModel::class.java)
        if (intent != null) {
            viewModel.handleDeepLinkIntent(intent)
        }
        viewModel.navigateToLoginActivity.observe(this, EventObserver {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        })
        viewModel.navigateToLink.observe(this, EventObserver {
            host.navController.navigate(R.id.personalAccountsFragment)
            host.navController.navigate(R.id.findUsersFragment)
            host.navController.safeNavigateToDeepLink(it)
        })
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            viewModel.handleDeepLinkIntent(it)
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.personalAccountsFragment -> mainFab.setImageResource(R.drawable.ic_add)
            R.id.friendAccountsFragment -> mainFab.setImageResource(R.drawable.ic_add)
            R.id.findAccountsFragment -> mainFab.setImageResource(R.drawable.ic_back)
            R.id.findUsersFragment -> mainFab.setImageResource(R.drawable.ic_search)
            R.id.settingsFragment -> mainFab.setImageResource(R.drawable.ic_home)
            R.id.addUpdateAccountFragment -> mainFab.setImageResource(R.drawable.ic_save)
        }
    }

    override fun onDestroy() {
        Preferences.preferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceListener)
        super.onDestroy()
    }
}

