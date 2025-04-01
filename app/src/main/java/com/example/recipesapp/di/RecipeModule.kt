package com.example.recipesapp.di

import android.content.Context
import androidx.room.Room
import com.example.recipesapp.DataConstants.BASE_URL
import com.example.recipesapp.data.CategoriesDao
import com.example.recipesapp.data.Database
import com.example.recipesapp.data.RecipeApiService
import com.example.recipesapp.data.RecipesDao
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class RecipeModule {

    @Provides
    fun provideDataBase(@ApplicationContext context: Context): Database = Room.databaseBuilder(
        context,
        Database::class.java,
        "database"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideCategoriesDao(database: Database): CategoriesDao = database.categoriesDao()

    @Provides
    fun provideRecipesDao(database: Database): RecipesDao = database.recipesDao()

    @Provides
    fun provideHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
        return retrofit
    }

    @Provides
    fun provideRecipeApiService(retrofit: Retrofit): RecipeApiService {
        return retrofit.create(RecipeApiService::class.java)
    }
}