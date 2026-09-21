package com.bioeducando.mobile.ui.screens.admin

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Person
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bioeducando.mobile.data.model.admin.RecentActivity
import com.bioeducando.mobile.ui.theme.BioeducandoMobileTheme
import com.bioeducando.mobile.ui.theme.PrimaryGreen
import com.bioeducando.mobile.viewmodels.admin.AdminDashboardUiState
import com.bioeducando.mobile.viewmodels.admin.AdminDashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: AdminDashboardViewModel = viewModel(),
    onMenuClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "DASHBOARD",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menú"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF8B5E3C),
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
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
            item {
                WelcomeCard()
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                StatsSection(uiState)
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Text(
                    text = "Actividad del Sistema",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B4D3E),
                    modifier = Modifier.padding(bottom = 12.dp)
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

            if (uiState.error != null) {
                item {
                    Text(
                        text = uiState.error.orEmpty(),
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            items(uiState.activities) { activity ->
                ActivityItem(activity = activity)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
        }
    }
}

@Composable
private fun WelcomeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B4D3E))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "¡Bienvenido,",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Administrador!",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Gestiona la educación ambiental y supervisa el impacto de la comunidad.",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun StatsSection(uiState: AdminDashboardUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        StatCard(
            icon = Icons.Outlined.Person,
            iconBackground = Color(0xFFE8F5E9),
            iconTint = PrimaryGreen,
            count = uiState.usersCount,
            label = "Usuarios Registrados"
        )
        StatCard(
            icon = Icons.Filled.LocationOn,
            iconBackground = Color(0xFFFFF3E0),
            iconTint = Color(0xFF8B5E3C),
            count = uiState.missionsCount,
            label = "Misiones Activas"
        )
        StatCard(
            icon = Icons.Filled.MailOutline,
            iconBackground = Color(0xFFE3F2FD),
            iconTint = Color(0xFF1976D2),
            count = uiState.interactionsCount,
            label = "Interacciones Hoy"
        )
    }
}

@Composable
private fun StatCard(
    icon: ImageVector,
    iconBackground: Color,
    iconTint: Color,
    count: Int,
    label: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = count.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B4D3E)
                )
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun ActivityItem(activity: RecentActivity) {
    val dotColor = if (activity.type == "publication") Color(0xFF1976D2) else PrimaryGreen

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(
                text = activity.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B4D3E)
            )
            Text(
                text = activity.description,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = activity.timeAgo,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Preview
@Composable
private fun AdminDashboardScreenPreview() {
    BioeducandoMobileTheme {
        AdminDashboardScreen()
    }
}
