package com.bioeducando.mobile.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bioeducando.mobile.data.model.admin.RetoUpdateRequest
import com.bioeducando.mobile.ui.theme.PrimaryGreen
import com.bioeducando.mobile.viewmodels.admin.AdminEditarRetoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEditarRetoScreen(
    retoId: Int,
    onBackClick: () -> Unit = {},
    viewModel: AdminEditarRetoViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("activa") }
    var categoria by remember { mutableStateOf("reciclaje") }
    var dificultad by remember { mutableStateOf("intermedio") }
    var puntos by remember { mutableStateOf("100") }
    var duracion by remember { mutableStateOf("") }
    var insignia by remember { mutableStateOf("experto") }
    var evidencias by remember { mutableStateOf(listOf("foto", "reflexion")) }

    LaunchedEffect(retoId) {
        viewModel.loadReto(retoId)
    }

    LaunchedEffect(uiState.reto) {
        uiState.reto?.let { reto ->
            titulo = reto.titulo
            descripcion = reto.descripcion ?: ""
            estado = reto.estado ?: "activa"
            categoria = reto.categoria ?: "reciclaje"
            dificultad = reto.dificultad ?: "intermedio"
            puntos = reto.puntos.toString()
            duracion = reto.duracion ?: ""
            insignia = reto.insignia ?: "experto"
            evidencias = reto.evidencias ?: listOf("foto", "reflexion")
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "EDITAR MISIÓN",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF8B5E3C),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F7F4))
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryGreen)
                }
            }

            uiState.error?.let { error ->
                Text(text = error, color = Color.Red, fontSize = 14.sp)
            }

            uiState.successMessage?.let { message ->
                Text(text = message, color = PrimaryGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = PrimaryGreen
                )
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = PrimaryGreen
                )
            )

            DropdownSelector(
                label = "Estado",
                selected = estado,
                options = listOf("activa" to "Activa", "inactiva" to "Inactiva"),
                onSelected = { estado = it }
            )

            DropdownSelector(
                label = "Categoría",
                selected = categoria,
                options = listOf(
                    "reciclaje" to "Reciclaje",
                    "agua" to "Cuidado del Agua",
                    "energia" to "Ahorro de Energía",
                    "biodiversidad" to "Biodiversidad"
                ),
                onSelected = { categoria = it }
            )

            DropdownSelector(
                label = "Dificultad",
                selected = dificultad,
                options = listOf(
                    "facil" to "Fácil",
                    "intermedio" to "Intermedio",
                    "dificil" to "Difícil"
                ),
                onSelected = { dificultad = it }
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = puntos,
                    onValueChange = { newValue -> puntos = newValue.filter { it.isDigit() } },
                    label = { Text("Puntos") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = PrimaryGreen
                    )
                )

                OutlinedTextField(
                    value = duracion,
                    onValueChange = { duracion = it },
                    label = { Text("Duración") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = PrimaryGreen
                    )
                )
            }

            DropdownSelector(
                label = "Insignia",
                selected = insignia,
                options = listOf(
                    "experto" to "Reciclador Experto",
                    "guardian" to "Guardián del Bosque",
                    "maestro" to "Maestro Ambiental"
                ),
                onSelected = { insignia = it }
            )

            Text(
                text = "Evidencia requerida",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B4D3E),
                fontSize = 14.sp
            )

            Column {
                EvidenciaCheckbox("Foto", "foto", evidencias) { selected ->
                    evidencias = toggleEvidencia(evidencias, "foto", selected)
                }
                EvidenciaCheckbox("Reflexión", "reflexion", evidencias) { selected ->
                    evidencias = toggleEvidencia(evidencias, "reflexion", selected)
                }
                EvidenciaCheckbox("Video", "video", evidencias) { selected ->
                    evidencias = toggleEvidencia(evidencias, "video", selected)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val request = RetoUpdateRequest(
                        titulo = titulo,
                        descripcion = descripcion.ifBlank { null },
                        estado = estado,
                        categoria = categoria,
                        dificultad = dificultad,
                        puntos = puntos.toIntOrNull() ?: 0,
                        duracion = duracion.ifBlank { null },
                        insignia = insignia,
                        evidencias = evidencias
                    )
                    viewModel.updateReto(
                        retoId = retoId,
                        reto = request,
                        onSuccess = { onBackClick() },
                        onError = {}
                    )
                },
                enabled = !uiState.isSaving,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                shape = RoundedCornerShape(14.dp)
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.height(20.dp))
                } else {
                    Text("Guardar cambios", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownSelector(
    label: String,
    selected: String,
    options: List<Pair<String, String>>,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val display = options.find { it.first == selected }?.second ?: selected

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = display,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = PrimaryGreen
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.second) },
                    onClick = {
                        onSelected(option.first)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun EvidenciaCheckbox(
    label: String,
    value: String,
    evidencias: List<String>,
    onCheckedChange: (Boolean) -> Unit
) {
    val checked = value in evidencias
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onCheckedChange(it) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, color = Color.DarkGray, fontSize = 14.sp)
    }
}

private fun toggleEvidencia(list: List<String>, value: String, add: Boolean): List<String> {
    return if (add) list + value else list - value
}
