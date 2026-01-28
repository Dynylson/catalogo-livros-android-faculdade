package com.example.catalogodelivros

data class Book(
    val id: String = "",
    val capaUrl: String = "",
    val titulo: String = "",
    val genero: String = "",
    val autor: String = "",
    val sinopse: String = "",
    val status: String = "Quero ler", // "Lido", "Lendo", "Quero ler"
    var rating: Float = 0f
)
