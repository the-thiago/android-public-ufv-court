package com.ufv.court.core.user_service.data_remote.data_sources

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.ufv.court.core.core_common.base.requestWrapper
import com.ufv.court.core.user_service.data.data_sources.UserDataSource
import com.ufv.court.ui_login.register.RegisterError
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class UserRemoteDataSourceImpl @Inject constructor(
    private val service: FirebaseAuth
) : UserDataSource {

    override suspend fun registerUser(email: String, password: String) {
        requestWrapper {
            suspendCoroutine { continuation ->
                service.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(Unit)
                        } else {
                            var exception = Exception()
                            task.exception?.let { firebaseException ->
                                exception = when (firebaseException) {
                                    is FirebaseAuthWeakPasswordException -> RegisterError
                                        .AuthWeakPassword
                                    is FirebaseAuthUserCollisionException -> RegisterError
                                        .AuthUserCollision
                                    is FirebaseAuthInvalidCredentialsException -> RegisterError
                                        .AuthInvalidCredentials
                                    else -> Exception()
                                }
                            }
                            continuation.resumeWithException(exception)
                        }
                    }
            }
        }
    }

    override suspend fun sendEmailVerification() {
        requestWrapper {
            suspendCoroutine { continuation ->
                val user = service.currentUser
                if (user == null) {
                    continuation.resumeWithException(RegisterError.SendEmailVerification)
                } else {
                    user.sendEmailVerification()
                    continuation.resume(Unit)
                }
            }
        }
    }
}