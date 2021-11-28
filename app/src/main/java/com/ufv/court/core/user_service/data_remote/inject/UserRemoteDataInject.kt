package com.ufv.court.core.user_service.data_remote.inject

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ufv.court.core.user_service.data.data_sources.UserDataSource
import com.ufv.court.core.user_service.data_remote.data_sources.UserRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UserDataRemoteModule {

    @Singleton
    @Binds
    internal abstract fun bindsUserDataSource(
        UserRemoteDataSourceImpl: UserRemoteDataSourceImpl
    ): UserDataSource
}

@Module
@InstallIn(SingletonComponent::class)
internal object UserServiceModule {

    @Provides
    internal fun providesUserService(): FirebaseAuth = Firebase.auth
}
