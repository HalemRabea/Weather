package com.hr.musalatask.di


import com.hr.musalatask.internet.ApiServices
import com.hr.musalatask.views.weatherMainScreen.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@InstallIn(ViewModelComponent::class)
@Module
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideUserLoginRepository(
        apis: ApiServices
    ): WeatherRepository {
        return WeatherRepository(apis)
    }


}