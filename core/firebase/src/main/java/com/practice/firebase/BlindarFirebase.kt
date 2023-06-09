package com.practice.firebase

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

object BlindarFirebase {
    private const val TAG = "BlindarFirebase"
    private val auth: FirebaseAuth = Firebase.auth
    private val database: DatabaseReference = Firebase.database.reference

    fun addAuthStateListener(listener: FirebaseAuth.AuthStateListener) {
        auth.addAuthStateListener(listener)
    }

    fun launchGoogleLogin(
        context: Context,
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
    ) {
        val googleSignInIntent = createGoogleSignInIntent(context)
    }

    private fun createGoogleSignInIntent(context: Context): Intent {
        return getGoogleSignInClient(context).signInIntent
    }

    private fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        val webClientId = context.getString(R.string.web_client_id)
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, options)
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
        onSuccess: () -> Unit,
        onCodeInvalid: () -> Unit,
    ) {
        Log.d(TAG, "credential: $credential")
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    Log.d(TAG, "signInWithCredential success! ${user?.phoneNumber}")
                    onSuccess()
                } else {
                    Log.e(TAG, "signInWithCredential failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // verification code invalid
                        onCodeInvalid()
                    }
                }
            }
    }

    fun tryUpdateCurrentUsername(
        username: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit,
    ) {
        auth.currentUser?.let { user ->
            database.child(usersKey).child(username).child(ownerKey).setValue(user.uid)
                .addOnSuccessListener {
                    tryUpdateCurrentUsername(user, username, onSuccess, onFail)
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