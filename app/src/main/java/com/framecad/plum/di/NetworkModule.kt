package com.framecad.plum.di

import android.content.Context
import com.framecad.plum.data.api.LoginService
import com.framecad.plum.data.api.ProjectsService
import com.framecad.plum.utils.PreferenceUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@InstallIn(ApplicationComponent::class)
@Module
class NetworkModule @Inject constructor(){

    @Singleton
    @Provides
    fun provideProjectsService(@ApplicationContext context: Context, preferenceUtils: PreferenceUtils): ProjectsService {
        return ProjectsService.create(context, preferenceUtils)
    }


    @Singleton
    @Provides
    fun provideLoginService(@ApplicationContext context: Context, preferenceUtils: PreferenceUtils): LoginService {
        return LoginService.create(context, preferenceUtils)
    }



}