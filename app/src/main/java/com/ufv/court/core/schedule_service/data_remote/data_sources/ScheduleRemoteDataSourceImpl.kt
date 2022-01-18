package com.ufv.court.core.schedule_service.data_remote.data_sources

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

    override suspend fun getScheduleByDayUseCase(
        day: String,
        month: String,
        year: String
    ): List<ScheduleModel> {
        return requestWrapper {
            suspendCoroutine { continuation ->
                Firebase.firestore.collection(schedulesPath)
                    .whereEqualTo("day", day)
                    .whereEqualTo("month", month)
                    .whereEqualTo("year", year)
                    .get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val schedules = task.result?.documents?.mapNotNull {
                                it.toObject<ScheduleModel>()
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
