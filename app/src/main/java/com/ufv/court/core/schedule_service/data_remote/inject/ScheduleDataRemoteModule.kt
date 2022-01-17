package com.ufv.court.core.schedule_service.data_remote.inject

import com.ufv.court.core.schedule_service.data.data_sources.ScheduleDataSource
import com.ufv.court.core.schedule_service.data_remote.data_sources.ScheduleRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ScheduleDataRemoteModule {

    @Singleton
    @Binds
    internal abstract fun bindsScheduleDataSource(
        ScheduleRemoteDataSourceImpl: ScheduleRemoteDataSourceImpl
    ): ScheduleDataSource
}
