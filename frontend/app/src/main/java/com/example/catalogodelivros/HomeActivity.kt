package com.example.livrosfoda

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.livrosfoda.adapter.BookAdapter
import com.example.livrosfoda.model.Book
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.jvm.java

class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookAdapter
    private lateinit var tvEmptyState: TextView
    private val bookList = mutableListOf<Book>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.rvBooks)
        tvEmptyState = findViewById(R.id.tvEmptyState)
        val fabAddBook = findViewById<FloatingActionButton>(R.id.fabAddBook)

        setupRecyclerView()
        updateEmptyState()

        fabAddBook.setOnClickListener {
            val intent = Intent(this, CriarLivroActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD_BOOK)
        }
    }

    private fun setupRecyclerView() {
        bookAdapter = BookAdapter(bookList) { book ->
            val intent = Intent(this, DetalhesLivroActivity::class.java)
            intent.putExtra("BOOK_TITULO", book.titulo)
            intent.putExtra("BOOK_AUTOR", book.autor)
            intent.putExtra("BOOK_CAPA", book.capaUrl)
            intent.putExtra("BOOK_SINOPSE", book.sinopse)
            intent.putExtra("BOOK_RATING", book.rating)
            startActivity(intent)
        }

        recyclerView.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
            adapter = bookAdapter
        }
    }

    private fun updateEmptyState() {
        if (bookList.isEmpty()) {
            tvEmptyState.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            tvEmptyState.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_BOOK && resultCode == RESULT_OK) {
            data?.let {
                val book = Book(
                    capaUrl = it.getStringExtra("BOOK_CAPA") ?: "",
                    titulo = it.getStringExtra("BOOK_TITULO") ?: "",
                    genero = it.getStringExtra("BOOK_GENERO") ?: "",
                    autor = it.getStringExtra("BOOK_AUTOR") ?: "",
                    sinopse = it.getStringExtra("BOOK_SINOPSE") ?: "",
                    status = it.getStringExtra("BOOK_STATUS") ?: "Quero ler"
                )
                bookList.add(book)
                bookAdapter.notifyDataSetChanged()
                updateEmptyState()
            }
        }
    }

    companion object {
        const val REQUEST_ADD_BOOK = 1
    }
}