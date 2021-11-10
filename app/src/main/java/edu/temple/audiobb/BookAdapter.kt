package edu.temple.audiobb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter (_bookList: BookList, _onClick: (Book) -> Unit) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
    val bookList = _bookList
    val onClick = _onClick

    class BookViewHolder (layout : View, onClick : (Book) -> Unit): RecyclerView.ViewHolder (layout) {
        val titleTextView : TextView
        val authorTextView: TextView
        lateinit var book: Book
        init {
            titleTextView = layout.findViewById(R.id.titleTextView)
            authorTextView = layout.findViewById(R.id.authorTextView)
            titleTextView.setOnClickListener {
                onClick(book)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.booklist_items_layout, parent, false), onClick)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.titleTextView.text = bookList[position].title
        holder.authorTextView.text = bookList[position].author
        holder.book = bookList[position]
    }

    override fun getItemCount(): Int {
        return bookList.size()
    }

}