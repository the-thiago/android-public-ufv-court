package com.ufv.court.core.user_service.data.inject

import com.ufv.court.core.user_service.data.repositories.UserRepositoryImpl
import com.ufv.court.core.user_service.domain.repositories.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UserRepositoryModule {

    @Singleton
    @Binds
    internal abstract fun bindsUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}
