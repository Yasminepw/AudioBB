package edu.temple.audiobb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(private var bookList: BookList, private val myOnClick : (book : Book) -> Unit) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View, val myOnClick : (book : Book) -> Unit) : RecyclerView.ViewHolder(itemView){
        private val bookTitleTextView: TextView = itemView.findViewById(R.id.bookTitleRecyclerTextView)
        private val bookDetailsTextView: TextView = itemView.findViewById(R.id.bookDetailsRecyclerTextView)
        private val bookIDTextView: TextView = itemView.findViewById(R.id.bookIDTextView)
        private var currentBook: Book? = null

        init {
            itemView.setOnClickListener {
                currentBook?.let {
                    myOnClick(it)
                }
            }
        }

        fun bindBook(book: Book) {
            currentBook = book
            bookTitleTextView.text = book.title
            bookDetailsTextView.text = book.author
            if (book.id == 0) {
                bookIDTextView.text = ""
            } else {
                bookIDTextView.text = book.id.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.booklist_items_layout, parent, false)
        return BookViewHolder(view, myOnClick)
    }

    override fun onBindViewHolder(holderBook: BookViewHolder, position: Int) {
        holderBook.bindBook(bookList.get(position))
    }

    override fun getItemCount() = bookList.size()

    fun updateList(bookList: BookList) {
        this.bookList = bookList
        notifyDataSetChanged()
    }

}