package com.example.catalogodelivros

data class Book(
    val id: String = "",
    val capaUrl: String = "",
    val titulo: String = "",
    val genero: String = "",
    val autor: String = "",
    val review: String = "",
    val status: String = "Quero ler",
    var rating: Float = 0f
)
