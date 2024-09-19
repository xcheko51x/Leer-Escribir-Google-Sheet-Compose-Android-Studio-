package com.xcheko51x.leer_escribir_google_sheet_compose_ejemplo

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WebService {

    @GET("exec?spreadsheetId=16auqqsXhK73jTs_X-tLswjbekofhFvUKJX49CMOpUbk&sheet=personal")
    suspend fun obtenerTodoPersonal()
    : Response<GetResponse>

    @POST("exec")
    suspend fun agregarPersonal(
        @Body personal: PersonalData
    ): Response<PostResponse>

}