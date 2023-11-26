package com.practice.firebase

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
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

    suspend fun signInWithGoogle(intent: Intent): FirebaseUser? {
        val account = GoogleSignIn.getSignedInAccountFromIntent(intent).await()
        return signInWithGoogle(idToken = account.idToken)
    }

    private suspend fun signInWithGoogle(idToken: String?): FirebaseUser? {
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

    fun signInWithPhoneAuthCredential(
        activity: Activity,
        credential: PhoneAuthCredential,
        onRegisterOrLoginSuccessful: () -> Unit,
        onLoginFail: () -> Unit,
    ) {
        Log.d(TAG, "credential: $credential")
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                val user = task.result?.user
                if (task.isSuccessful && user != null) {
                    onRegisterOrLoginSuccessful()
                } else {
                    Log.e(TAG, "signInWithCredential failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // verification code invalid
                        onLoginFail()
                    }
                }
            }
    }

    fun tryStoreUsername(
        username: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit,
        updateProfile: Boolean = true,
    ) {
        auth.currentUser?.let { user ->
            database.child(usersKey).child(username).child(ownerKey).setValue(user.uid)
                .addOnSuccessListener {
                    if (updateProfile) {
                        tryUpdateCurrentUsername(user, username, onSuccess, onFail)
                    } else {
                        onSuccess()
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "Username owner set failed", it)
                    onFail()
                }
        }
    }

    fun storeUsername(username: String): Task<Void>? = auth.currentUser?.let { user ->
        database.child(usersKey).child(username).child(ownerKey).setValue(user.uid)
    }

    private fun tryUpdateCurrentUsername(
        user: FirebaseUser,
        username: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit,
    ) {
        val profileUpdates = userProfileChangeRequest {
            displayName = username
        }
        user.updateProfile(profileUpdates)
            .addOnSuccessListener {
                Log.d(TAG, "update profile success")
                onSuccess()
            }.addOnFailureListener {
                Log.e(TAG, "Update profile failed", it)
                onFail()
            }
    }

    fun tryUpdateCurrentUserSchoolCode(
        schoolCode: Int,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        val username = auth.currentUser?.displayName
        if (username != null) {
            database.child(usersKey).child(username).child(schoolCodeKey).setValue(schoolCode)
                .addOnSuccessListener {
                    Log.d(TAG, "user $username school set to $schoolCode")
                    onSuccess()
                }
                .addOnFailureListener {
                    Log.e(TAG, "user $username school set fail to $schoolCode")
                    onFail()
                }
        } else {
            Log.e(TAG, "username is null: ${auth.currentUser}")
            onFail()
        }
    }

    suspend fun getschoolCode(username: String): Long? {
        val value =
            database.child(usersKey).child(username).child(schoolCodeKey).get().await().value
        Log.d(TAG, "username $username schoolCode: $value")
        if (value != null) {
            return value as? Long
        }
        val oldValue =
            database.child(usersKey).child(username).child(oldSchoolCodeKey).get().await().value
        Log.d(TAG, "username $username old school code: $oldValue")
        return oldValue as? Long
    }

    private const val usersKey = "users"
    private const val ownerKey = "owner"
    private const val schoolCodeKey = "school_code"
    private const val oldSchoolCodeKey = "schoolCode"

}