package com.example.catalogodelivros.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/books")
    suspend fun createBook(
        @Header("Authorization") token: String,
        @Body request: CreateBookRequest
    ): Response<BookResponse>
}

data class CreateBookRequest(
    val title: String,
    val author: String,
    val year: Int,
    val genre: String,
    val status: String,
    val rating: Int? = null,
    val review: String? = null
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
    val userId: Int,
    val createdAt: String,
    val updatedAt: String
)
