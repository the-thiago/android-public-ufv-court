package com.ufv.court.core.user_service.data_remote.data_sources

import com.google.firebase.auth.FirebaseAuth
import com.ufv.court.core.core_common.base.requestWrapper
import com.ufv.court.core.user_service.data.data_sources.UserDataSource
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
                            continuation.resumeWithException(task.exception ?: Exception())
                        }
                    }
            }
        }
    }
}