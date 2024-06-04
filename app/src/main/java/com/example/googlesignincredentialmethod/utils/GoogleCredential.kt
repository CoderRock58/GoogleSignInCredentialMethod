package com.example.googlesignincredentialmethod.utils

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.googlesignincredentialmethod.BuildConfig
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

class GoogleCredential {

    private var signInResult: SignInResult? = null

    private fun getRequest(): GetCredentialRequest {
        val signInWithGoogleOption =
            GetSignInWithGoogleOption.Builder(BuildConfig.googleSignInCredential)
                .setNonce(createKeyValue())
                .build()
        return GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()
    }

    fun launchGoogleSignInFlow(
        scope : LifecycleCoroutineScope,
        context: Context,
    ) {
        val credentialManager = CredentialManager.create(context)
        scope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = getRequest(),
                    context = context,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                Log.e("TAG", "onCreate: ${e.message}")
                signInResult?.onError(e.message.toString())
            }
        }
    }

    private fun createKeyValue(): String {
        val key = UUID.randomUUID().toString()
        val keyToBytes = key.toByteArray()
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val message = messageDigest.digest(keyToBytes)
        return message.fold("") { acc: String, byte: Byte ->
            acc + "%02x".format(byte)
        }
    }

    fun setSignInListener(signInResult: SignInResult) {
        this.signInResult = signInResult
    }

    private fun handleSignIn(result: GetCredentialResponse) {

        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        signInResult?.onSuccess(googleIdTokenCredential)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("TAG", "Received an invalid google id token response", e)
                        signInResult?.onError(e.message.toString())
                    }
                } else {
                    signInResult?.onError("Unexpected type of credential")
                }
            }

            else -> {
                signInResult?.onError("Unexpected type of credential")
            }
        }
    }

    interface SignInResult {
        fun onSuccess(result: GoogleIdTokenCredential)
        fun onError(message: String)
    }
}