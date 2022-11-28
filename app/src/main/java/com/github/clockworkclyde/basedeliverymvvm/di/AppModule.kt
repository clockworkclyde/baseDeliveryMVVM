package com.github.clockworkclyde.basedeliverymvvm.di

import android.content.Context
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.util.AndroidResourceProvider
import com.github.clockworkclyde.basedeliverymvvm.util.ResourceProvider
import com.mapbox.search.MapboxSearchSdk
import com.mapbox.search.SearchEngine
import com.mapbox.search.SearchEngineSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun resourceProvider(@ApplicationContext context: Context): ResourceProvider =
        AndroidResourceProvider(context)

    @Singleton
    @Provides
    fun provideSearchEngine(resources: ResourceProvider): SearchEngine =
        MapboxSearchSdk.createSearchEngineWithBuiltInDataProviders(
            SearchEngineSettings(resources.getString(R.string.mapbox_public_token))
        )

}