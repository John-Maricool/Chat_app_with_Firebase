package com.example.firebasechatapp.di

import android.content.SharedPreferences
import com.example.firebasechatapp.data.db.remote.FirebaseAuthSource
import com.example.firebasechatapp.data.db.remote.FirebaseFirestoreSource
import com.example.firebasechatapp.data.db.remote.FirebaseStorageSource
import com.example.firebasechatapp.data.repositories.abstractions.*
import com.example.firebasechatapp.data.repositories.impl.*
import com.example.firebasechatapp.utils.SharedPrefsCalls
import com.example.firebasechatapp.utils.SharedPrefsCallsImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Singleton
    @Provides
    fun provideAuthRepository(source: FirebaseAuthSource, prefs: SharedPrefsCalls): AuthRepository {
        return AuthRepositoryImpl(source, prefs)
    }

    @Singleton
    @Provides
    fun provideSharedPRefsCalls(prefs: SharedPreferences): SharedPrefsCalls {
        return SharedPrefsCallsImpl(prefs)
    }

    @Singleton
    @Provides
    fun provideStorageRepository(source: FirebaseStorageSource): StorageRepository {
        return StorageRepositoryImpl(source)
    }

    @Singleton
    @Provides
    fun provideRemoteUserRepository(
        source: FirebaseFirestoreSource,
        prefs: SharedPrefsCalls
    ): RemoteUserRepository {
        return RemoteUserRepositoryImpl(source, prefs)
    }

    @Singleton
    @Provides
    fun provideCloudRepository(
        source: FirebaseFirestoreSource,
        prefs: SharedPrefsCalls
    ): CloudRepository {
        return CloudRepositoryImpl(source, prefs)
    }

    @Provides
    @Named("users")
    fun provideUsersAndChatsRepository(
        source: FirebaseFirestoreSource,
        prefs: SharedPrefsCalls
    ): UsersAndChatsRepository {
        return UsersListRepository(prefs, source)
    }
}