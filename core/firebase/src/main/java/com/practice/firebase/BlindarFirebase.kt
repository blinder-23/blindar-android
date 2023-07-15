package com.practice.firebase

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
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

    suspend fun parseIntentAndSignInWithGoogle(
        intent: Intent,
        onNewUserSignUp: (FirebaseUser) -> Unit,
        onExistingUserLogin: (FirebaseUser) -> Unit,
        onFail: () -> Unit,
    ) {
        try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(intent).await()
            signInWithGoogle(
                idToken = account.idToken,
                onSelectSchool = onNewUserSignUp,
                onExistingUserLogin = onExistingUserLogin,
                onFail = onFail
            )
        } catch (e: ApiException) {
            Log.d(TAG, "parse account fail", e)
            onFail()
        }
    }

    private suspend fun signInWithGoogle(
        idToken: String?,
        onSelectSchool: (FirebaseUser) -> Unit,
        onExistingUserLogin: (FirebaseUser) -> Unit,
        onFail: () -> Unit,
    ) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        val task = try {
            auth.signInWithCredential(firebaseCredential).await()
        } catch (e: CancellationException) {
            null
        }
        val user = task?.user
        val username = user?.displayName
        if (user != null && username != null) {
            val schoolId = getSchoolId(username).value as String?
            if (schoolId == null) {
                // new user register
                tryStoreUsername(
                    username = username,
                    onSuccess = { onSelectSchool(user) },
                    onFail = onFail,
                    updateProfile = false,
                )
            } else {
                // existing user sign in
                Log.d(TAG, "user ${user.uid} school id: $schoolId")
                onExistingUserLogin(user)
            }
        } else {
            Log.e(TAG, "sign in with google fail: $user")
            onFail()
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
        schoolId: Int,
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

    suspend fun getSchoolId(username: String): DataSnapshot {
        return database.child(usersKey).child(username).child(schoolIdKey).get().await()
    }

    private const val usersKey = "users"
    private const val ownerKey = "owner"
    private const val schoolIdKey = "schoolId"

}