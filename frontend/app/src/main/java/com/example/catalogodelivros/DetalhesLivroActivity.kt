package com.example.catalogodelivros

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load


class DetalhesLivroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_livro)

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        val ivBookCover = findViewById<ImageView>(R.id.ivBookCover)
        val tvTitulo = findViewById<TextView>(R.id.tvTitulo)
        val tvAutor = findViewById<TextView>(R.id.tvAutor)
        val tvGenero = findViewById<TextView>(R.id.tvGenero)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val tvSinopse = findViewById<TextView>(R.id.tvSinopse)

        val titulo = intent.getStringExtra("BOOK_TITULO") ?: ""
        val autor = intent.getStringExtra("BOOK_AUTOR") ?: ""
        val capaUrl = intent.getStringExtra("BOOK_CAPA") ?: ""
        val sinopse = intent.getStringExtra("BOOK_SINOPSE") ?: ""
        val rating = intent.getFloatExtra("BOOK_RATING", 0f)

        tvTitulo.text = titulo
        tvAutor.text = autor
        tvGenero.text = "Fantasia"
        tvSinopse.text = sinopse
        ratingBar.rating = rating

        if (capaUrl.isNotEmpty()) {
            ivBookCover.load(capaUrl) {
                crossfade(true)
            }
        }

        btnVoltar.setOnClickListener {
            finish()
        }

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            // Aqui você pode salvar a avaliação do usuário
        }
    }
}