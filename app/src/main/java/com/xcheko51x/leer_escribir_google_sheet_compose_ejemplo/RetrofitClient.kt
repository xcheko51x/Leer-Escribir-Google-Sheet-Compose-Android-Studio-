package com.xcheko51x.leer_escribir_google_sheet_compose_ejemplo

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    fun webService(baseUrl: String): WebService {
        val webService: WebService by lazy {
            Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(WebService::class.java)
        }

        return webService
    }
}