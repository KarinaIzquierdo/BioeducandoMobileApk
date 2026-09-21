package com.bioeducando.mobile.repositories.usuario

import com.bioeducando.mobile.data.model.admin.Noticia
import com.bioeducando.mobile.data.model.admin.ProyectoSteam
import com.bioeducando.mobile.data.model.admin.SteamResponse
import com.bioeducando.mobile.data.remote.admin.AdminApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class UsuarioSteamRepository(
    private val apiService: AdminApiService
) {

    suspend fun getSteamProyectos(token: String): Response<SteamResponse> {
        return apiService.getSteamProyectos("Bearer $token")
    }

    suspend fun getNoticias(token: String): Response<List<Noticia>> {
        return apiService.getAllNoticias("Bearer $token")
    }

    suspend fun createSteamProyecto(
        token: String,
        titulo: RequestBody,
        categoria: RequestBody,
        descripcion: RequestBody,
        objetivos: RequestBody?,
        materiales: RequestBody?,
        impactoAmbiental: RequestBody?,
        imagen: MultipartBody.Part?,
        destacado: RequestBody?
    ): Response<ProyectoSteam> {
        return apiService.createSteamProyecto(
            "Bearer $token",
            titulo,
            categoria,
            descripcion,
            objetivos,
            materiales,
            impactoAmbiental,
            imagen,
            destacado
        )
    }
}
