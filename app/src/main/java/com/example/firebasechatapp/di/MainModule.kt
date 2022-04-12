package com.example.firebasechatapp.di

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirestore(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun getSavedSharedPrefs(@ApplicationContext context: Context): SharedPreferences{
        val  prefs = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        return prefs
    }

    @Singleton
    @Provides
    fun provideCloudStorage(): FirebaseStorage{
        return FirebaseStorage.getInstance()
    }

}