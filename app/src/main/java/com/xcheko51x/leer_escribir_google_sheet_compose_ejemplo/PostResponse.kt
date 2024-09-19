package com.xcheko51x.leer_escribir_google_sheet_compose_ejemplo

data class PostResponse(
    val rows: List<List<String>>,
    val sheet: String
)
