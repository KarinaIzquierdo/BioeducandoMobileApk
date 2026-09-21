package com.bioeducando.mobile.ui.screens.publico

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.bioeducando.mobile.data.model.admin.Comentario
import com.bioeducando.mobile.data.model.admin.Publicacion
import com.bioeducando.mobile.viewmodels.publico.PublicComunidadViewModel

private val HeaderGreen = Color(0xFF1A3A2A)
private val AccentGreen = Color(0xFF6AB06A)
private val AvatarBrown = Color(0xFF744D2D)
private val LikeRed = Color(0xFFEF4444)
private val ScreenBackground = Color(0xFFF0F2F0)
private val ActionsBackground = Color(0xFFFAFAFA)
private val CommentsBackground = Color(0xFFFCFDFC)
private val TextDark = Color(0xFF334155)
private val MetaGray = Color(0xFF94A3B8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicComunidadScreen(
    onBackClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    viewModel: PublicComunidadViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Spa,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Comunidad Ambiental",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.White
                        )
                    }
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
                actions = {
                    TextButton(onClick = onLoginClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Login,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Ingresar",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = HeaderGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = ScreenBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = AccentGreen)
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

            if (!uiState.isLoading && uiState.publicaciones.isEmpty() && uiState.error == null) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Spa,
                            contentDescription = null,
                            tint = HeaderGreen.copy(alpha = 0.5f),
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No hay publicaciones aún",
                            color = HeaderGreen,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "¡Sé el primero en compartir con la comunidad!",
                            color = HeaderGreen.copy(alpha = 0.7f),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            items(uiState.publicaciones, key = { it.id }) { publicacion ->
                PublicPostCard(
                    publicacion = publicacion,
                    liked = uiState.likedPosts.contains(publicacion.id),
                    commentsExpanded = uiState.expandedComments.contains(publicacion.id),
                    comentarios = uiState.comentarios[publicacion.id] ?: emptyList(),
                    loadingComments = uiState.loadingComments.contains(publicacion.id),
                    onLikeClick = { viewModel.toggleLike(publicacion.id) },
                    onCommentClick = { viewModel.toggleComments(publicacion.id) }
                )
            }
        }
    }
}

@Composable
private fun PublicPostCard(
    publicacion: Publicacion,
    liked: Boolean,
    commentsExpanded: Boolean,
    comentarios: List<Comentario>,
    loadingComments: Boolean,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit
) {
    var imageFailed by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PostAvatar(
                    imageUrl = publicacion.userAvatar,
                    initial = publicacion.userName.firstOrNull()?.uppercase() ?: "U",
                    backgroundColor = if (publicacion.id % 2 == 0) AccentGreen else AvatarBrown
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = publicacion.userName,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = HeaderGreen
                    )
                    Text(
                        text = publicacion.timeAgo,
                        fontSize = 12.sp,
                        color = MetaGray
                    )
                }
            }

            if (publicacion.contenido.isNotBlank()) {
                Text(
                    text = publicacion.contenido,
                    fontSize = 15.sp,
                    color = TextDark,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (!publicacion.mediaUrl.isNullOrBlank() && publicacion.mediaType == "image" && !imageFailed) {
                AsyncImage(
                    model = publicacion.mediaUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(260.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop,
                    onError = { imageFailed = true }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ActionsBackground)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onLikeClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (liked) LikeRed else HeaderGreen
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Me gusta",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${publicacion.likesCount}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Button(
                    onClick = onCommentClick,
                    colors = ButtonDefaults.buttonColors(containerColor = HeaderGreen),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Message,
                        contentDescription = "Comentar",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Comentar",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            if (commentsExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CommentsBackground)
                        .padding(20.dp)
                ) {
                    if (loadingComments) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = AccentGreen,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    } else if (comentarios.isEmpty()) {
                        Text(
                            text = "Sé el primero en comentar...",
                            color = MetaGray,
                            fontStyle = FontStyle.Italic,
                            fontSize = 13.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        comentarios.forEach { comentario ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            ) {
                                PostAvatar(
                                    imageUrl = comentario.userAvatar,
                                    initial = comentario.userName.firstOrNull()?.uppercase() ?: "U",
                                    backgroundColor = AccentGreen,
                                    size = 32.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color.White)
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Text(
                                        text = comentario.userName,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp,
                                        color = HeaderGreen
                                    )
                                    Text(
                                        text = comentario.contenido,
                                        fontSize = 13.sp,
                                        color = Color(0xFF475569),
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PostAvatar(
    imageUrl: String?,
    initial: String,
    backgroundColor: Color,
    size: androidx.compose.ui.unit.Dp = 44.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = initial,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = (size.value * 0.4).sp
            )
        }
    }
}
