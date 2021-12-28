package com.ufv.court.core.user_service.data_remote.data_sources

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import com.ufv.court.core.core_common.base.requestWrapper
import com.ufv.court.core.user_service.data.data_sources.UserDataSource
import com.ufv.court.core.user_service.data_remote.mapper.toModel
import com.ufv.court.core.user_service.domain.model.User
import com.ufv.court.ui_login.login.LoginError
import com.ufv.court.ui_login.register.RegisterError
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class UserRemoteDataSourceImpl @Inject constructor(
    private val authService: FirebaseAuth
) : UserDataSource {

    override suspend fun registerUser(email: String, password: String) {
        requestWrapper {
            suspendCoroutine { continuation ->
                authService.createUserWithEmailAndPassword(email, password)
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
                val user = authService.currentUser
                if (user == null) {
                    continuation.resumeWithException(RegisterError.SendEmailVerification)
                } else {
                    user.sendEmailVerification()
                    continuation.resume(Unit)
                }
            }
        }
    }

    override suspend fun login(email: String, password: String) {
        requestWrapper {
            suspendCoroutine { continuation ->
                authService.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(Unit)
                        } else {
                            var exception = Exception()
                            task.exception?.let { firebaseException ->
                                exception = when (firebaseException) {
                                    is FirebaseAuthInvalidCredentialsException ->
                                        LoginError.InvalidCredentials
                                    is FirebaseAuthInvalidUserException -> LoginError.NoUserFound
                                    else -> Exception()
                                }
                            }
                            continuation.resumeWithException(exception)
                        }
                    }
            }
        }
    }

    override suspend fun isEmailVerified(): Boolean {
        return requestWrapper {
            suspendCoroutine { continuation ->
                val user = authService.currentUser
                if (user == null) {
                    continuation.resumeWithException(Exception())
                } else {
                    continuation.resume(user.isEmailVerified)
                }
            }
        }
    }

    override suspend fun getCurrentUser(): User {
        return requestWrapper {
            suspendCoroutine { continuation ->
                continuation.resume(authService.currentUser.toModel())
            }
        }
    }
}