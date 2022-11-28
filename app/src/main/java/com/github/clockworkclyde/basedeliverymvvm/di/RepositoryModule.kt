package com.github.clockworkclyde.basedeliverymvvm.di

import com.github.clockworkclyde.basedeliverymvvm.data.datasource.DishesLocalDataSourceImpl
import com.github.clockworkclyde.basedeliverymvvm.data.repository.UserAddressRepository
import com.github.clockworkclyde.basedeliverymvvm.data.repository.AuthRepository
import com.github.clockworkclyde.basedeliverymvvm.data.repository.DishesRepository
import com.github.clockworkclyde.network.api.DeliveryRemoteDataSourceImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideDeliveryRepository(
        localDataSource: DishesLocalDataSourceImpl,
        remoteDataSource: DeliveryRemoteDataSourceImpl
    ): DishesRepository =
        DishesRepository(localDataSource = localDataSource, remoteDataSource = remoteDataSource)

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository =
        AuthRepository(firebaseAuth)

    @Provides
    @Singleton
    fun provideAddressRepository(
        @Named("usersReference") usersReference: DatabaseReference, gson: Gson
    ): UserAddressRepository =
        UserAddressRepository(usersReference, gson)

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Named("usersReference")
    @Provides
    @Singleton
    fun provideUsersDatabaseReference(db: FirebaseDatabase): DatabaseReference =
        db.getReference(USERS_TABLE)

    @Provides
    @Reusable
    fun provideGson(): Gson = Gson()

    companion object {
        private const val USERS_TABLE = "users"
    }
}