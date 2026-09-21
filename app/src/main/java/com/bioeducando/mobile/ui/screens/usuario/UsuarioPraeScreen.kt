package com.bioeducando.mobile.ui.screens.usuario

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bioeducando.mobile.data.model.admin.PraeActividad
import com.bioeducando.mobile.data.model.admin.PraeDocumento
import com.bioeducando.mobile.ui.theme.PrimaryGreen
import com.bioeducando.mobile.utils.RetrofitClient
import com.bioeducando.mobile.viewmodels.usuario.UsuarioPraeViewModel

private val DarkGreen = Color(0xFF1B4D3E)
private val BrownHeader = Color(0xFF8B5E3C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioPraeScreen(
    onLogoutClick: () -> Unit = {},
    onRetosClick: () -> Unit = {},
    onComunidadClick: () -> Unit = {},
    onSteamClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {},
    onNoticiasClick: () -> Unit = {},
    onEcoEstudioClick: () -> Unit = {},
    viewModel: UsuarioPraeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var drawerOpen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadPrae()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "PROYECTOS PRAE",
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
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { BannerPraeCard() }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            InfoPraeCard(
                                icon = Icons.Filled.Info,
                                title = "¿Qué es el PRAE?",
                                content = uiState.info.descripcion,
                                modifier = Modifier.weight(1f)
                            )
                            DocumentosPraeCard(
                                documentos = uiState.documentos,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            InfoPraeCard(
                                icon = Icons.Filled.MenuBook,
                                title = "Nuestros Objetivos",
                                content = uiState.info.objetivos,
                                modifier = Modifier.weight(1f)
                            )
                            ContactoPraeCard(modifier = Modifier.weight(1f))
                        }
                    }

                    item { CronogramaPraeCard(actividades = uiState.actividades) }
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
                onSteamClick = {
                    onSteamClick()
                    drawerOpen = false
                },
                onNoticiasClick = onNoticiasClick,
                onEcoEstudioClick = onEcoEstudioClick
            )
        }
    }
}

@Composable
private fun BannerPraeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text(
                    text = "Proyecto Ambiental Escolar (PRAE)",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 26.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Transformando nuestra institución a través de la conciencia y la acción ecológica.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.MenuBook,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.15f),
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }
}

@Composable
private fun InfoPraeCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    color = DarkGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                color = Color.DarkGray,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun DocumentosPraeCard(
    documentos: List<PraeDocumento>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.MenuBook,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Documentos",
                    color = DarkGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (documentos.isEmpty()) {
                Text(
                    text = "No hay documentos",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            } else {
                documentos.forEach { doc ->
                    DocumentoPraeRow(doc = doc) { url ->
                        val full = if (url.startsWith("http")) url else "http://10.0.2.2:8000$url"
                        try {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(full)))
                        } catch (e: Exception) {
                            Toast.makeText(context, "No se pudo abrir el documento", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DocumentoPraeRow(
    doc: PraeDocumento,
    onDescargar: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = doc.titulo,
                color = DarkGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "PDF para descargar",
                color = PrimaryGreen,
                fontSize = 11.sp
            )
        }
        IconButton(
            onClick = { doc.archivoUrl?.let { onDescargar(it) } },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Download,
                contentDescription = "Descargar",
                tint = PrimaryGreen,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ContactoPraeCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Help,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "¿Tienes una duda?",
                    color = DarkGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Contacta al comité ambiental para más información.",
                color = Color.DarkGray,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun CronogramaPraeCard(
    actividades: List<PraeActividad>
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
                    imageVector = Icons.Filled.CalendarToday,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cronograma Ambiental",
                    color = DarkGreen,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (actividades.isEmpty()) {
                Text(
                    text = "No hay actividades programadas",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            } else {
                actividades.forEach { actividad ->
                    ActividadPraeRow(actividad = actividad)
                }
            }
        }
    }
}

@Composable
private fun ActividadPraeRow(
    actividad: PraeActividad
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(40.dp)
                .background(PrimaryGreen, RoundedCornerShape(2.dp))
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = formatearFechaPrae(actividad.fecha).uppercase(),
                color = PrimaryGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = actividad.titulo,
                color = DarkGreen,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = actividad.descripcion,
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
    }
}

private fun formatearFechaPrae(fecha: String?): String {
    if (fecha == null) return ""
    val partes = fecha.split("-")
    return if (partes.size == 3) {
        val meses = listOf("ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC")
        val mes = meses.getOrNull(partes[1].toIntOrNull()?.minus(1) ?: 0) ?: "ENE"
        "${partes[2].toIntOrNull() ?: 1} $mes, ${partes[0]}"
    } else {
        fecha
    }
}
