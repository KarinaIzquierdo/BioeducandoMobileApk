package com.bioeducando.mobile.data.model.admin

data class PraeDocumento(
    val id: Int,
    val titulo: String,
    val archivoPath: String?,
    val archivoUrl: String?,
    val createdAt: String?
)
