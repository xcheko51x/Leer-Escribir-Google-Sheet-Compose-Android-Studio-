package com.xcheko51x.leer_escribir_google_sheet_compose_ejemplo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {

            Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                Screen(
                    paddingValues
                )
            }
        }
    }
}

@Composable
fun Screen(paddingValues: PaddingValues) {

    var id by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }

    var listaPersonal by remember { mutableStateOf(emptyList<Personal>()) }

    LaunchedEffect(Unit) {
        obtenerData { newList ->
            listaPersonal = newList
        }
    }

    Surface(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = id,
                onValueChange = {
                    id = it
                },
                label = {
                    Text(text = "ID")
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = nombre,
                onValueChange = {
                    nombre = it
                },
                label = {
                    Text(text = "NOMBRE")
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = correo,
                onValueChange = {
                    correo = it
                },
                label = {
                    Text(text = "CORREO")
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
            )
            
            Button(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(4.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    agregarData(id, nombre, correo) {
                        obtenerData { newList ->
                            listaPersonal = newList
                            // Limpiar campos si es necesario
                            id = ""
                            nombre = ""
                            correo = ""
                        }
                    }
                }
            ) {
                Text(text = "Agregar Personal")
            }
            

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(listaPersonal) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(8.dp),
                                text = item.ID
                            )

                            Text(
                                modifier = Modifier
                                    .padding(8.dp),
                                text = item.NOMBRE
                            )

                            Text(
                                modifier = Modifier
                                    .padding(8.dp),
                                text = item.CORREO
                            )
                        }
                    }
                }
            }
        }
    }
}

fun obtenerData(onDataReceived: (List<Personal>) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = RetrofitClient.webService(BaseUrl.base_url_get).obtenerTodoPersonal()
        if (response.isSuccessful) {
            withContext(Dispatchers.Main) {
                onDataReceived(response.body()?.personal ?: emptyList())
            }
        }
    }
}

fun agregarData(id: String, nombre: String, correo: String, onComplete: () -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        val personalData = PersonalData(
            spreadsheet_id = Constantes.google_sheet_id,
            sheet = Constantes.sheet,
            rows = listOf(
                listOf(id, nombre, correo)
            )
        )

        val response = RetrofitClient.webService(BaseUrl.base_url_post).agregarPersonal(personalData)

        if (response.isSuccessful) {
            withContext(Dispatchers.Main) {
                onComplete() // Llama a la función para actualizar la lista
            }
        }
    }
}