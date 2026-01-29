package com.example.catalogodelivros

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.catalogodelivros.api.RetrofitClient
import com.example.catalogodelivros.api.UpdateBookRequest
import kotlinx.coroutines.launch

class EditarLivroActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private var bookId: Int = -1

    private lateinit var etCapaUrl: EditText
    private lateinit var etTitulo: EditText
    private lateinit var etAutor: EditText
    private lateinit var etGenero: EditText
    private lateinit var etReview: EditText
    private lateinit var btnSalvar: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_editar_livro)

        sessionManager = SessionManager(this)

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        etCapaUrl = findViewById(R.id.etCapaUrl)
        etTitulo = findViewById(R.id.etTitulo)
        etAutor = findViewById(R.id.etAutor)
        etGenero = findViewById(R.id.etGenero)
        etReview = findViewById(R.id.etReview)
        btnSalvar = findViewById(R.id.btnSalvar)
        progressBar = findViewById(R.id.progressBar)

        val bookIdStr = intent.getStringExtra("BOOK_ID") ?: ""
        bookId = bookIdStr.toIntOrNull() ?: -1

        if (bookId != -1) {
            loadBookData()
        }

        btnVoltar.setOnClickListener {
            finish()
        }

        btnSalvar.setOnClickListener {
            saveChanges()
        }
    }

    private fun loadBookData() {
        val token = sessionManager.getToken() ?: return

        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getBook("Bearer $token", bookId)
                if (response.isSuccessful) {
                    response.body()?.let { book ->
                        etCapaUrl.setText(book.imageUrl ?: "")
                        etTitulo.setText(book.title)
                        etAutor.setText(book.author)
                        etGenero.setText(book.genre)
                        etReview.setText(book.review ?: "")
                    }
                } else {
                    Toast.makeText(this@EditarLivroActivity, "Erro ao carregar livro", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EditarLivroActivity, "Erro de conexão", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun saveChanges() {
        val capaUrl = etCapaUrl.text.toString()
        val titulo = etTitulo.text.toString()
        val autor = etAutor.text.toString()
        val genero = etGenero.text.toString()
        val review = etReview.text.toString()

        if (titulo.isEmpty() || autor.isEmpty()) {
            Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_SHORT).show()
            return
        }

        val token = sessionManager.getToken() ?: return

        btnSalvar.isEnabled = false
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val request = UpdateBookRequest(
                    title = titulo,
                    author = autor,
                    genre = genero,
                    review = review.ifEmpty { null },
                    imageUrl = capaUrl.ifEmpty { null }
                )

                val response = RetrofitClient.apiService.updateBook("Bearer $token", bookId, request)
                if (response.isSuccessful) {
                    Toast.makeText(this@EditarLivroActivity, "Livro atualizado", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@EditarLivroActivity, "Erro ao atualizar livro", Toast.LENGTH_SHORT).show()
                    btnSalvar.isEnabled = true
                }
            } catch (e: Exception) {
                Toast.makeText(this@EditarLivroActivity, "Erro de conexão", Toast.LENGTH_SHORT).show()
                btnSalvar.isEnabled = true
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}
