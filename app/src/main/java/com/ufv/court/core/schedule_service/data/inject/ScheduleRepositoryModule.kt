package com.ufv.court.core.schedule_service.data.inject

import com.ufv.court.core.schedule_service.data.repositories.ScheduleRepositoryImpl
import com.ufv.court.core.schedule_service.domain.repositories.ScheduleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ScheduleRepositoryModule {

    @Singleton
    @Binds
    internal abstract fun bindsScheduleRepository(
        scheduleRepositoryImpl: ScheduleRepositoryImpl
    ): ScheduleRepository
}
