package com.github.clockworkclyde.basedeliverymvvm.layers.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context): Context = context.applicationContext
    
}