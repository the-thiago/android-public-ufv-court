package com.ufv.court.core.schedule_service.data_remote.data_sources

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.ufv.court.core.core_common.base.requestWrapper
import com.ufv.court.core.schedule_service.data.data_sources.ScheduleDataSource
import com.ufv.court.core.schedule_service.domain.model.ScheduleModel
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class ScheduleRemoteDataSourceImpl @Inject constructor() : ScheduleDataSource {

    private val schedulesPath = "schedules"

    override suspend fun createSchedule(schedule: ScheduleModel) {
        requestWrapper {
            suspendCoroutine { continuation ->
                Firebase.firestore.collection(schedulesPath).add(schedule).addOnCompleteListener {
                    if (it.isSuccessful) {
                        continuation.resume(Unit)
                    } else {
                        continuation.resumeWithException(it.exception ?: Exception())
                    }
                }
            }
        }
    }

    override suspend fun getScheduleByDay(timeInMillis: Long): List<ScheduleModel> {
        return requestWrapper {
            suspendCoroutine { continuation ->
                Firebase.firestore.collection(schedulesPath)
                    .whereEqualTo("timeInMillis", timeInMillis)
                    .get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val schedules = task.result?.documents?.mapNotNull {
                                val schedule = it.toObject<ScheduleModel>()
                                schedule?.copy(id = it.id)
                            } ?: listOf()
                            continuation.resume(schedules)
                        } else {
                            continuation.resumeWithException(task.exception ?: Exception())
                        }
                    }
            }
        }
    }

    override suspend fun getScheduledByUser(): List<ScheduleModel> {
        return requestWrapper {
            suspendCoroutine { continuation ->
                Firebase.firestore.collection(schedulesPath)
                    .whereEqualTo("ownerId", Firebase.auth.currentUser?.uid ?: "x")
                    .orderBy("timeInMillis")
                    .orderBy("hourStart")
                    .get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val schedules = task.result?.documents?.mapNotNull {
                                val schedule = it.toObject<ScheduleModel>()
                                schedule?.copy(id = it.id)
                            } ?: listOf()
                            continuation.resume(schedules)
                        } else {
                            continuation.resumeWithException(task.exception ?: Exception())
                        }
                    }
            }
        }
    }

    override suspend fun getScheduledAsParticipant(): List<ScheduleModel> {
        return requestWrapper {
            suspendCoroutine { continuation ->
                Firebase.firestore.collection(schedulesPath)
                    .whereArrayContains("participantsId", Firebase.auth.currentUser?.uid ?: "x")
                    .orderBy("timeInMillis")
                    .orderBy("hourStart")
                    .get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val schedules = task.result?.documents?.mapNotNull {
                                val schedule = it.toObject<ScheduleModel>()
                                schedule?.copy(id = it.id)
                            } ?: listOf()
                            continuation.resume(schedules)
                        } else {
                            continuation.resumeWithException(task.exception ?: Exception())
                        }
                    }
            }
        }
    }

    override suspend fun getSchedule(id: String): ScheduleModel {
        return requestWrapper {
            suspendCoroutine { continuation ->
                Firebase.firestore.collection(schedulesPath).document(id)
                    .get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            val schedule = document?.toObject<ScheduleModel>() ?: ScheduleModel()
                            continuation.resume(schedule.copy(id = document?.id ?: ""))
                        } else {
                            continuation.resumeWithException(task.exception ?: Exception())
                        }
                    }
            }
        }
    }

    override suspend fun updateSchedule(id: String, newSchedule: ScheduleModel) {
        return requestWrapper {
            suspendCoroutine { continuation ->
                Firebase.firestore.collection(schedulesPath).document(id).set(newSchedule)
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

    override suspend fun getSchedulesFreeSpace(): List<ScheduleModel> {
        return requestWrapper {
            suspendCoroutine { continuation ->
                Firebase.firestore.collection(schedulesPath)
                    .whereEqualTo("hasFreeSpace", true)
                    .orderBy("timeInMillis")
                    .orderBy("hourStart")
                    .get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val schedules = task.result?.documents?.mapNotNull {
                                val schedule = it.toObject<ScheduleModel>()
                                schedule?.copy(id = it.id)
                            } ?: listOf()
                            continuation.resume(schedules)
                        } else {
                            continuation.resumeWithException(task.exception ?: Exception())
                        }
                    }
            }
        }
    }
}
