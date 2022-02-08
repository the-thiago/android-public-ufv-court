package com.ufv.court.core.comments_service.data_remote.data_sources

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.ufv.court.core.comments_service.data.data_sourcers.CommentsDataSource
import com.ufv.court.core.comments_service.domain.model.ScheduleComments
import com.ufv.court.core.core_common.base.requestWrapper
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class CommentsRemoteDataSourceImpl @Inject constructor() : CommentsDataSource {

    private val commentsPath = "comments"

    override suspend fun getComments(eventId: String): ScheduleComments {
        return requestWrapper {
            suspendCoroutine { continuation ->
                Firebase.firestore.collection(commentsPath)
                    .whereEqualTo("eventId", eventId)
                    .limit(1)
                    .get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val documents = task.result?.documents
                            if (documents != null && documents.size > 0) {
                                val comments = documents[0].toObject<ScheduleComments>()
                                if (comments == null) {
                                    continuation.resumeWithException(Exception())
                                } else {
                                    continuation.resume(comments.copy(id = documents[0].id))
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

    override suspend fun sendComment(eventComments: ScheduleComments) {
        return requestWrapper {
            suspendCoroutine { continuation ->
                Firebase.firestore.collection(commentsPath).document(eventComments.id)
                    .set(eventComments)
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
