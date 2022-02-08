package com.ufv.court.core.comments_service.data.inject

import com.ufv.court.core.comments_service.data.repositories.CommentsRepositoryImpl
import com.ufv.court.core.comments_service.domain.repositories.CommentsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CommentsRepositoryModule {

    @Singleton
    @Binds
    internal abstract fun bindsCommentsRepository(
        commentsRepositoryImpl: CommentsRepositoryImpl
    ): CommentsRepository
}
