package com.dm.berxley.offlinefirstmoviesapp.di

import android.app.Application
import androidx.room.Room
import com.dm.berxley.offlinefirstmoviesapp.movieslist.data.local.movie.MovieDatabase
import com.dm.berxley.offlinefirstmoviesapp.movieslist.data.remote.MovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()


    @Provides
    @Singleton
    fun providesMovieApi(): MovieApi {
        return Retrofit.Builder()
            .baseUrl(MovieApi.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun providesMovieDatabase(application: Application): MovieDatabase {
        return Room.databaseBuilder(
            application,
            MovieDatabase::class.java,
            "moviedb.db"
        ).build()
    }

}