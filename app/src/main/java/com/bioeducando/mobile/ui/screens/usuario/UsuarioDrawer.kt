package com.bioeducando.mobile.ui.screens.usuario

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bioeducando.mobile.R
import com.bioeducando.mobile.ui.theme.PrimaryGreen

@Composable
fun UsuarioDrawer(
    isOpen: Boolean,
    onClose: () -> Unit,
    onLogout: () -> Unit,
    onRetosClick: () -> Unit = {},
    onComunidadClick: () -> Unit = {},
    onSteamClick: () -> Unit = {},
    onPraeClick: () -> Unit = {},
    onNoticiasClick: () -> Unit = {},
    onEcoEstudioClick: () -> Unit = {}
) {
    if (isOpen) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onClose() }
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    .background(PrimaryGreen)
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {}
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
                        onClick = {
                            onRetosClick()
                            onClose()
                        }
                    )
                    MenuItem(
                        icon = Icons.Filled.Spa,
                        label = "Comunidad Ambiental",
                        onClick = {
                            onComunidadClick()
                            onClose()
                        }
                    )
                    MenuItem(
                        icon = Icons.Filled.Science,
                        label = "Proyectos STEAM",
                        onClick = {
                            onSteamClick()
                            onClose()
                        }
                    )
                    MenuItem(
                        icon = Icons.Filled.MenuBook,
                        label = "Proyectos PRAE",
                        onClick = {
                            onPraeClick()
                            onClose()
                        }
                    )
                    MenuItem(
                        icon = Icons.Filled.Info,
                        label = "Noticias",
                        onClick = {
                            onNoticiasClick()
                            onClose()
                        }
                    )
                    MenuItem(
                        icon = Icons.Filled.Spa,
                        label = "ECO-ESTUDIO",
                        onClick = {
                            onEcoEstudioClick()
                            onClose()
                        }
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
                        Image(
                            painter = painterResource(id = R.drawable.logo_bienvenida),
                            contentDescription = "Bioeducando con Oswal",
                            modifier = Modifier.height(110.dp)
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
