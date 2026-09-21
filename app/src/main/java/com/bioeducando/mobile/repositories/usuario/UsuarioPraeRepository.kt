package com.bioeducando.mobile.repositories.usuario

import com.bioeducando.mobile.data.model.admin.PraeResponse
import com.bioeducando.mobile.data.remote.admin.AdminApiService
import retrofit2.Response

class UsuarioPraeRepository(
    private val apiService: AdminApiService
) {

    suspend fun getPrae(token: String): Response<PraeResponse> {
        return apiService.getPrae("Bearer $token")
    }
}
