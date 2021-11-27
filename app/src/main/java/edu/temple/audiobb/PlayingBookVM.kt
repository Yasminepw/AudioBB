package edu.temple.audiobb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.temple.audlibplayer.PlayerService

class PlayingBookVM : ViewModel() {
    private val book: MutableLiveData<PlayerService.BookProgress> by lazy {
        MutableLiveData()
    }

    fun getPlayingBook() : LiveData<PlayerService.BookProgress> {
        return book
    }

    fun setPlayingBook(selectedBook: PlayerService.BookProgress?) {
        this.book.value = selectedBook
    }
}