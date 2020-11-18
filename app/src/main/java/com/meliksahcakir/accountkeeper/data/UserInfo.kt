package com.meliksahcakir.accountkeeper.data

import androidx.annotation.Keep
import com.google.firebase.firestore.Exclude
import java.util.*

@Keep
data class UserInfo(
    val username: String = "",
    val email: String = "",
    @get:Exclude
    var uid: String = "",
    var usernameLowerCase: String = ""
) {
    init {
        usernameLowerCase = username.toLowerCase(Locale.getDefault())
    }
}