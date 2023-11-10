package com.practice.firebase

import com.google.firebase.auth.FirebaseUser

sealed class BlindarUser {
    class LoginUser(val user: FirebaseUser): BlindarUser()
    object NotLoggedIn: BlindarUser()
}
