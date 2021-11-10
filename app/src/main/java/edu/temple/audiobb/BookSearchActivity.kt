package edu.temple.audiobb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class BookSearchActivity : AppCompatActivity() {

    private val queue by lazy {
        Volley.newRequestQueue(this)
    }

    private val bookList by lazy {
        BookList()
    }

    lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_search)
        searchEditText = findViewById(R.id.searchEditText)
        val searchButton = findViewById<Button>(R.id.searchButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)


        searchButton.setOnClickListener {
            startSearch()
            queue.addRequestFinishedListener<JsonArrayRequest> {
                if (bookList.size() == 0) {
                    bookList.add(Book(0, getString(R.string.no_matches), "", ""))
                    setResult(RESULT_OK, Intent().putExtra("edu.temple.audiobb.BookSearchActivity.SEARCH_RESULTS", bookList))
                } else
                    setResult(RESULT_OK, Intent().putExtra("edu.temple.audiobb.BookSearchActivity.SEARCH_RESULTS", bookList))
                finish()
            }

        }

        cancelButton.setOnClickListener {
            finish()
        }

    }

    private fun startSearch(){
        val url = "https://kamorris.com/lab/cis3515/search.php?term=" + searchEditText.text

        val arrayRequest = JsonArrayRequest(Request.Method.GET, url,
            null,
            {
                try {
                    for (i in 0 until it.length()) {
                        val bookData = it.getJSONObject(i)
                        val id = bookData.getString("id").toInt()
                        val title = bookData.getString("title")
                        val author = bookData.getString("author")
                        val coverURL = bookData.getString("cover_url")
                        val book = Book(id, title, author, coverURL)
                        bookList.add(book)
                    }
                } catch (e: JSONException) {
                    Toast.makeText(this, "There has been an error", Toast.LENGTH_SHORT).show()
                }                                       },
            {
                Toast.makeText(this, "There has been an error", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(arrayRequest)
    }

    override fun onStop() {
        super.onStop()
        queue.cancelAll(this)
    }


}
