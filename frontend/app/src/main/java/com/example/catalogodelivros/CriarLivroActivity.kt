package com.example.catalogodelivros

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class CriarLivroActivity : AppCompatActivity() {
    private lateinit var radioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_livro)

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        val etCapaUrl = findViewById<EditText>(R.id.etCapaUrl)
        val etTitulo = findViewById<EditText>(R.id.etTitulo)
        val etGenero = findViewById<EditText>(R.id.etGenero)
        val etAutor = findViewById<EditText>(R.id.etAutor)
        val etSinopse = findViewById<EditText>(R.id.etSinopse)
        radioGroup = findViewById(R.id.rgStatus)
        val btnCriarLivro = findViewById<Button>(R.id.btnCriarLivro)

        btnVoltar.setOnClickListener {
            finish()
        }

        btnCriarLivro.setOnClickListener {
            val capaUrl = etCapaUrl.text.toString()
            val titulo = etTitulo.text.toString()
            val genero = etGenero.text.toString()
            val autor = etAutor.text.toString()
            val sinopse = etSinopse.text.toString()

            if (titulo.isEmpty() || autor.isEmpty()) {
                Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedId = radioGroup.checkedRadioButtonId
            val status = when (selectedId) {
                R.id.rbLido -> "Lido"
                R.id.rbLendo -> "Lendo"
                else -> "Quero ler"
            }

            val intent = Intent()
            intent.putExtra("BOOK_CAPA", capaUrl)
            intent.putExtra("BOOK_TITULO", titulo)
            intent.putExtra("BOOK_GENERO", genero)
            intent.putExtra("BOOK_AUTOR", autor)
            intent.putExtra("BOOK_SINOPSE", sinopse)
            intent.putExtra("BOOK_STATUS", status)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}