package com.bioeducando.mobile.data.model.admin

data class PraeResponse(
    val info: PraeInfo?,
    val actividades: List<PraeActividad>,
    val documentos: List<PraeDocumento>
)
