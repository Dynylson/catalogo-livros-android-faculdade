package com.example.catalogodelivros.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("api/books")
    suspend fun getBooks(@Header("Authorization") token: String): Response<List<BookResponse>>

    @GET("api/books/{id}")
    suspend fun getBook(
        @Header("Authorization") token: String,
        @Path("id") bookId: Int
    ): Response<BookResponse>

    @POST("api/books")
    suspend fun createBook(
        @Header("Authorization") token: String,
        @Body request: CreateBookRequest
    ): Response<BookResponse>

    @PUT("api/books/{id}")
    suspend fun updateBook(
        @Header("Authorization") token: String,
        @Path("id") bookId: Int,
        @Body request: UpdateBookRequest
    ): Response<BookResponse>

    @DELETE("api/books/{id}")
    suspend fun deleteBook(
        @Header("Authorization") token: String,
        @Path("id") bookId: Int
    ): Response<Unit>
}

data class CreateBookRequest(
    val title: String,
    val author: String,
    val year: Int,
    val genre: String,
    val status: String,
    val rating: Int? = null,
    val review: String? = null,
    val imageUrl: String? = null
)

data class UpdateBookRequest(
    val title: String? = null,
    val author: String? = null,
    val year: Int? = null,
    val genre: String? = null,
    val status: String? = null,
    val rating: Int? = null,
    val review: String? = null,
    val imageUrl: String? = null
)

data class BookResponse(
    val id: Int,
    val title: String,
    val author: String,
    val year: Int,
    val genre: String,
    val status: String,
    val rating: Int?,
    val review: String?,
    val imageUrl: String?,
    val userId: Int,
    val createdAt: String,
    val updatedAt: String
)
