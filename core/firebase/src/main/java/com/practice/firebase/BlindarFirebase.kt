package com.practice.firebase

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

object BlindarFirebase {
    private const val TAG = "BlindarFirebase"
    private val auth: FirebaseAuth = Firebase.auth
    private val database: DatabaseReference = Firebase.database.reference

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

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
        if (user == null) {
            // Google login fail
            Log.e(TAG, "sign in with google fail: $user")
            onFail()
        } else if (getUserDataState(user) == UserDataState.ALL_FILLED) {
            // Existing user login
            Log.d(TAG, "user ${user.uid} login")
            onExistingUserLogin(user)
        } else {
            // New user registers
            tryStoreUsername(
                username = user.displayName!!,
                onSuccess = { onSelectSchool(user) },
                onFail = onFail,
                updateProfile = false,
            )
        }
    }

    suspend fun getUserDataState(user: FirebaseUser): UserDataState {
        Log.d(TAG, "user display name: ${user.displayName}")
        if (user.displayName.isNullOrEmpty()) return UserDataState.USERNAME_MISSING

        val schoolCode = getschoolCode(user.displayName!!)
        Log.d(TAG, "user school id: $schoolCode")
        return if (schoolCode == null) UserDataState.SCHOOL_NOT_SELECTED else UserDataState.ALL_FILLED
    }

    fun getBlindarUser(): BlindarUser {
        val user = auth.currentUser
        return when {
            user != null -> BlindarUser.LoginUser(user)
            else -> BlindarUser.NotLoggedIn
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
        onUsernameNotSet: () -> Unit,
        onSchoolNotSelected: () -> Unit,
        onNewUserSignUp: () -> Unit,
        onLoginFail: () -> Unit,
    ) {
        Log.d(TAG, "credential: $credential")
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                val user = task.result?.user
                if (task.isSuccessful && user != null) {
                    scope.launch {
                        Log.d(TAG, "signInWithCredential success! ${user.phoneNumber}")
                        val userDataState = getUserDataState(user)
                        when (userDataState) {
                            UserDataState.USERNAME_MISSING -> onUsernameNotSet()
                            UserDataState.SCHOOL_NOT_SELECTED -> onSchoolNotSelected()
                            UserDataState.ALL_FILLED -> onExistingUserLogin()
                            else -> onNewUserSignUp()
                        }
                    }
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