package com.bioeducando.mobile.data.remote.admin

import com.bioeducando.mobile.data.model.admin.AuthResponse
import com.bioeducando.mobile.data.model.admin.Comentario
import com.bioeducando.mobile.data.model.admin.DashboardResponse
import com.bioeducando.mobile.data.model.admin.LikeResponse
import com.bioeducando.mobile.data.model.admin.LoginRequest
import com.bioeducando.mobile.data.model.admin.PraeActividad
import com.bioeducando.mobile.data.model.admin.PraeDocumento
import com.bioeducando.mobile.data.model.admin.PraeResponse
import com.bioeducando.mobile.data.model.admin.ProyectoSteam
import com.bioeducando.mobile.data.model.admin.Publicacion
import com.bioeducando.mobile.data.model.admin.RegisterRequest
import com.bioeducando.mobile.data.model.admin.Contenido
import com.bioeducando.mobile.data.model.admin.Role
import com.bioeducando.mobile.data.model.admin.Reto
import com.bioeducando.mobile.data.model.admin.RetoUpdateRequest
import com.bioeducando.mobile.data.model.admin.Noticia
import com.bioeducando.mobile.data.model.admin.NoticiaComentario
import com.bioeducando.mobile.data.model.admin.NoticiaLikeResponse
import com.bioeducando.mobile.data.model.admin.SteamResponse
import com.bioeducando.mobile.data.model.admin.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AdminApiService {

    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @GET("api/admin/dashboard")
    suspend fun getAdminDashboard(@Header("Authorization") token: String): Response<DashboardResponse>

    @GET("api/admin/retos")
    suspend fun getRetos(@Header("Authorization") token: String): Response<List<Reto>>

    @GET("api/public/retos")
    suspend fun getPublicRetos(): Response<List<Reto>>

    @GET("api/public/publicaciones")
    suspend fun getPublicPublicaciones(): Response<List<Publicacion>>

    @FormUrlEncoded
    @POST("api/public/publicaciones/{id}/like")
    suspend fun togglePublicLike(
        @Path("id") id: Int,
        @Field("action") action: String
    ): Response<LikeResponse>

    @GET("api/public/publicaciones/{id}/comentarios")
    suspend fun getPublicComentarios(@Path("id") id: Int): Response<List<Comentario>>

    @GET("api/public/noticias")
    suspend fun getPublicNoticias(): Response<List<Noticia>>

    @FormUrlEncoded
    @POST("api/public/noticias/{id}/like")
    suspend fun togglePublicNoticiaLike(
        @Path("id") id: Int,
        @Field("action") action: String
    ): Response<LikeResponse>

    @GET("api/admin/retos/{id}")
    suspend fun getReto(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Reto>

    @PUT("api/admin/retos/{id}")
    suspend fun updateReto(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body reto: RetoUpdateRequest
    ): Response<Reto>

    @GET("api/admin/publicaciones")
    suspend fun getPublicaciones(@Header("Authorization") token: String): Response<List<Publicacion>>

    @Multipart
    @POST("api/admin/publicaciones")
    suspend fun createPublicacion(
        @Header("Authorization") token: String,
        @Part("contenido") contenido: RequestBody,
        @Part media: MultipartBody.Part?
    ): Response<Publicacion>

    @FormUrlEncoded
    @POST("api/admin/publicaciones/{id}/like")
    suspend fun toggleLike(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("action") action: String
    ): Response<LikeResponse>

    @GET("api/admin/publicaciones/{id}/comentarios")
    suspend fun getComentarios(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<List<Comentario>>

    @DELETE("api/admin/publicaciones/{id}")
    suspend fun deletePublicacion(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    @FormUrlEncoded
    @POST("api/admin/publicaciones/{id}/comentarios")
    suspend fun storeComentario(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("contenido") contenido: String
    ): Response<Comentario>

    @GET("api/admin/users")
    suspend fun getAllUsers(@Header("Authorization") token: String): Response<List<User>>

    @GET("api/admin/users/roles")
    suspend fun getAllRoles(@Header("Authorization") token: String): Response<List<Role>>

    @FormUrlEncoded
    @POST("api/admin/users")
    suspend fun createUser(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password_confirmation") passwordConfirmation: String,
        @Field("role_id") roleId: Int
    ): Response<User>

    @GET("api/admin/noticias")
    suspend fun getAllNoticias(@Header("Authorization") token: String): Response<List<Noticia>>

    @Multipart
    @POST("api/admin/noticias")
    suspend fun createNoticia(
        @Header("Authorization") token: String,
        @Part("titulo") titulo: RequestBody,
        @Part("entradilla") entradilla: RequestBody,
        @Part("cuerpo") cuerpo: RequestBody,
        @Part("categoria") categoria: RequestBody,
        @Part("fecha_publicacion") fechaPublicacion: RequestBody,
        @Part("estado") estado: RequestBody,
        @Part("imagen") imagen: RequestBody?
    ): Response<Noticia>

    @Multipart
    @PUT("api/admin/noticias/{id}")
    suspend fun updateNoticia(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part("titulo") titulo: RequestBody,
        @Part("entradilla") entradilla: RequestBody,
        @Part("cuerpo") cuerpo: RequestBody,
        @Part("categoria") categoria: RequestBody,
        @Part("fecha_publicacion") fechaPublicacion: RequestBody,
        @Part("estado") estado: RequestBody,
        @Part("imagen") imagen: RequestBody?
    ): Response<Noticia>

    @DELETE("api/admin/noticias/{id}")
    suspend fun deleteNoticia(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    @POST("api/admin/noticias/{id}/like")
    suspend fun toggleNoticiaLike(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<NoticiaLikeResponse>

    @GET("api/admin/noticias/{id}/comentarios")
    suspend fun getNoticiaComentarios(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<List<NoticiaComentario>>

    @FormUrlEncoded
    @POST("api/admin/noticias/{id}/comentarios")
    suspend fun storeNoticiaComentario(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("comentario") comentario: String
    ): Response<NoticiaComentario>

    @GET("api/admin/steam")
    suspend fun getSteamProyectos(@Header("Authorization") token: String): Response<SteamResponse>

    @Multipart
    @POST("api/admin/steam")
    suspend fun createSteamProyecto(
        @Header("Authorization") token: String,
        @Part("titulo") titulo: RequestBody,
        @Part("categoria") categoria: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("objetivos") objetivos: RequestBody?,
        @Part("materiales") materiales: RequestBody?,
        @Part("impacto_ambiental") impactoAmbiental: RequestBody?,
        @Part imagen: MultipartBody.Part?,
        @Part("destacado") destacado: RequestBody?
    ): Response<ProyectoSteam>

    @FormUrlEncoded
    @PATCH("api/admin/steam/{id}/estado")
    suspend fun updateSteamEstado(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("estado") estado: String
    ): Response<ProyectoSteam>

    @DELETE("api/admin/steam/{id}")
    suspend fun deleteSteamProyecto(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    @GET("api/admin/prae")
    suspend fun getPrae(@Header("Authorization") token: String): Response<PraeResponse>

    @FormUrlEncoded
    @POST("api/admin/prae/info")
    suspend fun updatePraeInfo(
        @Header("Authorization") token: String,
        @Field("descripcion") descripcion: String,
        @Field("objetivos") objetivos: String
    ): Response<PraeResponse>

    @FormUrlEncoded
    @POST("api/admin/prae/actividades")
    suspend fun createPraeActividad(
        @Header("Authorization") token: String,
        @Field("titulo") titulo: String,
        @Field("descripcion") descripcion: String,
        @Field("fecha") fecha: String,
        @Field("estado") estado: String
    ): Response<PraeActividad>

    @FormUrlEncoded
    @PUT("api/admin/prae/actividades/{id}")
    suspend fun updatePraeActividad(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("titulo") titulo: String,
        @Field("descripcion") descripcion: String,
        @Field("fecha") fecha: String,
        @Field("estado") estado: String
    ): Response<PraeActividad>

    @DELETE("api/admin/prae/actividades/{id}")
    suspend fun deletePraeActividad(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    @Multipart
    @POST("api/admin/prae/documentos")
    suspend fun createPraeDocumento(
        @Header("Authorization") token: String,
        @Part("titulo") titulo: RequestBody,
        @Part archivo: MultipartBody.Part
    ): Response<PraeDocumento>

    @DELETE("api/admin/prae/documentos/{id}")
    suspend fun deletePraeDocumento(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    @GET("api/admin/perfil")
    suspend fun getPerfil(@Header("Authorization") token: String): Response<User>

    @FormUrlEncoded
    @PUT("api/admin/perfil")
    suspend fun updatePerfil(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("email") email: String
    ): Response<User>

    @FormUrlEncoded
    @PUT("api/admin/perfil/password")
    suspend fun updatePerfilPassword(
        @Header("Authorization") token: String,
        @Field("current_password") currentPassword: String,
        @Field("password") password: String,
        @Field("password_confirmation") passwordConfirmation: String
    ): Response<Unit>

    @GET("api/admin/contenidos")
    suspend fun getContenidos(@Header("Authorization") token: String): Response<List<Contenido>>

    @Multipart
    @POST("api/admin/contenidos")
    suspend fun createContenido(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
        @Part pdf: MultipartBody.Part?
    ): Response<Contenido>
}
