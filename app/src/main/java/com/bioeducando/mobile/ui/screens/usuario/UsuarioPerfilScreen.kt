package com.bioeducando.mobile.ui.screens.usuario

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bioeducando.mobile.ui.theme.BioeducandoMobileTheme
import com.bioeducando.mobile.ui.theme.DarkGreen
import com.bioeducando.mobile.viewmodels.admin.AdminPerfilViewModel
import com.bioeducando.mobile.ui.screens.usuario.UsuarioDrawer as UsuarioMenuDrawer
import com.bioeducando.mobile.ui.theme.PrimaryGreen

private val BrownHeader = Color(0xFF8B5E3C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioPerfilScreen(
    onMenuClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onRetosClick: () -> Unit = {},
    onComunidadClick: () -> Unit = {},
    onSteamClick: () -> Unit = {},
    onPraeClick: () -> Unit = {},
    onNoticiasClick: () -> Unit = {},
    onEcoEstudioClick: () -> Unit = {},
    viewModel: AdminPerfilViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var telefono by remember { mutableStateOf("31435388857") }
    var drawerOpen by remember { mutableStateOf(false) }

    var showCurrent by remember { mutableStateOf(false) }
    var showNew by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.saveMessage, uiState.passwordMessage) {
        uiState.saveMessage?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
        uiState.passwordMessage?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MI PERFIL", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { drawerOpen = true }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menú", tint = Color.White)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tarjeta de usuario
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFCCBC)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Avatar",
                            modifier = Modifier.size(70.dp),
                            tint = BrownHeader
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Laura Karina",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B4D3E)
                    )

                    Text(
                        text = "Usuario Bioeducando",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = "Miembro desde: Abr 2026",
                        fontSize = 12.sp,
                        color = Color(0xFF4CAF50)
                    )
                }
            }

            // Datos personales
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    SectionHeader(
                        icon = Icons.AutoMirrored.Filled.List,
                        title = "Datos Personales"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileTextField(
                        value = uiState.nameField,
                        onValueChange = viewModel::onNameChange,
                        label = "Nombre Completo",
                        leadingIcon = Icons.Filled.Person
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileTextField(
                        value = uiState.emailField,
                        onValueChange = viewModel::onEmailChange,
                        label = "Correo Electrónico",
                        leadingIcon = Icons.Filled.Email
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileTextField(
                        value = telefono,
                        onValueChange = { telefono = it },
                        label = "Número Telefónico",
                        leadingIcon = Icons.Filled.Phone
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = viewModel::guardarCambios,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            "Guardar Cambios",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            // Seguridad
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    SectionHeader(
                        icon = Icons.Filled.Lock,
                        title = "Seguridad"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PasswordTextField(
                        value = uiState.currentPassword,
                        onValueChange = viewModel::onCurrentPasswordChange,
                        label = "Contraseña Actual",
                        show = showCurrent,
                        onToggleShow = { showCurrent = !showCurrent }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PasswordTextField(
                        value = uiState.newPassword,
                        onValueChange = viewModel::onNewPasswordChange,
                        label = "Nueva Contraseña",
                        show = showNew,
                        onToggleShow = { showNew = !showNew }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PasswordTextField(
                        value = uiState.confirmPassword,
                        onValueChange = viewModel::onConfirmPasswordChange,
                        label = "Confirmar Nueva Contraseña",
                        show = showConfirm,
                        onToggleShow = { showConfirm = !showConfirm }
                    )

                    Text(
                        text = "Repite tu nueva contraseña",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = viewModel::actualizarContrasena,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            "Actualizar Contraseña",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }

    if (drawerOpen) {
        UsuarioMenuDrawer(
            isOpen = drawerOpen,
            onClose = { drawerOpen = false },
            onLogout = onLogoutClick,
            onRetosClick = onRetosClick,
            onComunidadClick = onComunidadClick,
            onSteamClick = onSteamClick,
            onPraeClick = onPraeClick,
            onNoticiasClick = onNoticiasClick,
            onEcoEstudioClick = onEcoEstudioClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UsuarioDrawer(
    isOpen: Boolean,
    onClose: () -> Unit,
    onLogout: () -> Unit
) {
    if (isOpen) {
        Dialog(
            onDismissRequest = onClose,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f)
                        .background(PrimaryGreen)
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Usuario",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    MenuItem(
                        icon = Icons.Filled.Spa,
                        label = "Retos Ecológicos",
                        onClick = { onClose() }
                    )
                    MenuItem(
                        icon = Icons.Filled.Spa,
                        label = "Comunidad Ambiental",
                        onClick = { onClose() }
                    )
                    MenuItem(
                        icon = Icons.Filled.Science,
                        label = "Proyectos STEAM",
                        onClick = { onClose() }
                    )
                    MenuItem(
                        icon = Icons.Filled.MenuBook,
                        label = "Proyectos PRAE",
                        onClick = { onClose() }
                    )
                    MenuItem(
                        icon = Icons.Filled.Settings,
                        label = "Configuración",
                        onClick = { onClose() }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Logo",
                            modifier = Modifier.size(72.dp),
                            tint = Color.White
                        )
                        Text(
                            text = "BIOEDUCANDO",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "con Oswal",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onLogout()
                            onClose()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "cerrar sesión",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun SectionHeader(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF1B4D3E))
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B4D3E)
        )
    }
}

@Composable
private fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        leadingIcon = {
            Icon(imageVector = leadingIcon, contentDescription = null, tint = PrimaryGreen)
        },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryGreen,
            focusedLabelColor = PrimaryGreen
        )
    )
}

@Composable
private fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    show: Boolean,
    onToggleShow: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        leadingIcon = {
            Icon(Icons.Filled.Lock, contentDescription = null, tint = PrimaryGreen)
        },
        trailingIcon = {
            IconButton(onClick = onToggleShow) {
                Icon(
                    imageVector = if (show) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = if (show) "Ocultar" else "Mostrar"
                )
            }
        },
        visualTransformation = if (show) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryGreen,
            focusedLabelColor = PrimaryGreen
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun UsuarioPerfilScreenPreview() {
    BioeducandoMobileTheme {
        UsuarioPerfilScreen()
    }
}
