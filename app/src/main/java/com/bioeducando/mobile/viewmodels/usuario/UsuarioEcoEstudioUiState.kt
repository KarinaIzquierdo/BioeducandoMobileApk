package com.bioeducando.mobile.viewmodels.usuario

import android.net.Uri
import com.bioeducando.mobile.data.model.admin.Contenido

data class UsuarioEcoEstudioUiState(
    val descripcion: String = "",
    val archivoUri: Uri? = null,
    val pdfUri: Uri? = null,
    val contenidos: List<Contenido> = emptyList(),
    val isLoading: Boolean = false,
    val isPublicando: Boolean = false,
    val error: String? = null,
    val exito: String? = null
)
