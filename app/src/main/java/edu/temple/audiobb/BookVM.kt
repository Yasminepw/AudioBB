package edu.temple.audiobb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookVM : ViewModel() {
    private var empty: Boolean = true

    private val book: MutableLiveData<Book> by lazy {
        MutableLiveData()
    }

    fun setSelectedBook(selectedBook: Book?) {
        this.book.value = selectedBook
    }

    fun getBook(): LiveData<Book> {
        return book

    }

    fun isEmpty(): Boolean {
        return empty
    }
}
