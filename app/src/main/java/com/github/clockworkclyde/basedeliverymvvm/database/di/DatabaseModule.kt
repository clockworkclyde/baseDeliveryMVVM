package com.github.clockworkclyde.basedeliverymvvm.database.di

import android.app.Application
import androidx.room.Room
import com.github.clockworkclyde.basedeliverymvvm.database.dao.MenuCategoriesDao
import com.github.clockworkclyde.basedeliverymvvm.database.dao.OrderCartDao
import com.github.clockworkclyde.basedeliverymvvm.database.db.BaseDeliveryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): BaseDeliveryDatabase =
        Room.databaseBuilder(app, BaseDeliveryDatabase::class.java, "base_delivery_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideMenuCategoriesDao(db: BaseDeliveryDatabase): MenuCategoriesDao = db.deliveryDao()

    @Provides
    fun provideOrderCartDao(db: BaseDeliveryDatabase): OrderCartDao = db.orderCartDao()

}