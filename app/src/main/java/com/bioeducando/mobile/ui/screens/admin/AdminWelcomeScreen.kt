package com.bioeducando.mobile.ui.screens.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bioeducando.mobile.R
import com.bioeducando.mobile.ui.components.admin.AdminWelcomeBackground
import com.bioeducando.mobile.ui.theme.BioeducandoMobileTheme
import com.bioeducando.mobile.ui.theme.DarkGreen

@Composable
fun AdminWelcomeScreen(
    modifier: Modifier = Modifier,
    onStartClick: () -> Unit = {},
    onComunidadClick: () -> Unit = {},
    onNoticiasClick: () -> Unit = {},
    onRetosClick: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AdminWelcomeBackground()

        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .offset(y = (-80).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_bienvenida),
                contentDescription = "Logo Bioeducando",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(280.dp)
                    .height(280.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onStartClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkGreen,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .width(240.dp)
                    .height(56.dp)
            ) {
                Text(
                    text = "Comenzar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Text(
            text = "TODO EMPIEZA DESDE CASA",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        var expanded by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (expanded) {
                PublicOption("Comunidad Ambiental", Icons.Filled.Spa, onComunidadClick)
                PublicOption("Noticias Ambientales", Icons.Filled.Notifications, onNoticiasClick)
                PublicOption("Retos Ecológicos", Icons.Filled.EmojiEvents, onRetosClick)
            }
            FloatingActionButton(
                onClick = { expanded = !expanded },
                containerColor = DarkGreen,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.Close else Icons.Filled.Add,
                    contentDescription = "Explorar"
                )
            }
        }
    }
}

@Composable
private fun PublicOption(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        containerColor = Color.White,
        contentColor = DarkGreen,
        icon = { Icon(icon, contentDescription = label) },
        text = { Text(label) }
    )
}

@Preview(showBackground = true)
@Composable
private fun AdminWelcomeScreenPreview() {
    BioeducandoMobileTheme {
        AdminWelcomeScreen()
    }
}
