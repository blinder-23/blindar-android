package com.practice.firebase

import com.google.firebase.auth.FirebaseUser

sealed class BlindarUserStatus {
    class LoginUser(val user: FirebaseUser): BlindarUserStatus()
    object NotLoggedIn: BlindarUserStatus()
}
