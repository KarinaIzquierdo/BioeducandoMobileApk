package com.bioeducando.mobile.viewmodels.admin

import android.net.Uri
import com.bioeducando.mobile.data.model.admin.PraeActividad
import com.bioeducando.mobile.data.model.admin.PraeDocumento
import com.bioeducando.mobile.data.model.admin.PraeInfo

data class AdminPraeUiState(
    val info: PraeInfo = PraeInfo(),
    val actividades: List<PraeActividad> = emptyList(),
    val documentos: List<PraeDocumento> = emptyList(),
    val isLoading: Boolean = false,
    val isSavingInfo: Boolean = false,
    val isSavingActividad: Boolean = false,
    val isSavingDocumento: Boolean = false,
    val error: String? = null,
    val infoSaved: Boolean = false,
    val actividadSaved: Boolean = false,
    val documentoSaved: Boolean = false,
    val documentoUri: Uri? = null,
    val documentoTitulo: String = "",
    val actividadTitulo: String = "",
    val actividadDescripcion: String = "",
    val actividadFecha: String = "",
    val actividadEstado: String = "proxima"
)
