package com.flexeiprata.androidmytaskapplication.modules

import com.flexeiprata.androidmytaskapplication.common.BASE_URL
import com.flexeiprata.androidmytaskapplication.description.data.DescApiHelper
import com.flexeiprata.androidmytaskapplication.description.data.DescApiService
import com.flexeiprata.androidmytaskapplication.products.data.api.ProductsApiHelper
import com.flexeiprata.androidmytaskapplication.products.data.api.ProductsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {
    @Singleton
    @Provides
    fun  provideHttpClient() : OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ProductsApiService = retrofit.create(ProductsApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(productsApiService: ProductsApiService) = ProductsApiHelper(productsApiService)

    @Provides
    @Singleton
    fun provideDescApiService(retrofit: Retrofit): DescApiService = retrofit.create(DescApiService::class.java)

    @Provides
    @Singleton
    fun provideDescApiHelper(descApiService:  DescApiService) = DescApiHelper(descApiService)

}