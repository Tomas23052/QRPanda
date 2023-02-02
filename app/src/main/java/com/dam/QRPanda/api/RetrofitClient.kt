package com.dam.QRPanda.api

import android.util.Base64
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

    //Autenticação usada na API
    private val AUTH = "Basic "+ Base64.encodeToString("tom-secret-key".toByteArray(), Base64.NO_WRAP)
    //Link da API, alojada no Railway
    private const val BASE_URL = "https://damapi-production.up.railway.app/api/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()


            val requestBuilder = original.newBuilder()
                .addHeader("Authorization", AUTH)
                .method(original.method, original.body)

            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()



    val instance: interfaceapi by lazy{
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

                retrofit.create(interfaceapi::class.java)

    }
}