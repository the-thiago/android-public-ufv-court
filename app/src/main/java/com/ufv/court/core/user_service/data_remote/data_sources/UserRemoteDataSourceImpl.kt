package com.ufv.court.core.user_service.data_remote.data_sources

import android.net.Uri
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.ufv.court.core.core_common.base.requestWrapper
import com.ufv.court.core.user_service.data.data_sources.UserDataSource
import com.ufv.court.core.user_service.data_remote.request.RegisterUser
import com.ufv.court.core.user_service.domain.model.UserModel
import com.ufv.court.ui_login.forgotpassword.ForgotPasswordError
import com.ufv.court.ui_login.login.LoginError
import com.ufv.court.ui_login.register.RegisterCredentialsError
import com.ufv.court.ui_profile.changepasword.ChangePasswordError
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class UserRemoteDataSourceImpl @Inject constructor() : UserDataSource {

    private val usersPath = "users"

    override suspend fun registerUser(userRequest: RegisterUser) {
        requestWrapper {
            suspendCoroutine { continuation ->
                Firebase.auth.createUserWithEmailAndPassword(
                    userRequest.email,
                    userRequest.password
                ).addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        val newUser = authTask.result?.user
                        val userModel = UserModel(
                            id = newUser?.uid ?: "",
                            name = userRequest.name,
                            email = userRequest.email,
                            image = "",
                            phone = userRequest.phone
                        )
                        if (userRequest.photo != Uri.EMPTY) {
                            FirebaseStorage.getInstance().reference
                                .child("images/user/${Firebase.auth.currentUser?.uid}/profile")
                                .putFile(userRequest.photo)
                                .addOnCompleteListener { photoTask ->
                                    if (photoTask.isSuccessful) {
                                        photoTask.result?.storage?.downloadUrl?.addOnCompleteListener { urlTask ->
                                            if (urlTask.isSuccessful) {
                                                Firebase.firestore.collection(usersPath)
                                                    .add(userModel.copy(image = urlTask.result.toString()))
                                                    .addOnCompleteListener { userStoreTask ->
                                                        if (userStoreTask.isSuccessful) {
                                                            continuation.resume(Unit)
                                                        } else {
                                                            continuation.resumeWithException(
                                                                userStoreTask.exception
                                                                    ?: Exception()
                                                            )
                                                        }
                                                    }
                                            } else {
                                                Firebase.firestore.collection(usersPath)
                                                    .add(userModel)
                                                    .addOnCompleteListener { userStoreTask ->
                                                        if (userStoreTask.isSuccessful) {
                                                            continuation.resume(Unit)
                                                        } else {
                                                            continuation.resumeWithException(
                                                                userStoreTask.exception
                                                                    ?: Exception()
                                                            )
                                                        }
                                                    }
                                            }
                                        }
                                    } else {
                                        Firebase.firestore.collection(usersPath)
                                            .add(userModel)
                                            .addOnCompleteListener { userStoreTask ->
                                                if (userStoreTask.isSuccessful) {
                                                    continuation.resume(Unit)
                                                } else {
                                                    continuation.resumeWithException(
                                                        userStoreTask.exception
                                                            ?: Exception()
                                                    )
                                                }
                                            }
                                    }
                                }
                        } else {
                            Firebase.firestore.collection(usersPath)
                                .add(userModel)
                                .addOnCompleteListener { userStoreTask ->
                                    if (userStoreTask.isSuccessful) {
                                        continuation.resume(Unit)
                                    } else {
                                        continuation.resumeWithException(
                                            userStoreTask.exception ?: Exception()
                                        )
                                    }
                                }
                        }
                    } else {
                        var exception = Exception()
                        authTask.exception?.let { firebaseException ->
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

    override suspend fun getCurrentUser(): UserModel {
        return requestWrapper {
            suspendCoroutine { continuation ->
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (uid?.isEmpty() == true) {
                    continuation.resumeWithException(Exception())
                } else {
                    Firebase.firestore.collection(usersPath)
                        .whereEqualTo("id", uid)
                        .limit(1)
                        .get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val usersDocs = task.result?.documents
                                if (usersDocs?.isNotEmpty() == true) {
                                    val user = usersDocs[0].toObject<UserModel>() ?: UserModel()
                                    continuation.resume(user)
                                } else {
                                    continuation.resumeWithException(Exception())
                                }
                            } else {
                                continuation.resumeWithException(task.exception ?: Exception())
                            }
                        }
                }
            }
        }
    }

    override suspend fun getUsers(ids: List<String>): List<UserModel> {
        return requestWrapper {
            suspendCoroutine { continuation ->
                Firebase.firestore.collection(usersPath)
                    .whereIn("id", ids)
                    .orderBy("name")
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val usersDocs = task.result?.documents
                            if (usersDocs?.isNotEmpty() == true) {
                                val users = usersDocs.map {
                                    it.toObject<UserModel>() ?: UserModel()
                                }
                                continuation.resume(users)
                            } else {
                                continuation.resumeWithException(Exception())
                            }
                        } else {
                            continuation.resumeWithException(task.exception ?: Exception())
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

    override suspend fun updateUser(user: UserModel, imageUri: Uri?) {
        return requestWrapper {
            suspendCoroutine { continuation ->
                Firebase.firestore.collection(usersPath).whereEqualTo("id", user.id)
                    .limit(1)
                    .get()
                    .addOnCompleteListener { task -> // getting user data id
                        if (task.isSuccessful) {
                            val documents = task.result?.documents
                            if (documents?.isNotEmpty() == true) {
                                if (imageUri != null) {
                                    FirebaseStorage.getInstance().reference
                                        .child("images/user/${Firebase.auth.currentUser?.uid}/profile")
                                        .putFile(imageUri)
                                        .addOnCompleteListener { photoTask ->
                                            if (photoTask.isSuccessful) {
                                                photoTask.result?.storage?.downloadUrl?.addOnCompleteListener { urlTask ->
                                                    if (urlTask.isSuccessful) {
                                                        Firebase.firestore.collection(usersPath)
                                                            .document(documents[0].id)
                                                            .set(user.copy(image = urlTask.result.toString()))
                                                            .addOnCompleteListener { updateTask ->
                                                                if (updateTask.isSuccessful) {
                                                                    continuation.resume(Unit)
                                                                } else {
                                                                    continuation.resumeWithException(
                                                                        task.exception
                                                                            ?: Exception()
                                                                    )
                                                                }
                                                            }
                                                    } else {
                                                        Firebase.firestore.collection(usersPath)
                                                            .document(documents[0].id)
                                                            .set(user)
                                                            .addOnCompleteListener { updateTask ->
                                                                if (updateTask.isSuccessful) {
                                                                    continuation.resume(Unit)
                                                                } else {
                                                                    continuation.resumeWithException(
                                                                        task.exception
                                                                            ?: Exception()
                                                                    )
                                                                }
                                                            }
                                                    }
                                                }
                                            } else {
                                                Firebase.firestore.collection(usersPath)
                                                    .document(documents[0].id)
                                                    .set(user).addOnCompleteListener { updateTask ->
                                                        if (updateTask.isSuccessful) {
                                                            continuation.resume(Unit)
                                                        } else {
                                                            continuation.resumeWithException(
                                                                task.exception ?: Exception()
                                                            )
                                                        }
                                                    }
                                            }
                                        }
                                } else {
                                    Firebase.firestore.collection(usersPath)
                                        .document(documents[0].id)
                                        .set(user).addOnCompleteListener { updateTask ->
                                            if (updateTask.isSuccessful) {
                                                continuation.resume(Unit)
                                            } else {
                                                continuation.resumeWithException(
                                                    task.exception ?: Exception()
                                                )
                                            }
                                        }
                                }
                            } else {
                                continuation.resumeWithException(task.exception ?: Exception())
                            }
                        } else {
                            continuation.resumeWithException(task.exception ?: Exception())
                        }
                    }
            }
        }
    }
}
