package com.bioeducando.mobile.ui.screens.admin

import android.content.Intent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bioeducando.mobile.ui.theme.PrimaryGreen
import com.bioeducando.mobile.viewmodels.admin.AdminPraeUiState
import com.bioeducando.mobile.viewmodels.admin.AdminPraeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPraeScreen(
    onMenuClick: () -> Unit = {},
    viewModel: AdminPraeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var descripcion by remember { mutableStateOf("") }
    var objetivos by remember { mutableStateOf("") }
    var showActividadDialog by remember { mutableStateOf(false) }
    var showDocumentoDialog by remember { mutableStateOf(false) }

    val pdfPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.setDocumentoUri(uri)
    }

    LaunchedEffect(Unit) {
        viewModel.loadPrae()
    }

    LaunchedEffect(uiState.info) {
        descripcion = uiState.info?.descripcion ?: ""
        objetivos = uiState.info?.objetivos ?: ""
    }

    LaunchedEffect(uiState.infoSaved) {
        if (uiState.infoSaved) {
            Toast.makeText(context, "Información guardada", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uiState.actividadSaved) {
        if (uiState.actividadSaved) {
            Toast.makeText(context, "Actividad guardada", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uiState.documentoSaved) {
        if (uiState.documentoSaved) {
            Toast.makeText(context, "Documento subido", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "GESTIONAR PRAE",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
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
                .background(Color(0xFFF4F7F4))
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Información del PRAE",
                        color = Color(0xFF1B4D3E),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Descripción General",
                                color = Color(0xFF1B4D3E),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = descripcion,
                                onValueChange = { descripcion = it },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3,
                                maxLines = 5,
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Objetivos del Proyecto",
                                color = Color(0xFF1B4D3E),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = objetivos,
                                onValueChange = { objetivos = it },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3,
                                maxLines = 5,
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.updateInfo(descripcion, objetivos) },
                                enabled = !uiState.isSavingInfo,
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                if (uiState.isSavingInfo) {
                                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                                } else {
                                    Text("Guardar Información", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = PrimaryGreen,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "Actividades",
                                color = Color(0xFF1B4D3E),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                        Button(
                            onClick = { showActividadDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Nueva Actividad", fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("TITULO", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp, modifier = Modifier.weight(1.2f))
                                Text("FECHA", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp, modifier = Modifier.weight(0.9f))
                                Text("ESTADO", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp, modifier = Modifier.weight(0.9f))
                                Text("", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp, modifier = Modifier.width(40.dp))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            if (uiState.actividades.isEmpty()) {
                                Text("No hay actividades", color = Color.Gray, fontSize = 14.sp)
                            } else {
                                uiState.actividades.forEach { act ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(act.titulo, fontSize = 13.sp, modifier = Modifier.weight(1.2f))
                                        Text(act.fecha, fontSize = 13.sp, modifier = Modifier.weight(0.9f))
                                        EstadoBadge(act.estado)
                                        IconButton(
                                            onClick = { viewModel.deleteActividad(act.id) },
                                            modifier = Modifier.width(40.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Delete,
                                                contentDescription = "Eliminar",
                                                tint = Color.Red
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = null,
                                tint = PrimaryGreen,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "Documentos y Guías",
                                color = Color(0xFF1B4D3E),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { showDocumentoDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Subir Documento", fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("TITULO", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp, modifier = Modifier.weight(1.5f))
                                Text("FECHA SUBIDA", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp, modifier = Modifier.weight(1f))
                                Text("", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp, modifier = Modifier.width(80.dp))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            if (uiState.documentos.isEmpty()) {
                                Text("No hay documentos", color = Color.Gray, fontSize = 14.sp)
                            } else {
                                uiState.documentos.forEach { doc ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(doc.titulo, fontSize = 13.sp, modifier = Modifier.weight(1.5f))
                                        Text(doc.createdAt ?: "", fontSize = 13.sp, modifier = Modifier.weight(1f))
                                        Row(
                                            modifier = Modifier.width(80.dp),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            IconButton(
                                                onClick = {
                                                    doc.archivoUrl?.let { url ->
                                                        val full = if (url.startsWith("http")) url else "http://10.0.2.2:8000$url"
                                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(full)))
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.KeyboardArrowDown,
                                                    contentDescription = "Descargar",
                                                    tint = PrimaryGreen
                                                )
                                            }
                                            IconButton(
                                                onClick = { viewModel.deleteDocumento(doc.id) }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Delete,
                                                    contentDescription = "Eliminar",
                                                    tint = Color.Red
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (uiState.error != null) {
                    item {
                        Text(
                            text = uiState.error ?: "",
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            if (showActividadDialog) {
                ActividadDialog(
                    viewModel = viewModel,
                    uiState = uiState,
                    onDismiss = {
                        showActividadDialog = false
                        viewModel.clearActividadForm()
                    }
                )
            }

            if (showDocumentoDialog) {
                DocumentoDialog(
                    viewModel = viewModel,
                    uiState = uiState,
                    onPickPdf = { pdfPicker.launch("application/pdf") },
                    onDismiss = {
                        showDocumentoDialog = false
                        viewModel.setDocumentoTitulo("")
                        viewModel.setDocumentoUri(null)
                    }
                )
            }
        }
    }
}

@Composable
private fun EstadoBadge(estado: String) {
    val isFinalizada = estado.equals("finalizada", ignoreCase = true)
    Box(
        modifier = Modifier
            .background(
                color = if (isFinalizada) Color(0xFFE8F5E9) else Color(0xFFFFF8E1),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isFinalizada) "FINALIZADA" else "PRÓXIMA",
            color = if (isFinalizada) Color(0xFF2E7D32) else Color(0xFFFF9800),
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun ActividadDialog(
    viewModel: AdminPraeViewModel,
    uiState: AdminPraeUiState,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Actividad", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = uiState.actividadTitulo,
                    onValueChange = viewModel::setActividadTitulo,
                    label = { Text("Título") },
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = uiState.actividadDescripcion,
                    onValueChange = viewModel::setActividadDescripcion,
                    label = { Text("Descripción") },
                    minLines = 3,
                    maxLines = 4,
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = uiState.actividadFecha,
                    onValueChange = viewModel::setActividadFecha,
                    label = { Text("Fecha (dd/mm/aaaa)") },
                    shape = RoundedCornerShape(12.dp)
                )
                Text("Estado", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = uiState.actividadEstado == "proxima",
                        onClick = { viewModel.setActividadEstado("proxima") }
                    )
                    Text("Próxima")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = uiState.actividadEstado == "finalizada",
                        onClick = { viewModel.setActividadEstado("finalizada") }
                    )
                    Text("Finalizada")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.createActividad()
                    if (!uiState.isSavingActividad) onDismiss()
                },
                enabled = !uiState.isSavingActividad,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                if (uiState.isSavingActividad) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                else Text("Guardar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun DocumentoDialog(
    viewModel: AdminPraeViewModel,
    uiState: AdminPraeUiState,
    onPickPdf: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Subir Documento", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = uiState.documentoTitulo,
                    onValueChange = viewModel::setDocumentoTitulo,
                    label = { Text("Título del documento") },
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedButton(
                    onClick = onPickPdf,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF8B5E3C))
                ) {
                    Text(
                        if (uiState.documentoUri != null) "Cambiar PDF" else "Seleccionar PDF",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.createDocumento()
                    if (!uiState.isSavingDocumento) onDismiss()
                },
                enabled = !uiState.isSavingDocumento,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                if (uiState.isSavingDocumento) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                else Text("Subir")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
