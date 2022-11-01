package dev.ma7.fallingwords.data.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object GsonModule {

    @Reusable
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }
}