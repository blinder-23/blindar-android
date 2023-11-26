package com.practice.user

import com.practice.firebase.BlindarFirebase
import com.practice.firebase.BlindarUserStatus
import javax.inject.Inject

class RegisterStateManager @Inject constructor() {
    suspend fun getUserState(): UserRegisterState {
        val blindarUser = BlindarFirebase.getBlindarUser()
        if (blindarUser !is BlindarUserStatus.NotLoggedIn) {
            return UserRegisterState.NOT_LOGGED_IN
        }

        val user = blindarUser as BlindarUserStatus.LoginUser
        return when {
            user.user.displayName == null -> UserRegisterState.USERNAME_MISSING
            BlindarFirebase.getschoolCode(user.user.displayName!!) == null -> UserRegisterState.SCHOOL_NOT_SELECTED
            else -> UserRegisterState.ALL_FILLED
        }
    }
}