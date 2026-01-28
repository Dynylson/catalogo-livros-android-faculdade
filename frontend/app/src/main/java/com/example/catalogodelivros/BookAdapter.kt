package com.example.catalogodelivros


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import coil.load
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(
    private val books: List<Book>,
    private val onBookClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
    inner class BookViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val ivCover: ImageView = itemView.findViewById(R.id.ivBookCover)
        val tvTitle: TextView = itemView.findViewById(R.id.tvBookTitle)
        val tvAuthor: TextView = itemView.findViewById(R.id.tvBookAuthor)

        fun bind(book: Book){
            tvTitle.text = book.titulo
            tvAuthor.text = book.autor

            ivCover.load(book.capaUrl){
                crossfade(true)
            }

            itemView.setOnClickListener {
                onBookClick(book)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book,parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount() = books.size
}