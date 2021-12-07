package edu.temple.audiobb

import android.content.Context
import java.io.*
import java.net.URL
import java.net.URLConnection

private const val DOWNLOAD_URL = "https://kamorris.com/lab/audlib/download.php?id="

class BookDownloader(val context: Context) {

    fun bookDownload(id:Int) {
        Thread{
            val mainURL = URL(DOWNLOAD_URL + id)
            val urlConnection: URLConnection = mainURL.openConnection()
            urlConnection.connect()
            val filename = "book_$id.mp3"
            val cache = File(context.filesDir, filename)

            if(!cache.exists()) {
                mainURL.openStream().use { input ->
                    FileOutputStream(cache).use { output ->
                        input.copyTo(output)
                    }
                }

            }
        }.start()
    }

    fun savedBookList(list: BookList) {
        val file: File
        file = File(context.cacheDir, "BookList")
        file.outputStream().use { fileOutputStream ->
            ObjectOutputStream(fileOutputStream).use {
                it.writeObject(list)
            }
        }
    }

    fun getBookList() : BookList? {
        val file: File
        file = File(context.cacheDir, "BookList")
        if(!file.exists()) {
            return null
        }
        file.inputStream().use { fileInputStream ->
            ObjectInputStream(fileInputStream).use {
                return it.readObject() as BookList
            }
        }
    }

}