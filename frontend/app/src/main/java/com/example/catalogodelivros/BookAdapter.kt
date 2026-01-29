package com.example.catalogodelivros

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class BookAdapter(
    private val books: MutableList<Book>,
    private val onBookClick: (Book) -> Unit,
    private val onEditClick: (Book) -> Unit,
    private val onDeleteClick: (Book, Int) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCover: ImageView = itemView.findViewById(R.id.ivBookCover)
        val tvTitle: TextView = itemView.findViewById(R.id.tvBookTitle)
        val tvAuthor: TextView = itemView.findViewById(R.id.tvBookAuthor)
        val btnMoreOptions: ImageButton = itemView.findViewById(R.id.btnMoreOptions)

        fun bind(book: Book, position: Int) {
            tvTitle.text = book.titulo
            tvAuthor.text = book.autor

            if (book.capaUrl.isNotEmpty()) {
                ivCover.load(book.capaUrl) {
                    crossfade(true)
                }
            }

            itemView.setOnClickListener {
                onBookClick(book)
            }

            btnMoreOptions.setOnClickListener { view ->
                showPopupMenu(view, book, position)
            }
        }

        private fun showPopupMenu(view: View, book: Book, position: Int) {
            val popup = PopupMenu(view.context, view)
            popup.menuInflater.inflate(R.menu.menu_book_options, popup.menu)
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_edit -> {
                        onEditClick(book)
                        true
                    }
                    R.id.action_delete -> {
                        onDeleteClick(book, position)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position], position)
    }

    override fun getItemCount() = books.size

    fun removeAt(position: Int) {
        books.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, books.size)
    }
}
