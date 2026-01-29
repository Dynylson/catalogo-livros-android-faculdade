package com.example.catalogodelivros

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.catalogodelivros.api.CreateBookRequest
import com.example.catalogodelivros.api.RetrofitClient
import kotlinx.coroutines.launch
import java.util.Calendar

class CriarLivroActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var radioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_criar_livro)

        sessionManager = SessionManager(this)

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        val etCapaUrl = findViewById<EditText>(R.id.etCapaUrl)
        val etTitulo = findViewById<EditText>(R.id.etTitulo)
        val etAutor = findViewById<EditText>(R.id.etAutor)
        val etGenero = findViewById<EditText>(R.id.etGenero)
        val etReview = findViewById<EditText>(R.id.etReview)
        radioGroup = findViewById(R.id.rgStatus)
        val btnCriarLivro = findViewById<Button>(R.id.btnCriarLivro)

        btnVoltar.setOnClickListener {
            finish()
        }

        btnCriarLivro.setOnClickListener {
            val capaUrl = etCapaUrl.text.toString()
            val titulo = etTitulo.text.toString()
            val autor = etAutor.text.toString()
            val genero = etGenero.text.toString()
            val review = etReview.text.toString()

            if (titulo.isEmpty() || autor.isEmpty()) {
                Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (genero.isEmpty()) {
                Toast.makeText(this, "Preencha o gênero do livro", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedId = radioGroup.checkedRadioButtonId
            val (statusApi, statusDisplay) = when (selectedId) {
                R.id.rbLido -> "READ" to "Lido"
                R.id.rbLendo -> "READING" to "Lendo"
                else -> "WANT_TO_READ" to "Quero ler"
            }

            val token = sessionManager.getToken()
            if (token == null) {
                Toast.makeText(this, "Sessão expirada", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnCriarLivro.isEnabled = false

            lifecycleScope.launch {
                try {
                    val request = CreateBookRequest(
                        title = titulo,
                        author = autor,
                        year = Calendar.getInstance().get(Calendar.YEAR),
                        genre = genero,
                        status = statusApi,
                        review = review.ifEmpty { null },
                        imageUrl = capaUrl.ifEmpty { null }
                    )

                    val response = RetrofitClient.apiService.createBook("Bearer $token", request)

                    if (response.isSuccessful) {
                        val bookResponse = response.body()
                        val intent = Intent()
                        intent.putExtra("BOOK_ID", bookResponse?.id.toString())
                        intent.putExtra("BOOK_CAPA", capaUrl)
                        intent.putExtra("BOOK_TITULO", titulo)
                        intent.putExtra("BOOK_GENERO", genero)
                        intent.putExtra("BOOK_AUTOR", autor)
                        intent.putExtra("BOOK_REVIEW", review)
                        intent.putExtra("BOOK_STATUS", statusDisplay)
                        setResult(RESULT_OK, intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@CriarLivroActivity,
                            "Erro ao criar livro: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        btnCriarLivro.isEnabled = true
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@CriarLivroActivity,
                        "Erro de conexão",
                        Toast.LENGTH_SHORT
                    ).show()
                    btnCriarLivro.isEnabled = true
                }
            }
        }
    }
}
