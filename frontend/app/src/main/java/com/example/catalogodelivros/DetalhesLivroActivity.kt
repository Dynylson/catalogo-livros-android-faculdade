package com.example.catalogodelivros

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.catalogodelivros.api.RetrofitClient
import com.example.catalogodelivros.api.UpdateBookRequest
import kotlinx.coroutines.launch

class DetalhesLivroActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private var bookId: Int = -1
    private var currentStatus: String = ""

    private lateinit var ivBookCover: ImageView
    private lateinit var tvTitulo: TextView
    private lateinit var tvAutor: TextView
    private lateinit var tvGenero: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var tvReviewLabel: TextView
    private lateinit var tvReview: TextView
    private lateinit var btnLido: Button
    private lateinit var btnLendo: Button
    private lateinit var btnQueroLer: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_detalhes_livro)

        sessionManager = SessionManager(this)

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        ivBookCover = findViewById(R.id.ivBookCover)
        tvTitulo = findViewById(R.id.tvTitulo)
        tvAutor = findViewById(R.id.tvAutor)
        tvGenero = findViewById(R.id.tvGenero)
        ratingBar = findViewById(R.id.ratingBar)
        tvReviewLabel = findViewById(R.id.tvReviewLabel)
        tvReview = findViewById(R.id.tvReview)
        btnLido = findViewById(R.id.btnLido)
        btnLendo = findViewById(R.id.btnLendo)
        btnQueroLer = findViewById(R.id.btnQueroLer)

        val bookIdStr = intent.getStringExtra("BOOK_ID") ?: ""
        bookId = bookIdStr.toIntOrNull() ?: -1

        btnVoltar.setOnClickListener {
            finish()
        }

        if (bookId != -1) {
            loadBookData()
        }

        setupStatusButtons()

        ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser && bookId != -1) {
                updateBookRating(rating.toInt())
            }
        }
    }

    private fun loadBookData() {
        val token = sessionManager.getToken() ?: return

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getBook("Bearer $token", bookId)
                if (response.isSuccessful) {
                    response.body()?.let { book ->
                        tvTitulo.text = book.title
                        tvAutor.text = book.author
                        tvGenero.text = book.genre
                        ratingBar.rating = book.rating?.toFloat() ?: 0f

                        if (!book.review.isNullOrEmpty()) {
                            tvReviewLabel.visibility = View.VISIBLE
                            tvReview.visibility = View.VISIBLE
                            tvReview.text = book.review
                        } else {
                            tvReviewLabel.visibility = View.GONE
                            tvReview.visibility = View.GONE
                        }

                        if (!book.imageUrl.isNullOrEmpty()) {
                            ivBookCover.load(book.imageUrl) {
                                crossfade(true)
                            }
                        }

                        currentStatus = book.status
                        updateStatusButtonsUI(book.status)
                    }
                } else {
                    Toast.makeText(this@DetalhesLivroActivity, "Erro ao carregar livro", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetalhesLivroActivity, "Erro de conexão", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupStatusButtons() {
        btnLido.setOnClickListener {
            updateBookStatus("READ")
        }

        btnLendo.setOnClickListener {
            updateBookStatus("READING")
        }

        btnQueroLer.setOnClickListener {
            updateBookStatus("WANT_TO_READ")
        }
    }

    private fun updateStatusButtonsUI(status: String) {
        btnLido.backgroundTintList = getColorStateList(
            if (status == "READ") R.color.verde else R.color.cinza
        )
        btnLendo.backgroundTintList = getColorStateList(
            if (status == "READING") R.color.verde else R.color.cinza
        )
        btnQueroLer.backgroundTintList = getColorStateList(
            if (status == "WANT_TO_READ") R.color.verde else R.color.cinza
        )
    }

    private fun updateBookStatus(newStatus: String) {
        if (newStatus == currentStatus) return

        val token = sessionManager.getToken() ?: return

        lifecycleScope.launch {
            try {
                val request = UpdateBookRequest(status = newStatus)
                val response = RetrofitClient.apiService.updateBook("Bearer $token", bookId, request)

                if (response.isSuccessful) {
                    currentStatus = newStatus
                    updateStatusButtonsUI(newStatus)
                    setResult(RESULT_OK)
                } else {
                    Toast.makeText(this@DetalhesLivroActivity, "Erro ao atualizar status", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetalhesLivroActivity, "Erro de conexão", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateBookRating(rating: Int) {
        val token = sessionManager.getToken() ?: return

        lifecycleScope.launch {
            try {
                val request = UpdateBookRequest(rating = rating)
                val response = RetrofitClient.apiService.updateBook("Bearer $token", bookId, request)

                if (response.isSuccessful) {
                    setResult(RESULT_OK)
                } else {
                    Toast.makeText(this@DetalhesLivroActivity, "Erro ao atualizar avaliação", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetalhesLivroActivity, "Erro de conexão", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
