package com.github.clockworkclyde.basedeliverymvvm.di

import com.github.clockworkclyde.basedeliverymvvm.data.datasource.DeliveryLocalDataSourceImpl
import com.github.clockworkclyde.basedeliverymvvm.data.repository.DeliveryRepository
import com.github.clockworkclyde.network.api.DeliveryRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideDeliveryRepository(
        localDataSource: DeliveryLocalDataSourceImpl,
        remoteDataSource: DeliveryRemoteDataSourceImpl
    ): DeliveryRepository =
        DeliveryRepository(localDataSource = localDataSource, remoteDataSource = remoteDataSource)
}