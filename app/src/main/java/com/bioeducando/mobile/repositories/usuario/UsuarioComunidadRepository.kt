package com.bioeducando.mobile.repositories.usuario

import com.bioeducando.mobile.data.model.admin.Publicacion
import com.bioeducando.mobile.data.model.admin.User
import com.bioeducando.mobile.data.remote.admin.AdminApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class UsuarioComunidadRepository(
    private val apiService: AdminApiService
) {

    suspend fun getPublicaciones(token: String): Response<List<Publicacion>> {
        return apiService.getPublicaciones("Bearer $token")
    }

    suspend fun createPublicacion(
        token: String,
        contenido: RequestBody,
        media: MultipartBody.Part?
    ): Response<Publicacion> {
        return apiService.createPublicacion("Bearer $token", contenido, media)
    }

    suspend fun getPerfil(token: String): Response<User> {
        return apiService.getPerfil("Bearer $token")
    }
}
