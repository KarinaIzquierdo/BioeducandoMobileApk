package com.bioeducando.mobile.ui.screens.usuario

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Label
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bioeducando.mobile.data.model.admin.Reto
import com.bioeducando.mobile.ui.theme.PrimaryGreen
import com.bioeducando.mobile.viewmodels.usuario.UsuarioRetosViewModel

private val BrownHeader = Color(0xFF8B5E3C)
private val DarkGreen = Color(0xFF1B4D3E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioRetosScreen(
    onMenuClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {},
    onComunidadClick: () -> Unit = {},
    onSteamClick: () -> Unit = {},
    onPraeClick: () -> Unit = {},
    onNoticiasClick: () -> Unit = {},
    onEcoEstudioClick: () -> Unit = {},
    viewModel: UsuarioRetosViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var drawerOpen by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedReto by remember { mutableStateOf<Reto?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "RETOS ECOLÓGICOS",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { drawerOpen = true }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menú",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BrownHeader,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F7F4))
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PrimaryGreen)
                    }
                }
            }

            if (uiState.error != null) {
                item {
                    Text(
                        text = uiState.error.orEmpty(),
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
            }

            if (!uiState.isLoading && uiState.retos.isEmpty() && uiState.error == null) {
                item {
                    Text(
                        text = "No hay retos disponibles",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }

            itemsIndexed(uiState.retos) { index, reto ->
                RetoUsuarioCard(
                    reto = reto,
                    missionNumber = index + 1,
                    onIniciar = {
                        selectedReto = reto
                        showDialog = true
                    }
                )
            }
        }
    }

    if (showDialog && selectedReto != null) {
        RetoCompletarDialog(
            reto = selectedReto!!,
            onDismiss = {
                showDialog = false
                selectedReto = null
            },
            onCompletar = {
                Toast.makeText(context, "Reto completado: ${it.titulo}", Toast.LENGTH_SHORT).show()
                showDialog = false
                selectedReto = null
            }
        )
    }

    if (drawerOpen) {
        UsuarioDrawer(
            isOpen = drawerOpen,
            onClose = { drawerOpen = false },
            onLogout = onLogoutClick,
            onRetosClick = { drawerOpen = false },
            onComunidadClick = onComunidadClick,
            onSteamClick = onSteamClick,
            onPraeClick = onPraeClick,
            onNoticiasClick = onNoticiasClick,
            onEcoEstudioClick = onEcoEstudioClick
        )
    }
}

@Composable
private fun RetoUsuarioCard(
    reto: Reto,
    missionNumber: Int,
    onIniciar: () -> Unit
) {
    val saved = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 240.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(PrimaryGreen)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "MISIÓN ${String.format("%02d", missionNumber)}",
                        color = PrimaryGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )

                    IconButton(
                        onClick = { saved.value = !saved.value },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (saved.value) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = "Guardar",
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = reto.titulo,
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        RetoTag(icon = Icons.Filled.Schedule, text = reto.duracion ?: "--")
                        RetoTag(icon = Icons.Filled.BarChart, text = reto.dificultad ?: "--")
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        RetoTag(icon = Icons.Filled.Label, text = reto.categoria ?: "--")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = reto.descripcion ?: "",
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { onIniciar() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Dar inicio al reto",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun RetoCompletarDialog(
    reto: Reto,
    onDismiss: () -> Unit,
    onCompletar: (Reto) -> Unit
) {
    var evidencia by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(reto.titulo, fontWeight = FontWeight.Bold, color = DarkGreen) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        RetoTag(icon = Icons.Filled.Schedule, text = reto.duracion ?: "--")
                        RetoTag(icon = Icons.Filled.BarChart, text = reto.dificultad ?: "--")
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        RetoTag(icon = Icons.Filled.Label, text = reto.categoria ?: "--")
                    }
                }

                Text(
                    text = reto.descripcion ?: "Sin descripción",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    lineHeight = 20.sp
                )

                OutlinedTextField(
                    value = evidencia,
                    onValueChange = { evidencia = it },
                    label = { Text("Evidencia (notas o enlace)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onCompletar(reto) },
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen)
            ) {
                Text("Completar reto", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = DarkGreen)
            }
        }
    )
}

@Composable
private fun RetoTag(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFE8F5E9))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryGreen,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = PrimaryGreen,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
