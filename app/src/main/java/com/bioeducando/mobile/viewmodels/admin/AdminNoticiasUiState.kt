package com.bioeducando.mobile.viewmodels.admin

import com.bioeducando.mobile.data.model.admin.Noticia

data class AdminNoticiasUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val noticias: List<Noticia> = emptyList(),

    val showCreateDialog: Boolean = false,
    val editingId: Int? = null,
    val isCreating: Boolean = false,
    val createError: String? = null,

    val titulo: String = "",
    val entradilla: String = "",
    val cuerpo: String = "",
    val categoria: String = "",
    val fechaPublicacion: String = "",
    val estado: String = "activa",
    val imagenUrl: String = ""
)
