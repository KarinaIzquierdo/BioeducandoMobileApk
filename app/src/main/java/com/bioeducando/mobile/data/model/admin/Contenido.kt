package com.bioeducando.mobile.data.model.admin

import com.google.gson.annotations.SerializedName

data class Contenido(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("file_url") val fileUrl: String?,
    @SerializedName("pdf_url") val pdfUrl: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("time_ago") val timeAgo: String?
)
