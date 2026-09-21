package com.bioeducando.mobile.ui.screens.usuario

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.outlined.Person
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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.bioeducando.mobile.data.local.TokenManager
import com.bioeducando.mobile.data.model.admin.Publicacion
import com.bioeducando.mobile.ui.theme.PrimaryGreen
import com.bioeducando.mobile.viewmodels.usuario.UsuarioComunidadViewModel

private val BrownHeader = Color(0xFF8B5E3C)
private val DarkGreen = Color(0xFF1B4D3E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioComunidadScreen(
    onLogoutClick: () -> Unit = {},
    onRetosClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {},
    onSteamClick: () -> Unit = {},
    onPraeClick: () -> Unit = {},
    onNoticiasClick: () -> Unit = {},
    onEcoEstudioClick: () -> Unit = {},
    viewModel: UsuarioComunidadViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    var drawerOpen by remember { mutableStateOf(false) }

    var postText by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
    }

    val currentUserName = uiState.currentUserName ?: tokenManager.getUserName() ?: ""
    val currentUserPhoto = uiState.currentUserPhoto ?: tokenManager.getUserPhotoUrl()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "COMUNIDAD AMBIENTAL",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F7F4))
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        UserAvatar(
                            imageUrl = currentUserPhoto,
                            size = 48.dp
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        OutlinedTextField(
                            value = postText,
                            onValueChange = { postText = it },
                            placeholder = {
                                Text(
                                    text = if (currentUserName.isNotBlank()) {
                                        "¿Qué quieres compartir hoy, $currentUserName?"
                                    } else {
                                        "¿Qué quieres compartir hoy?"
                                    },
                                    color = Color.Gray
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(24.dp),
                            singleLine = true,
                            enabled = !uiState.isPublishing,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent,
                                unfocusedContainerColor = Color(0xFFF4F4F4),
                                focusedContainerColor = Color(0xFFF4F4F4)
                            )
                        )
                    }

                    selectedImageUri?.let { uri ->
                        Spacer(modifier = Modifier.height(12.dp))
                        AsyncImage(
                            model = uri,
                            contentDescription = "Imagen seleccionada",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { imagePickerLauncher.launch("image/*") }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Image,
                                contentDescription = null,
                                tint = Color(0xFF5C7A99),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (selectedImageUri != null) "Cambiar foto" else "Foto / Video",
                                color = Color(0xFF5C7A99),
                                fontSize = 14.sp
                            )
                        }

                        Button(
                            onClick = {
                                if (postText.isBlank() && selectedImageUri == null) {
                                    Toast.makeText(context, "Escribe algo o selecciona una imagen", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                viewModel.createPublicacion(
                                    postText,
                                    selectedImageUri,
                                    onSuccess = {
                                        postText = ""
                                        selectedImageUri = null
                                        Toast.makeText(context, "Publicado", Toast.LENGTH_SHORT).show()
                                    },
                                    onError = { error ->
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            },
                            enabled = !uiState.isPublishing,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                            shape = RoundedCornerShape(50)
                        ) {
                            if (uiState.isPublishing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(text = "Publicar", color = Color.White)
                            }
                        }
                    }

                    uiState.publishError?.let { error ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error,
                            color = Color.Red,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryGreen)
                }
            }

            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            if (uiState.publicaciones.isEmpty() && !uiState.isLoading && uiState.error == null) {
                Text(
                    text = "No hay publicaciones aún",
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            uiState.publicaciones.forEach { publicacion ->
                PostCard(publicacion = publicacion)
                Spacer(modifier = Modifier.height(16.dp))
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
            onComunidadClick = { drawerOpen = false },
            onSteamClick = {
                onSteamClick()
                drawerOpen = false
            },
            onPraeClick = {
                onPraeClick()
                drawerOpen = false
            },
            onNoticiasClick = onNoticiasClick,
            onEcoEstudioClick = onEcoEstudioClick
        )
    }
}

@Composable
private fun UserAvatar(
    imageUrl: String?,
    size: androidx.compose.ui.unit.Dp = 40.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(BrownHeader),
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
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(size * 0.6f)
            )
        }
    }
}

@Composable
private fun PostCard(publicacion: Publicacion) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                UserAvatar(imageUrl = publicacion.userAvatar, size = 40.dp)

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = publicacion.userName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = DarkGreen
                    )
                    Text(
                        text = publicacion.timeAgo,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = publicacion.contenido,
                fontSize = 15.sp,
                color = Color.DarkGray
            )

            if (!publicacion.mediaUrl.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                AsyncImage(
                    model = publicacion.mediaUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
