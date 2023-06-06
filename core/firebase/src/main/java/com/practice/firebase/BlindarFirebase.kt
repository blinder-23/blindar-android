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
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

object BlindarFirebase {
    private val TAG = "BlindarFirebase"
    private val auth: FirebaseAuth = Firebase.auth

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

    fun updateCurrentUsername(
        username: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit,
    ) {
        auth.currentUser?.let { user ->
            val profileUpdates = userProfileChangeRequest {
                displayName = username
            }
            user.updateProfile(profileUpdates)
                .addOnSuccessListener {
                    onSuccess()
                }.addOnFailureListener {
                    Log.e(TAG, "Update profile failed", it)
                    onFail()
                }
        }
    }

}