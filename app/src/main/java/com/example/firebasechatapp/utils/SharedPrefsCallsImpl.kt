package com.example.firebasechatapp.utils

import android.content.SharedPreferences
import com.example.firebasechatapp.utils.Constants.DARK_MODE
import com.example.firebasechatapp.utils.Constants.USERNAME
import com.example.firebasechatapp.utils.Constants.USER_MAIL
import com.example.firebasechatapp.utils.Constants.USER_PHOTOT
import com.example.firebasechatapp.utils.Constants.USER_UID

class SharedPrefsCallsImpl(val prefs: SharedPreferences) : SharedPrefsCalls {

    override fun getUserUid(): String? {
        return prefs.getString(USER_UID, null)
    }

    override fun storeUserUid(id: String) {
        prefs.edit().putString(USER_UID, id).apply()
    }

    override fun resetUserUid() {
        prefs.edit().putString(USER_UID, null).apply()
    }

    override fun isNightModeOn(): Boolean {
        return prefs
            .getBoolean(
                DARK_MODE, false
            )
    }

    override fun storeUserDetails(name: String, email: String, photo: String) {
        if (name.isNotEmpty()) {
            prefs.edit().putString(USERNAME, name).apply()
        }
        if (email.isNotEmpty()) {
            prefs.edit().putString(USER_MAIL, email).apply()
        }
        if (photo.isNotEmpty()) {
            prefs.edit().putString(USER_PHOTOT, photo).apply()
        }
    }

    override fun getUserName(): String? {
        return prefs.getString(USERNAME, null)
    }

    override fun getUserEmail(): String? {
        return prefs.getString(USER_MAIL, null)
    }

    override fun getUserPhoto(): String? {
        return prefs.getString(USER_PHOTOT, null)
    }

    override fun changeNightMode(mode: Boolean) {
        prefs.edit().putBoolean(DARK_MODE, mode).apply()
    }


}