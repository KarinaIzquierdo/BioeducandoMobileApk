package com.bioeducando.mobile.ui.screens.usuario

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.bioeducando.mobile.data.model.admin.Noticia
import com.bioeducando.mobile.data.model.admin.NoticiaComentario
import com.bioeducando.mobile.ui.theme.PrimaryGreen
import com.bioeducando.mobile.viewmodels.usuario.UsuarioNoticiasViewModel

private val BrownHeader = Color(0xFF8B5E3C)
private val DarkGreen = Color(0xFF1B4D3E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioNoticiasScreen(
    onLogoutClick: () -> Unit = {},
    onRetosClick: () -> Unit = {},
    onComunidadClick: () -> Unit = {},
    onSteamClick: () -> Unit = {},
    onPraeClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {},
    onEcoEstudioClick: () -> Unit = {},
    viewModel: UsuarioNoticiasViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var drawerOpen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadNoticias()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "NOTICIAS AMBIENTALES",
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
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F9F6))
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = PrimaryGreen
                    )
                }

                uiState.error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.error ?: "Error",
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        androidx.compose.material3.Button(
                            onClick = { viewModel.loadNoticias() }
                        ) {
                            Text("Reintentar")
                        }
                    }
                }

                uiState.noticias.isEmpty() -> {
                    Text(
                        text = "No hay noticias disponibles",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(uiState.noticias) { noticia ->
                            NoticiaAmbientalCard(
                                noticia = noticia,
                                onLike = { viewModel.toggleLike(noticia.id) },
                                onComment = { viewModel.abrirComentarios(noticia.id) }
                            )
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
                onRetosClick = onRetosClick,
                onComunidadClick = onComunidadClick,
                onSteamClick = onSteamClick,
                onPraeClick = onPraeClick,
                onNoticiasClick = { },
                onEcoEstudioClick = onEcoEstudioClick
            )
        }

        uiState.comentariosNoticiaId?.let { noticiaId ->
            ComentariosDialog(
                noticiaId = noticiaId,
                comentarios = uiState.comentarios,
                isEnviando = uiState.isEnviandoComentario,
                onCerrar = viewModel::cerrarComentarios,
                onEnviar = { viewModel.enviarComentario(noticiaId, it) }
            )
        }
    }
}

@Composable
private fun NoticiaAmbientalCard(
    noticia: Noticia,
    onLike: (Int) -> Unit,
    onComment: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = noticia.categoria.uppercase(),
                color = PrimaryGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = noticia.titulo,
                color = DarkGreen,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 26.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = noticia.descripcion,
                color = Color(0xFF4A4A4A),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            noticia.cuerpo?.let { cuerpo ->
                if (cuerpo.isNotBlank()) {
                    Text(
                        text = cuerpo,
                        color = Color(0xFF555555),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            NoticiaImagen(
                imagenUrl = noticia.imagenUrl,
                titulo = noticia.titulo
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = noticia.categoria,
                    color = PrimaryGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )

                noticia.pie_foto?.let { pie ->
                    if (pie.isNotBlank()) {
                        Text(
                            text = "#${pie.lowercase().replace(" ", "")}",
                            color = Color(0xFF6B7280),
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onLike(noticia.id) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Me gusta",
                        tint = if (noticia.is_liked_by_user) Color(0xFFE53935) else Color(0xFF6B7280),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (noticia.is_liked_by_user) "Te gusta ${noticia.likes_count}" else "Me gusta ${noticia.likes_count}",
                        color = if (noticia.is_liked_by_user) Color(0xFFE53935) else Color(0xFF6B7280),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onComment(noticia.id) }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comentar",
                        tint = Color(0xFF6B7280),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Comentar (${noticia.comments_count})",
                        color = Color(0xFF6B7280),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun ComentariosDialog(
    noticiaId: Int,
    comentarios: List<NoticiaComentario>,
    isEnviando: Boolean,
    onCerrar: () -> Unit,
    onEnviar: (String) -> Unit
) {
    var texto by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onCerrar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Comentarios",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (comentarios.isEmpty()) {
                        item {
                            Text(
                                text = "Aún no hay comentarios.",
                                color = Color.Gray,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        items(comentarios) { c ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = c.userName,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkGreen,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = c.comentario,
                                    fontSize = 14.sp,
                                    color = Color(0xFF4A4A4A)
                                )
                                Text(
                                    text = c.timeAgo,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = texto,
                        onValueChange = { texto = it },
                        label = { Text("Escribe un comentario") },
                        modifier = Modifier.weight(1f),
                        singleLine = false,
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onEnviar(texto)
                            texto = ""
                        },
                        enabled = texto.isNotBlank() && !isEnviando,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                    ) {
                        if (isEnviando) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Enviar", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NoticiaImagen(
    imagenUrl: String?,
    titulo: String
) {
    val imageExtensions = listOf(".jpg", ".jpeg", ".png", ".webp", ".gif", ".bmp")
    val esImagen = imagenUrl?.let { url ->
        imageExtensions.any { ext -> url.lowercase().endsWith(ext) }
    } ?: false

    if (esImagen) {
        Log.d("NoticiaImagen", "Cargando imagen: $imagenUrl")
        AsyncImage(
            model = imagenUrl,
            contentDescription = titulo,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp)),
            onError = {
                Log.e("NoticiaImagen", "Error cargando imagen: $imagenUrl")
            }
        )
    } else {
        Log.d("NoticiaImagen", "URL no es imagen o está vacía: $imagenUrl")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Filled.Image,
                    contentDescription = "Sin imagen",
                    tint = Color(0xFF9E9E9E),
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Sin imagen",
                    color = Color(0xFF9E9E9E),
                    fontSize = 13.sp
                )
            }
        }
    }
}
