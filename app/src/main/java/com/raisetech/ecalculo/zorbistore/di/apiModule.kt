package com.raisetech.ecalculo.zorbistore.di


import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.raisetech.ecalculo.zorbistore.data.api.zorbiApiServices
import com.raisetech.ecalculo.zorbistore.data.network.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import okhttp3.internal.addHeaderLenient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val apiModule = module {
    single {
        createOkHttpClient(NetworkConnectionInterceptor(get()))
    }

    single {

        createRetrofit<zorbiApiServices>(get())
    }
}

fun createOkHttpClient(networkConnectionInterceptor: NetworkConnectionInterceptor): OkHttpClient {


    val httpLoginInterceptor = HttpLoggingInterceptor()
    httpLoginInterceptor.level = HttpLoggingInterceptor.Level.BODY


    return OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
        .connectTimeout(1000L, TimeUnit.SECONDS)
        .readTimeout(1000L, TimeUnit.SECONDS)
        .addInterceptor(httpLoginInterceptor)
        .build()



}

inline fun<reified T>createRetrofit(okHttpClient: OkHttpClient):T{

    val retrofit= Retrofit.Builder()

        .baseUrl("http://api.ecalculo.com/")
        .client(okHttpClient)
//        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    return retrofit.create(T::class.java)
}