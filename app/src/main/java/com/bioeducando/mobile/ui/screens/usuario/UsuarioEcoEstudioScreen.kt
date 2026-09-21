package com.bioeducando.mobile.ui.screens.usuario

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import coil.compose.AsyncImage
import com.bioeducando.mobile.data.model.admin.Contenido
import com.bioeducando.mobile.data.model.admin.Noticia
import com.bioeducando.mobile.ui.theme.PrimaryGreen
import com.bioeducando.mobile.viewmodels.usuario.UsuarioEcoEstudioUiState
import com.bioeducando.mobile.viewmodels.usuario.UsuarioEcoEstudioViewModel

private val BrownHeader = Color(0xFF8B5E3C)
private val DarkGreen = Color(0xFF1B4D3E)
private val LightBackground = Color(0xFFF5F9F6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioEcoEstudioScreen(
    onLogoutClick: () -> Unit = {},
    onRetosClick: () -> Unit = {},
    onComunidadClick: () -> Unit = {},
    onSteamClick: () -> Unit = {},
    onPraeClick: () -> Unit = {},
    onNoticiasClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {},
    viewModel: UsuarioEcoEstudioViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var drawerOpen by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "ECO-ESTUDIO",
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
        },
        containerColor = LightBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Header()
            }

            item {
                HerramientasCard()
            }

            item {
                CompartirCard(
                    uiState = uiState,
                    viewModel = viewModel
                )
            }

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

            if (uiState.contenidos.isNotEmpty()) {
                item {
                    Text(
                        text = "Mis aportes",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(uiState.contenidos) { contenido ->
                    ContenidoItem(contenido = contenido)
                }
            }
        }
    }

    if (drawerOpen) {
        UsuarioDrawer(
            isOpen = drawerOpen,
            onClose = { drawerOpen = false },
            onLogout = onLogoutClick,
            onRetosClick = onRetosClick,
            onComunidadClick = onComunidadClick,
            onSteamClick = onSteamClick,
            onPraeClick = onPraeClick,
            onNoticiasClick = onNoticiasClick,
            onEcoEstudioClick = { }
        )
    }
}

@Composable
private fun Header() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "¡Sé un Creador Eco!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGreen,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Utiliza estas herramientas para crear y compartir tus avances ecológicos con el mundo.",
            fontSize = 14.sp,
            color = Color(0xFF6B7280),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun HerramientasCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Herramientas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val context = LocalContext.current

            ToolItem(
                icon = Icons.Filled.PlayArrow,
                name = "CapCut",
                description = "Edita tus videos profesionalmente.",
                onClick = { openTool(context, "com.lemon.lvoverseas", "https://www.capcut.com") }
            )
            ToolItem(
                icon = Icons.Filled.Image,
                name = "Canva",
                description = "Diseña posters increíbles.",
                onClick = { openTool(context, "com.canva.editor", "https://www.canva.com") }
            )
            ToolItem(
                icon = Icons.Filled.PlayArrow,
                name = "TikTok",
                description = "Comparte y hazte viral.",
                onClick = { openTool(context, "com.zhiliaoapp.musically", "https://www.tiktok.com") }
            )
        }
    }
}

@Composable
private fun ToolItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    name: String,
    description: String,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF0E6FF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = name,
                tint = Color(0xFF6B21A8),
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = name,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1F2937),
                fontSize = 16.sp
            )
            Text(
                text = description,
                color = Color(0xFF6B7280),
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun CompartirCard(
    uiState: UsuarioEcoEstudioUiState,
    viewModel: UsuarioEcoEstudioViewModel
) {
    val context = LocalContext.current
    val archivoUri = uiState.archivoUri
    val pdfUri = uiState.pdfUri

    val launcherArchivo = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.onArchivoUriChange(it) }
    }

    val launcherPdf = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onPdfUriChange(it) }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Compartir",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Descripción",
                fontWeight = FontWeight.SemiBold,
                color = DarkGreen,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = uiState.descripcion,
                onValueChange = { viewModel.onDescripcionChange(it) },
                placeholder = { Text("Cuéntanos sobre tu creación...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tu Obra Maestra (Video o Imagen)",
                fontWeight = FontWeight.SemiBold,
                color = DarkGreen,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            FileSelector(
                label = "Seleccionar archivo",
                icon = Icons.Filled.Image,
                selectedUri = archivoUri,
                onClick = {
                    launcherArchivo.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Documento PDF (Opcional)",
                fontWeight = FontWeight.SemiBold,
                color = DarkGreen,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            FileSelector(
                label = "Seleccionar PDF",
                icon = Icons.Filled.PictureAsPdf,
                selectedUri = pdfUri,
                onClick = { launcherPdf.launch("application/pdf") }
            )

            uiState.exito?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = it,
                    color = PrimaryGreen,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }

            uiState.error?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = it,
                    color = Color(0xFFB00020),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.publicar() },
                enabled = (uiState.isPublicando == false),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isPublicando == true) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Publicar", color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Filled.Send,
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
private fun FileSelector(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selectedUri: Uri?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
        onClick = onClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = PrimaryGreen,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = selectedUri?.lastPathSegment ?: label,
                color = if (selectedUri != null) Color(0xFF1F2937) else Color(0xFF6B7280),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun ContenidoItem(contenido: Contenido) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = contenido.description ?: "",
                fontSize = 14.sp,
                color = Color(0xFF4A4A4A)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Estado:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = DarkGreen
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = contenido.status ?: "revision",
                    fontSize = 12.sp,
                    color = PrimaryGreen
                )
            }
            contenido.fileUrl?.let { url ->
                Spacer(modifier = Modifier.height(8.dp))
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }
    }
}

private fun openTool(context: Context, packageName: String, fallbackUrl: String) {
    val pm = context.packageManager
    val intent = pm.getLaunchIntentForPackage(packageName)
    if (intent != null) {
        context.startActivity(intent)
    } else {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl)))
    }
}
