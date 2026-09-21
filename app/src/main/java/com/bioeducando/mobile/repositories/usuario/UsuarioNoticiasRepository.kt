package com.bioeducando.mobile.repositories.usuario

import com.bioeducando.mobile.data.model.admin.Noticia
import com.bioeducando.mobile.data.model.admin.NoticiaComentario
import com.bioeducando.mobile.data.model.admin.NoticiaLikeResponse
import com.bioeducando.mobile.data.remote.admin.AdminApiService
import retrofit2.Response

class UsuarioNoticiasRepository(
    private val apiService: AdminApiService
) {

    suspend fun getNoticias(token: String): Response<List<Noticia>> {
        return apiService.getAllNoticias("Bearer $token")
    }

    suspend fun toggleLike(token: String, noticiaId: Int): Response<NoticiaLikeResponse> {
        return apiService.toggleNoticiaLike("Bearer $token", noticiaId)
    }

    suspend fun getComentarios(token: String, noticiaId: Int): Response<List<NoticiaComentario>> {
        return apiService.getNoticiaComentarios("Bearer $token", noticiaId)
    }

    suspend fun comentar(token: String, noticiaId: Int, comentario: String): Response<NoticiaComentario> {
        return apiService.storeNoticiaComentario("Bearer $token", noticiaId, comentario)
    }
}
