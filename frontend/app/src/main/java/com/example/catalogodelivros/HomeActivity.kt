package com.example.catalogodelivros

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogodelivros.api.RetrofitClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookAdapter
    private lateinit var tvEmptyState: TextView
    private lateinit var sessionManager: SessionManager

    private val bookList = mutableListOf<Book>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_home)

        sessionManager = SessionManager(this)

        recyclerView = findViewById(R.id.rvBooks)
        tvEmptyState = findViewById(R.id.tvEmptyState)
        val fabAddBook = findViewById<FloatingActionButton>(R.id.fabAddBook)
        val btnLogout = findViewById<android.widget.ImageButton>(R.id.btnLogout)

        setupRecyclerView()
        loadBooksFromApi()

        fabAddBook.setOnClickListener {
            val intent = Intent(this, CriarLivroActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD_BOOK)
        }

        btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    private fun setupRecyclerView() {
        bookAdapter = BookAdapter(
            bookList,
            onBookClick = { book ->
                val intent = Intent(this, DetalhesLivroActivity::class.java)
                intent.putExtra("BOOK_ID", book.id)
                startActivityForResult(intent, REQUEST_VIEW_BOOK)
            },
            onEditClick = { book ->
                val intent = Intent(this, EditarLivroActivity::class.java)
                intent.putExtra("BOOK_ID", book.id)
                startActivityForResult(intent, REQUEST_EDIT_BOOK)
            },
            onDeleteClick = { book, position ->
                showDeleteConfirmation(book, position)
            }
        )
        recyclerView.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
            adapter = bookAdapter
        }
    }

    private fun loadBooksFromApi() {
        val token = sessionManager.getToken() ?: return

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getBooks("Bearer $token")
                if (response.isSuccessful) {
                    val books = response.body() ?: emptyList()
                    bookList.clear()
                    bookList.addAll(books.map { bookResponse ->
                        Book(
                            id = bookResponse.id.toString(),
                            titulo = bookResponse.title,
                            autor = bookResponse.author,
                            genero = bookResponse.genre,
                            review = bookResponse.review ?: "",
                            status = mapStatusToDisplay(bookResponse.status),
                            rating = bookResponse.rating?.toFloat() ?: 0f,
                            capaUrl = bookResponse.imageUrl ?: ""
                        )
                    })
                    bookAdapter.notifyDataSetChanged()
                    updateEmptyState()
                }
            } catch (e: Exception) {
                Toast.makeText(this@HomeActivity, "Erro ao carregar livros", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mapStatusToDisplay(status: String): String {
        return when (status) {
            "READ" -> "Lido"
            "READING" -> "Lendo"
            "WANT_TO_READ" -> "Quero ler"
            else -> status
        }
    }

    private fun showDeleteConfirmation(book: Book, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Excluir livro")
            .setMessage("Tem certeza que deseja excluir \"${book.titulo}\"?")
            .setPositiveButton("Excluir") { _, _ ->
                deleteBook(book, position)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteBook(book: Book, position: Int) {
        val token = sessionManager.getToken() ?: return
        val bookId = book.id.toIntOrNull() ?: return

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.deleteBook("Bearer $token", bookId)
                if (response.isSuccessful) {
                    bookAdapter.removeAt(position)
                    updateEmptyState()
                    Toast.makeText(this@HomeActivity, "Livro excluído", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@HomeActivity, "Erro ao excluir livro", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@HomeActivity, "Erro de conexão", Toast.LENGTH_SHORT).show()
            }
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

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Fazer logout")
            .setMessage("Tem certeza que deseja fazer logout?")
            .setPositiveButton("Logout") { _, _ ->
                performLogout()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun performLogout() {
        sessionManager.logout()
        Toast.makeText(this, "Logout realizado", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_ADD_BOOK, REQUEST_VIEW_BOOK, REQUEST_EDIT_BOOK -> {
                    loadBooksFromApi()
                }
            }
        }
    }

    companion object {
        const val REQUEST_ADD_BOOK = 1
        const val REQUEST_VIEW_BOOK = 2
        const val REQUEST_EDIT_BOOK = 3
    }
}
