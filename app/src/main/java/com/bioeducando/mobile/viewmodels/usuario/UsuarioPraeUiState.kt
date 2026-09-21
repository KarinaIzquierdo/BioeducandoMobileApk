package com.bioeducando.mobile.viewmodels.usuario

import com.bioeducando.mobile.data.model.admin.PraeActividad
import com.bioeducando.mobile.data.model.admin.PraeDocumento
import com.bioeducando.mobile.data.model.admin.PraeInfo

data class UsuarioPraeUiState(
    val info: PraeInfo = PraeInfo(),
    val actividades: List<PraeActividad> = emptyList(),
    val documentos: List<PraeDocumento> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
