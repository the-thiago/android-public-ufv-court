package com.ufv.court.core.comments_service.data_remote.inject

import com.ufv.court.core.comments_service.data.data_sourcers.CommentsDataSource
import com.ufv.court.core.comments_service.data_remote.data_sources.CommentsRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CommentsDataRemoteModule {

    @Singleton
    @Binds
    internal abstract fun bindsCommentsDataSource(
        CommentsRemoteDataSourceImpl: CommentsRemoteDataSourceImpl
    ): CommentsDataSource
}
