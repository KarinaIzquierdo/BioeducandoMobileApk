package com.bioeducando.mobile.ui.screens.usuario

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.bioeducando.mobile.data.model.admin.Noticia
import com.bioeducando.mobile.data.model.admin.ProyectoSteam
import com.bioeducando.mobile.ui.theme.PrimaryGreen
import com.bioeducando.mobile.utils.RetrofitClient
import com.bioeducando.mobile.viewmodels.usuario.UsuarioSteamViewModel

private val DarkGreen = Color(0xFF1B4D3E)
private val BrownHeader = Color(0xFF8B5E3C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioSteamScreen(
    onMenuClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {},
    onRetosClick: () -> Unit = {},
    onComunidadClick: () -> Unit = {},
    onPraeClick: () -> Unit = {},
    onNoticiasClick: () -> Unit = {},
    onEcoEstudioClick: () -> Unit = {},
    viewModel: UsuarioSteamViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var drawerOpen by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedProyecto by remember { mutableStateOf<ProyectoSteam?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadData()
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
                    containerColor = BrownHeader
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
                        HeaderSectionSteam()
                    }

                    item(span = { GridItemSpan(2) }) {
                        Button(
                            onClick = { showDialog = true },
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
                        MisPropuestasCardSteam(propuestas = uiState.misPropuestas)
                    }

                    items(
                        items = uiState.proyectos,
                        key = { it.id }
                    ) { proyecto ->
                        ProyectoExploreCardSteam(proyecto = proyecto) {
                            selectedProyecto = proyecto
                        }
                    }

                    if (uiState.noticias.isNotEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            NoticiaCardSteam(noticia = uiState.noticias.first())
                        }
                    }
                }
            }
        }

        if (drawerOpen) {
            UsuarioDrawer(
                isOpen = drawerOpen,
                onClose = { drawerOpen = false },
                onLogout = onLogoutClick,
                onRetosClick = {
                    onRetosClick()
                    drawerOpen = false
                },
                onComunidadClick = {
                    onComunidadClick()
                    drawerOpen = false
                },
                onSteamClick = { drawerOpen = false },
                onPraeClick = {
                    onPraeClick()
                    drawerOpen = false
                },
                onNoticiasClick = onNoticiasClick,
                onEcoEstudioClick = onEcoEstudioClick
            )
        }
    }

    if (selectedProyecto != null) {
        ProyectoDetalleDialog(
            proyecto = selectedProyecto!!,
            onDismiss = { selectedProyecto = null }
        )
    }

    if (showDialog) {
        ProponerProyectoDialog(
            isCreating = uiState.isCreating,
            onDismiss = { showDialog = false },
            onCreate = { titulo, categoria, descripcion, imagenUri ->
                viewModel.createProyecto(
                    titulo = titulo,
                    categoria = categoria,
                    descripcion = descripcion,
                    imagenUri = imagenUri,
                    onSuccess = { showDialog = false },
                    onError = { error ->
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        )
    }
}

@Composable
private fun UserAvatarSteam(imageUrl: String?, size: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                imageVector = Icons.Filled.Biotech,
                contentDescription = null,
                tint = BrownHeader,
                modifier = Modifier.size(size * 0.6f)
            )
        }
    }
}

@Composable
private fun HeaderSectionSteam() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Biotech,
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
private fun MisPropuestasCardSteam(propuestas: List<ProyectoSteam>) {
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

            if (propuestas.isEmpty()) {
                Text(
                    text = "No tienes propuestas",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            } else {
                propuestas.forEachIndexed { index, propuesta ->
                    PropuestaRowSteam(propuesta = propuesta)
                    if (index < propuestas.size - 1) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun PropuestaRowSteam(propuesta: ProyectoSteam) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
                text = "${propuesta.categoria?.replaceFirstChar { it.uppercase() } ?: "Ciencia"} • ${formatearFechaSteam(propuesta.createdAt)}",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        EstadoChipSteam(estado = propuesta.estado ?: "PENDIENTE")
    }
}

@Composable
private fun EstadoChipSteam(estado: String) {
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
private fun ProyectoExploreCardSteam(
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
private fun NoticiaCardSteam(noticia: Noticia) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            val imageUrl = noticia.imagenUrl?.let { RetrofitClient.BASE_URL.trimEnd('/') + it }
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = noticia.titulo,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = (noticia.categoria ?: "Noticia").uppercase(),
                    color = PrimaryGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = noticia.titulo,
                    color = DarkGreen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = noticia.descripcion,
                    color = Color.Gray,
                    fontSize = 13.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                if (!noticia.cuerpo.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = noticia.cuerpo,
                        color = Color.Gray,
                        fontSize = 13.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun ProponerProyectoDialog(
    isCreating: Boolean,
    onDismiss: () -> Unit,
    onCreate: (String, String, String, Uri?) -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        imagenUri = uri
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Proponer Proyecto",
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría (Ej: Ciencia)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text(text = if (imagenUri != null) "Cambiar imagen" else "Seleccionar imagen")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onCreate(titulo, categoria, descripcion, imagenUri)
                        },
                        enabled = !isCreating && titulo.isNotBlank() && categoria.isNotBlank() && descripcion.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                    ) {
                        if (isCreating) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        } else {
                            Text("Enviar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProyectoDetalleDialog(
    proyecto: ProyectoSteam,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                val imageUrl = proyecto.imagenUrl?.let { RetrofitClient.BASE_URL.trimEnd('/') + it }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
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
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = (proyecto.categoria ?: "CIENCIA").uppercase(),
                    color = PrimaryGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = proyecto.titulo,
                    color = DarkGreen,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    EstadoChipSteam(estado = proyecto.estado ?: "PENDIENTE")
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = proyecto.autor ?: "BIOEDUCANDO",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatearFechaSteam(proyecto.createdAt),
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = proyecto.descripcion ?: "",
                    color = Color.DarkGray,
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )

                if (!proyecto.objetivos.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Objetivos",
                        color = DarkGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = proyecto.objetivos,
                        color = Color.DarkGray,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }

                if (!proyecto.materiales.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Materiales",
                        color = DarkGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = proyecto.materiales,
                        color = Color.DarkGray,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }

                if (!proyecto.impactoAmbiental.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Impacto Ambiental",
                        color = DarkGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = proyecto.impactoAmbiental,
                        color = Color.DarkGray,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "Cerrar",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

private fun formatearFechaSteam(iso: String?): String {
    if (iso == null) return ""
    val parte = iso.take(10)
    val partes = parte.split("-")
    return if (partes.size == 3) {
        "${partes[2]}/${partes[1]}/${partes[0]}"
    } else {
        parte
    }
}
