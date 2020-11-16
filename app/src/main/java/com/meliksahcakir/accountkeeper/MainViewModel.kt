package com.meliksahcakir.accountkeeper

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.meliksahcakir.accountkeeper.data.AccountRepository
import com.meliksahcakir.accountkeeper.utils.Event
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class MainViewModel(private val repository: AccountRepository) : ViewModel() {

    private val _navigateToLoginActivity = MutableLiveData<Event<Unit>>()
    val navigateToLoginActivity: LiveData<Event<Unit>> = _navigateToLoginActivity

    private val _navigateToLink = MutableLiveData<Event<Uri>>()
    val navigateToLink: LiveData<Event<Uri>> = _navigateToLink

    fun handleDeepLinkIntent(intent: Intent) {
        viewModelScope.launch {
            try {
                val data = Firebase.dynamicLinks.getDynamicLink(intent).await()
                var link: Uri? = null
                if (data == null) {
                    Timber.d("data: null  link: null")
                } else {
                    link = data.link
                    Timber.d("data: $data  link: ${data.link}")
                }
                handleNavigation(link)
            } catch (e: Exception) {
                e.printStackTrace()
                handleNavigation(null)
            }
        }
    }

    private fun handleNavigation(uri: Uri?) {
        viewModelScope.launch {
            if (repository.getUserId() == "") {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    repository.setUserId(user.uid)
                    repository.refreshAccounts()
                } else {
                    AccountKeeperApplication.unhandledDeepLink = uri
                    _navigateToLoginActivity.value = Event(Unit)
                    return@launch
                }
            }
            val link = uri ?: AccountKeeperApplication.unhandledDeepLink
            AccountKeeperApplication.unhandledDeepLink = null
            link?.let {
                _navigateToLink.value = Event(it)
            }
        }
    }

}