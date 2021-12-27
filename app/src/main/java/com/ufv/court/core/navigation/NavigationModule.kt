package com.ufv.court.core.navigation

import com.ufv.court.core.core_common.base.ApplicationScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NavigationModule {

    @Singleton
    @Provides
    fun providesNavigationManager(
        @ApplicationScope applicationScope: CoroutineScope
    ): NavigationManager {
        return NavigationManager(applicationScope)
    }
}
