package com.bioeducando.mobile.repositories.admin

import com.bioeducando.mobile.data.model.admin.AuthResponse
import com.bioeducando.mobile.data.model.admin.DashboardResponse
import com.bioeducando.mobile.data.model.admin.LoginRequest
import com.bioeducando.mobile.data.model.admin.PraeActividad
import com.bioeducando.mobile.data.model.admin.PraeDocumento
import com.bioeducando.mobile.data.model.admin.PraeResponse
import com.bioeducando.mobile.data.model.admin.Publicacion
import com.bioeducando.mobile.data.model.admin.Role
import com.bioeducando.mobile.data.model.admin.ProyectoSteam
import com.bioeducando.mobile.data.model.admin.RegisterRequest
import com.bioeducando.mobile.data.model.admin.Reto
import com.bioeducando.mobile.data.model.admin.RetoUpdateRequest
import com.bioeducando.mobile.data.model.admin.Comentario
import com.bioeducando.mobile.data.model.admin.LikeResponse
import com.bioeducando.mobile.data.model.admin.Noticia
import com.bioeducando.mobile.data.model.admin.SteamResponse
import com.bioeducando.mobile.data.model.admin.User
import com.bioeducando.mobile.data.remote.admin.AdminApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class AdminRepository(private val apiService: AdminApiService) {

    suspend fun login(request: LoginRequest): Response<AuthResponse> {
        return apiService.login(request)
    }

    suspend fun register(request: RegisterRequest): Response<AuthResponse> {
        return apiService.register(request)
    }

    suspend fun getDashboard(token: String): Response<DashboardResponse> {
        return apiService.getAdminDashboard("Bearer $token")
    }

    suspend fun getRetos(token: String): Response<List<Reto>> {
        return apiService.getRetos("Bearer $token")
    }

    suspend fun getReto(token: String, id: Int): Response<Reto> {
        return apiService.getReto("Bearer $token", id)
    }

    suspend fun updateReto(token: String, id: Int, reto: RetoUpdateRequest): Response<Reto> {
        return apiService.updateReto("Bearer $token", id, reto)
    }

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

    suspend fun toggleLike(token: String, id: Int, action: String): Response<LikeResponse> {
        return apiService.toggleLike("Bearer $token", id, action)
    }

    suspend fun deletePublicacion(token: String, id: Int): Response<Unit> {
        return apiService.deletePublicacion("Bearer $token", id)
    }

    suspend fun getComentarios(token: String, id: Int): Response<List<Comentario>> {
        return apiService.getComentarios("Bearer $token", id)
    }

    suspend fun storeComentario(token: String, id: Int, contenido: String): Response<Comentario> {
        return apiService.storeComentario("Bearer $token", id, contenido)
    }

    suspend fun getUsers(token: String): Response<List<User>> {
        return apiService.getAllUsers("Bearer $token")
    }

    suspend fun getRoles(token: String): Response<List<Role>> {
        return apiService.getAllRoles("Bearer $token")
    }

    suspend fun createUser(
        token: String,
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String,
        roleId: Int
    ): Response<User> {
        return apiService.createUser(
            "Bearer $token",
            name,
            email,
            password,
            passwordConfirmation,
            roleId
        )
    }

    suspend fun getNoticias(token: String): Response<List<Noticia>> {
        return apiService.getAllNoticias("Bearer $token")
    }

    suspend fun createNoticia(
        token: String,
        titulo: String,
        entradilla: String,
        cuerpo: String,
        categoria: String,
        fechaPublicacion: String,
        estado: String,
        imagen: String?
    ): Response<Noticia> {
        val toBody: (String) -> okhttp3.RequestBody = { it.toRequestBody("text/plain".toMediaTypeOrNull()) }
        return apiService.createNoticia(
            "Bearer $token",
            toBody(titulo),
            toBody(entradilla),
            toBody(cuerpo),
            toBody(categoria),
            toBody(fechaPublicacion),
            toBody(estado),
            imagen?.let { toBody(it) }
        )
    }

    suspend fun updateNoticia(
        token: String,
        id: Int,
        titulo: String,
        entradilla: String,
        cuerpo: String,
        categoria: String,
        fechaPublicacion: String,
        estado: String,
        imagen: String?
    ): Response<Noticia> {
        val toBody: (String) -> okhttp3.RequestBody = { it.toRequestBody("text/plain".toMediaTypeOrNull()) }
        return apiService.updateNoticia(
            "Bearer $token",
            id,
            toBody(titulo),
            toBody(entradilla),
            toBody(cuerpo),
            toBody(categoria),
            toBody(fechaPublicacion),
            toBody(estado),
            imagen?.let { toBody(it) }
        )
    }

    suspend fun deleteNoticia(token: String, id: Int): Response<Unit> {
        return apiService.deleteNoticia("Bearer $token", id)
    }

    suspend fun getSteamProyectos(token: String): Response<SteamResponse> {
        return apiService.getSteamProyectos("Bearer $token")
    }

    suspend fun updateSteamEstado(token: String, id: Int, estado: String): Response<ProyectoSteam> {
        return apiService.updateSteamEstado("Bearer $token", id, estado)
    }

    suspend fun deleteSteamProyecto(token: String, id: Int): Response<Unit> {
        return apiService.deleteSteamProyecto("Bearer $token", id)
    }

    suspend fun createSteamProyecto(
        token: String,
        titulo: okhttp3.RequestBody,
        categoria: okhttp3.RequestBody,
        descripcion: okhttp3.RequestBody,
        objetivos: okhttp3.RequestBody?,
        materiales: okhttp3.RequestBody?,
        impactoAmbiental: okhttp3.RequestBody?,
        imagen: okhttp3.MultipartBody.Part?,
        destacado: okhttp3.RequestBody?
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

    suspend fun getPrae(token: String): Response<PraeResponse> {
        return apiService.getPrae("Bearer $token")
    }

    suspend fun updatePraeInfo(token: String, descripcion: String, objetivos: String): Response<PraeResponse> {
        return apiService.updatePraeInfo("Bearer $token", descripcion, objetivos)
    }

    suspend fun createPraeActividad(
        token: String,
        titulo: String,
        descripcion: String,
        fecha: String,
        estado: String
    ): Response<PraeActividad> {
        return apiService.createPraeActividad("Bearer $token", titulo, descripcion, fecha, estado)
    }

    suspend fun updatePraeActividad(
        token: String,
        id: Int,
        titulo: String,
        descripcion: String,
        fecha: String,
        estado: String
    ): Response<PraeActividad> {
        return apiService.updatePraeActividad("Bearer $token", id, titulo, descripcion, fecha, estado)
    }

    suspend fun deletePraeActividad(token: String, id: Int): Response<Unit> {
        return apiService.deletePraeActividad("Bearer $token", id)
    }

    suspend fun createPraeDocumento(
        token: String,
        titulo: okhttp3.RequestBody,
        archivo: okhttp3.MultipartBody.Part
    ): Response<PraeDocumento> {
        return apiService.createPraeDocumento("Bearer $token", titulo, archivo)
    }

    suspend fun deletePraeDocumento(token: String, id: Int): Response<Unit> {
        return apiService.deletePraeDocumento("Bearer $token", id)
    }

    suspend fun getPerfil(token: String): Response<User> {
        return apiService.getPerfil("Bearer $token")
    }

    suspend fun updatePerfil(token: String, name: String, email: String): Response<User> {
        return apiService.updatePerfil("Bearer $token", name, email)
    }

    suspend fun updatePerfilPassword(
        token: String,
        currentPassword: String,
        password: String,
        passwordConfirmation: String
    ): Response<Unit> {
        return apiService.updatePerfilPassword(
            "Bearer $token",
            currentPassword,
            password,
            passwordConfirmation
        )
    }
}
