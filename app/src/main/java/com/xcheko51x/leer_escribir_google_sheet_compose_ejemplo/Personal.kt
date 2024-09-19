package com.xcheko51x.leer_escribir_google_sheet_compose_ejemplo

data class Personal(
    val ID: String,
    val NOMBRE: String,
    val CORREO: String
)

data class PersonalData(
    val spreadsheet_id: String,
    val sheet: String,
    val rows: List<List<String>>
)
