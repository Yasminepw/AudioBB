package edu.temple.audiobb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookVM : ViewModel() {

    private val book: MutableLiveData<Book> by lazy {
        MutableLiveData()
    }

    fun getSelectedBook(): LiveData<Book> {
        return book
    }

    fun setSelectedBook(selectedBook: Book?) {
        this.book.value = selectedBook
    }

    fun getBook(): LiveData<Book> {
        return book

    }
}
