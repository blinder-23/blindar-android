package com.practice.firebase

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

object BlindarFirebase {
    private const val TAG = "BlindarFirebase"
    private val auth: FirebaseAuth = Firebase.auth
    private val database: DatabaseReference = Firebase.database.reference

    suspend fun signInWithGoogle(idToken: String?): FirebaseUser? {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        val task = try {
            auth.signInWithCredential(firebaseCredential).await()
        } catch (e: CancellationException) {
            null
        }
        return task?.user
    }

    fun getBlindarUser(): BlindarUserStatus {
        val user = auth.currentUser
        return when {
            user != null -> BlindarUserStatus.LoginUser(user)
            else -> BlindarUserStatus.NotLoggedIn
        }
    }

    fun getUserIdOrNull(): String? {
        return when (val user = getBlindarUser()) {
            is BlindarUserStatus.LoginUser -> user.user.uid
            is BlindarUserStatus.NotLoggedIn -> null
        }
    }

    fun signUpWithPhoneNumber(
        activity: Activity,
        phoneNumber: String,
        callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        auth.setLanguageCode("ko")
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(120L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun trySignInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
    ): Task<AuthResult> {
        return auth.signInWithCredential(credential)
    }

    suspend fun tryStoreUsername(username: String) {
        auth.currentUser?.let { user ->
            tryUpdateCurrentUsername(user, username)
        }
    }

    private suspend fun tryUpdateCurrentUsername(
        user: FirebaseUser,
        username: String,
    ) {
        val profileUpdates = userProfileChangeRequest {
            displayName = username
        }
        user.updateProfile(profileUpdates).await()
    }

    suspend fun getSchoolCode(username: String = auth.currentUser?.displayName ?: ""): Long? {
        val value =
            database.child(usersKey).child(username).child(schoolCodeKey).get().await().value
        if (value != null) {
            return value as? Long
        }

        val oldValue =
            database.child(usersKey).child(username).child(oldSchoolCodeKey).get().await().value
        return oldValue as? Long
    }

    suspend fun getSchoolName(username: String = auth.currentUser?.displayName ?: ""): String? {
        val value =
            database.child(usersKey).child(username).child(schoolNameKey).get().await().value
        return value as? String
    }

    fun logout() {
        auth.signOut()
    }

    private const val usersKey = "users"
    private const val ownerKey = "owner"
    private const val schoolCodeKey = "school_code"
    private const val oldSchoolCodeKey = "schoolCode"
    private const val schoolNameKey = "school_name"

}