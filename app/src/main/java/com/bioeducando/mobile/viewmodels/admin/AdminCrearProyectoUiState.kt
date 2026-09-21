package com.bioeducando.mobile.viewmodels.admin

import android.net.Uri

data class AdminCrearProyectoUiState(
    val titulo: String = "",
    val categoria: String = "",
    val descripcion: String = "",
    val objetivos: String = "",
    val materiales: String = "",
    val impactoAmbiental: String = "",
    val destacado: Boolean = false,
    val imagenUri: Uri? = null,
    val isSaving: Boolean = false,
    val error: String? = null,
    val saved: Boolean = false
)
