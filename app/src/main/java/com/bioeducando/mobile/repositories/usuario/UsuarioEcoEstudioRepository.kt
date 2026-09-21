package com.bioeducando.mobile.repositories.usuario

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.bioeducando.mobile.data.model.admin.Contenido
import com.bioeducando.mobile.data.remote.admin.AdminApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class UsuarioEcoEstudioRepository(
    private val context: Context,
    private val apiService: AdminApiService
) {

    suspend fun getContenidos(token: String): Response<List<Contenido>> {
        return apiService.getContenidos("Bearer $token")
    }

    suspend fun publicar(
        token: String,
        descripcion: String,
        archivoUri: Uri,
        pdfUri: Uri?
    ): Response<Contenido> {
        val archivoPart = uriToMultipart(archivoUri, "file")
        val pdfPart = pdfUri?.let { uriToMultipart(it, "pdf") }
        val descripcionBody = descripcion.toRequestBody("text/plain".toMediaTypeOrNull())
        return apiService.createContenido("Bearer $token", descripcionBody, archivoPart, pdfPart)
    }

    private fun uriToMultipart(uri: Uri, partName: String): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val mime = contentResolver.getType(uri) ?: "application/octet-stream"
        val displayName = contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (idx >= 0) cursor.getString(idx) else null
            } else null
        } ?: "archivo_${System.currentTimeMillis()}"
        val input = contentResolver.openInputStream(uri)
            ?: throw IllegalStateException("No se pudo leer el archivo")
        val tempFile = File(context.cacheDir, displayName)
        tempFile.outputStream().use { output -> input.copyTo(output) }
        val requestBody = tempFile.asRequestBody(mime.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, displayName, requestBody)
    }
}
