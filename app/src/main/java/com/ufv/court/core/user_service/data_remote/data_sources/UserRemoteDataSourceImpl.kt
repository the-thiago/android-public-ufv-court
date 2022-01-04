package com.ufv.court.core.user_service.data_remote.data_sources

import android.net.Uri
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.ufv.court.core.core_common.base.requestWrapper
import com.ufv.court.core.user_service.data.data_sources.UserDataSource
import com.ufv.court.core.user_service.data_remote.mapper.toModel
import com.ufv.court.core.user_service.data_remote.request.RegisterUser
import com.ufv.court.core.user_service.domain.model.User
import com.ufv.court.ui_login.forgotpassword.ForgotPasswordError
import com.ufv.court.ui_login.login.LoginError
import com.ufv.court.ui_login.register.RegisterCredentialsError
import com.ufv.court.ui_profile.changepasword.ChangePasswordError
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class UserRemoteDataSourceImpl @Inject constructor() : UserDataSource {

    override suspend fun registerUser(user: RegisterUser) {
        requestWrapper {
            suspendCoroutine { continuation ->
                Firebase.auth.createUserWithEmailAndPassword(user.email, user.password)
                    .addOnCompleteListener { task ->
                        val profileInfo = UserProfileChangeRequest.Builder().apply {
                            displayName = user.name
                        }.build()
                        if (task.isSuccessful) {
                            val newUser = task.result?.user
                            newUser?.updateProfile(profileInfo)
                            Firebase.auth.signInWithEmailAndPassword(user.email, user.password)
                                .addOnCompleteListener {
                                    if (user.photo != Uri.EMPTY) {
                                        FirebaseStorage.getInstance().reference
                                            .child("images/user/${Firebase.auth.currentUser?.uid}/profile")
                                            .putFile(user.photo).addOnCompleteListener {
                                                continuation.resume(Unit)
                                            }
                                    } else {
                                        continuation.resume(Unit)
                                    }
                                }
                        } else {
                            var exception = Exception()
                            task.exception?.let { firebaseException ->
                                exception = when (firebaseException) {
                                    is FirebaseAuthWeakPasswordException -> RegisterCredentialsError
                                        .AuthWeakPassword
                                    is FirebaseAuthUserCollisionException -> RegisterCredentialsError
                                        .AuthUserCollision
                                    is FirebaseAuthInvalidCredentialsException -> RegisterCredentialsError
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
                val user = Firebase.auth.currentUser
                if (user == null) {
                    continuation.resumeWithException(RegisterCredentialsError.SendEmailVerification)
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
                Firebase.auth.signInWithEmailAndPassword(email, password)
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
                val user = Firebase.auth.currentUser
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
                val user = Firebase.auth.currentUser
                Firebase.storage.reference.child("images/user/${user?.uid}/profile")
                    .downloadUrl.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(
                                Firebase.auth.currentUser.toModel(image = task.result.toString())
                            )
                        } else {
                            continuation.resume(
                                Firebase.auth.currentUser.toModel(image = "")
                            )
                        }
                    }
            }
        }
    }

    override suspend fun logout() {
        requestWrapper {
            Firebase.auth.signOut()
        }
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String) {
        requestWrapper {
            suspendCoroutine { continuation ->
                val user = Firebase.auth.currentUser
                val credential = EmailAuthProvider.getCredential(
                    user?.email ?: "", oldPassword
                )
                user?.reauthenticate(credential)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        user.updatePassword(newPassword).addOnCompleteListener {
                            if (task.isSuccessful) {
                                continuation.resume(Unit)
                            } else {
                                var exception = Exception()
                                task.exception?.let { firebaseException ->
                                    exception = when (firebaseException) {
                                        is FirebaseAuthWeakPasswordException ->
                                            ChangePasswordError.AuthWeakPassword
                                        is FirebaseAuthInvalidUserException ->
                                            ChangePasswordError.NoUserFound
                                        else -> Exception()
                                    }
                                }
                                continuation.resumeWithException(exception)
                            }
                        }
                    } else {
                        var exception = Exception()
                        task.exception?.let { firebaseException ->
                            exception = when (firebaseException) {
                                is FirebaseAuthInvalidCredentialsException ->
                                    ChangePasswordError.InvalidCredentials
                                is FirebaseAuthInvalidUserException ->
                                    ChangePasswordError.NoUserFound
                                else -> Exception()
                            }
                        }
                        continuation.resumeWithException(exception)
                    }
                } ?: run {
                    continuation.resumeWithException(Exception())
                }
            }
        }
    }

    override suspend fun resetPassword(email: String) {
        requestWrapper {
            suspendCoroutine { continuation ->
                Firebase.auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Unit)
                    } else {
                        var exception = Exception()
                        task.exception?.let { firebaseException ->
                            exception = when (firebaseException) {
                                is FirebaseAuthInvalidUserException ->
                                    ForgotPasswordError.NoUserFound
                                else -> Exception()
                            }
                        }
                        continuation.resumeWithException(exception)
                    }
                }
            }
        }
    }
}
