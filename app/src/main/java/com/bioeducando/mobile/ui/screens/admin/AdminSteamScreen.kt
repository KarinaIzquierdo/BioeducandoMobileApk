package com.bioeducando.mobile.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.bioeducando.mobile.data.model.admin.ProyectoSteam
import com.bioeducando.mobile.ui.theme.PrimaryGreen
import com.bioeducando.mobile.utils.RetrofitClient
import com.bioeducando.mobile.viewmodels.admin.AdminSteamViewModel

private val DarkGreen = Color(0xFF1B4D3E)
private val BrownHeader = Color(0xFF8B5E3C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSteamScreen(
    onMenuClick: () -> Unit = {},
    onNuevoProyectoClick: () -> Unit = {},
    viewModel: AdminSteamViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var proyectoSeleccionado by remember { mutableStateOf<ProyectoSteam?>(null) }
    var confirmarEliminar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadProyectos()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "PROYECTO STEAM",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F7F4))
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryGreen)
                }
            } else if (uiState.error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.error ?: "Error",
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item(span = { GridItemSpan(2) }) {
                        HeaderSection()
                    }

                    item(span = { GridItemSpan(2) }) {
                        Button(
                            onClick = onNuevoProyectoClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Proponer Proyecto",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }

                    item(span = { GridItemSpan(2) }) {
                        MisPropuestasCard(
                            solicitudes = uiState.solicitudes,
                            onPropuestaClick = { proyectoSeleccionado = it }
                        )
                    }

                    items(
                        items = uiState.proyectos,
                        key = { it.id }
                    ) { proyecto ->
                        ProyectoExploreCard(proyecto = proyecto) {
                            proyectoSeleccionado = proyecto
                        }
                    }
                }
            }

            val proyecto = proyectoSeleccionado
            if (proyecto != null) {
                ProyectoDetalleDialog(
                    proyecto = proyecto,
                    onDismiss = { proyectoSeleccionado = null },
                    onAprobar = {
                        viewModel.updateEstado(proyecto.id, "aprobado") {
                            proyectoSeleccionado = null
                        }
                    },
                    onRechazar = {
                        viewModel.updateEstado(proyecto.id, "rechazado") {
                            proyectoSeleccionado = null
                        }
                    },
                    onEliminar = { confirmarEliminar = true }
                )
            }

            if (confirmarEliminar && proyecto != null) {
                AlertDialog(
                    onDismissRequest = { confirmarEliminar = false },
                    title = { Text("Eliminar proyecto") },
                    text = {
                        Text("¿Estás seguro de eliminar \"${proyecto.titulo}\"? Esta acción no se puede deshacer.")
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.eliminarProyecto(proyecto.id) {
                                confirmarEliminar = false
                                proyectoSeleccionado = null
                            }
                        }) {
                            Text(
                                text = "Eliminar",
                                color = Color(0xFFB91C1C),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { confirmarEliminar = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun HeaderSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Build,
            contentDescription = null,
            tint = PrimaryGreen,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Explora Proyectos STEAM",
            color = DarkGreen,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun MisPropuestasCard(
    solicitudes: List<ProyectoSteam>,
    onPropuestaClick: (ProyectoSteam) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Build,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Mis Propuestas",
                    color = DarkGreen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (solicitudes.isEmpty()) {
                Text(
                    text = "No tienes propuestas",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            } else {
                solicitudes.forEachIndexed { index, propuesta ->
                    PropuestaRow(
                        propuesta = propuesta,
                        onClick = { onPropuestaClick(propuesta) }
                    )
                    if (index < solicitudes.size - 1) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun PropuestaRow(
    propuesta: ProyectoSteam,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = propuesta.titulo,
                color = DarkGreen,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${propuesta.categoria?.replaceFirstChar { it.uppercase() } ?: "Ciencia"} • ${formatearFecha(propuesta.createdAt)}",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        EstadoChip(estado = propuesta.estado ?: "PENDIENTE")
    }
}

@Composable
private fun EstadoChip(estado: String) {
    val aprobado = estado.lowercase() == "aprobado"
    val bg = if (aprobado) Color(0xFFC8E6C9) else Color(0xFFFFF9C4)
    val textColor = if (aprobado) DarkGreen else Color(0xFF8B6C00)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = estado.uppercase(),
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun ProyectoExploreCard(
    proyecto: ProyectoSteam,
    onVerClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                val imageUrl = proyecto.imagenUrl?.let { RetrofitClient.BASE_URL.trimEnd('/') + it }
                if (imageUrl != null) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = proyecto.titulo,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Build,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = (proyecto.categoria ?: "CIENCIA").uppercase(),
                    color = PrimaryGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = proyecto.titulo,
                    color = DarkGreen,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = proyecto.descripcion ?: "",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onVerClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Ver Proyecto",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProyectoDetalleDialog(
    proyecto: ProyectoSteam,
    onDismiss: () -> Unit,
    onAprobar: () -> Unit,
    onRechazar: () -> Unit,
    onEliminar: () -> Unit
) {
    val estado = proyecto.estado?.lowercase() ?: "pendiente"

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 640.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
                    val imageUrl = proyecto.imagenUrl?.let {
                        RetrofitClient.BASE_URL.trimEnd('/') + it
                    }
                    if (imageUrl != null) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = proyecto.titulo,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Build,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(56.dp)
                        )
                    }
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = (proyecto.categoria ?: "CIENCIA").uppercase(),
                            color = PrimaryGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        EstadoChip(estado = estado)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = proyecto.titulo,
                        color = DarkGreen,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Por ${proyecto.autor ?: "Administrador"} • ${formatearFecha(proyecto.createdAt)}",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )

                    SeccionDetalle("Descripción", proyecto.descripcion)
                    SeccionDetalle("Objetivos", proyecto.objetivos)
                    SeccionDetalle("Materiales", proyecto.materiales)
                    SeccionDetalle("Impacto Ambiental", proyecto.impactoAmbiental)

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (estado != "aprobado") {
                            Button(
                                onClick = onAprobar,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryGreen
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Aprobar",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                        if (estado != "rechazado") {
                            OutlinedButton(
                                onClick = onRechazar,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = null,
                                    tint = Color(0xFFB91C1C),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Rechazar",
                                    color = Color(0xFFB91C1C),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onEliminar,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB91C1C)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Eliminar",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Cerrar", color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
private fun SeccionDetalle(titulo: String, contenido: String?) {
    if (contenido.isNullOrBlank()) return
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = titulo,
        color = DarkGreen,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = contenido,
        color = Color(0xFF475569),
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
}

private fun formatearFecha(iso: String?): String {
    if (iso == null) return ""
    val parte = iso.take(10)
    val partes = parte.split("-")
    return if (partes.size == 3) {
        "${partes[2]}/${partes[1]}/${partes[0]}"
    } else {
        parte
    }
}
