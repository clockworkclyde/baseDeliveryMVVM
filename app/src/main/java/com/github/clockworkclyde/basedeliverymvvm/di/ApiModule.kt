package com.github.clockworkclyde.basedeliverymvvm.di

import com.github.clockworkclyde.network.api.BuildConfig
import com.github.clockworkclyde.network.api.interactor.DishesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val RETROFIT_TIMEOUT = 10L

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

//    @Provides
//    @Singleton
//    fun provideRemoteDataSource(
//        api: FoodApi
//    ): RemoteDataSource = RemoteDataSourceImpl(api)

    @Provides
    fun provideOkHttpBuilder(): OkHttpClient.Builder = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(httpLoggingInterceptor)
        }

        readTimeout(RETROFIT_TIMEOUT, TimeUnit.SECONDS)
        connectTimeout(RETROFIT_TIMEOUT, TimeUnit.SECONDS)
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(
        httpBuilder: OkHttpClient.Builder,
        retrofitBuilder: Retrofit.Builder
    ) = retrofitBuilder
        .client(httpBuilder.build())
        .build()

    @Provides
    @Singleton
    fun provideFoodApi(retrofit: Retrofit): DishesApi = retrofit.create(DishesApi::class.java)

}