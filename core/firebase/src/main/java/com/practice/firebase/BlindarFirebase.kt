package com.practice.firebase

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.AuthResult
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

    suspend fun signInWithGoogle(idToken: String): AuthResult? {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        return try {
            auth.signInWithCredential(firebaseCredential).await()
        } catch (e: CancellationException) {
            null
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
        onExistingUserLogin: () -> Unit,
        onNewUserSignUp: () -> Unit,
        onCodeInvalid: () -> Unit,
    ) {
        Log.d(TAG, "credential: $credential")
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    Log.d(TAG, "signInWithCredential success! ${user?.phoneNumber}")
                    if (user?.displayName != null) {
                        onExistingUserLogin()
                    } else {
                        onNewUserSignUp()
                    }
                } else {
                    Log.e(TAG, "signInWithCredential failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // verification code invalid
                        onCodeInvalid()
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

    fun tryUpdateCurrentUserSchoolId(
        schoolId: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        val username = auth.currentUser?.displayName
        if (username != null) {
            database.child(usersKey).child(username).child(schoolIdKey).setValue(schoolId)
                .addOnSuccessListener {
                    Log.d(TAG, "user $username school set to $schoolId")
                    onSuccess()
                }
                .addOnFailureListener {
                    Log.e(TAG, "user $username school set fail to $schoolId")
                    onFail()
                }
        } else {
            Log.e(TAG, "username is null: ${auth.currentUser}")
            onFail()
        }
    }

    private const val usersKey = "users"
    private const val ownerKey = "owner"
    private const val schoolIdKey = "schoolId"

}