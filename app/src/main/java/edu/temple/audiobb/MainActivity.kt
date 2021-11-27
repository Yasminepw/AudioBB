package edu.temple.audiobb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity(), BookListFragment.BookSelectedInterface {

    private var isSingleContainer : Boolean = false
    private lateinit var selectedBookViewModel: BookVM
    private var bookList = BookList()

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == RESULT_OK) {
            bookList = it.data?.getSerializableExtra("edu.temple.audiobb.BookSearchActivity.SEARCH_RESULTS") as BookList
            ViewModelProvider(this).get(BookListViewModel::class.java).setBookList(bookList)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isSingleContainer = findViewById<View>(R.id.container2) != null
        selectedBookViewModel = ViewModelProvider(this).get(BookVM::class.java)

        val launchSearchButton = findViewById<Button>(R.id.launchMainSearchButton)
        launchSearchButton.setOnClickListener {
            val intent = Intent(this, BookSearchActivity::class.java)
            launcher.launch(intent)
        }

        bookList.add(Book(0, "Click to search", "", "", 0))

        val bookListFragment = BookListFragment.newInstance(bookList)

        if (supportFragmentManager.findFragmentById(R.id.container1) is BookDetailsFragment
            && isSingleContainer) {
            supportFragmentManager.popBackStack()
        }

        if (supportFragmentManager.findFragmentById(R.id.container2) is BookDetailsFragment
            && !isSingleContainer) {
            supportFragmentManager.popBackStack()
        }

        if (supportFragmentManager.findFragmentById(R.id.container2) is BookDetailsFragment
            && !isSingleContainer) {
            if (selectedBookViewModel.getBook().value?.id != -1
                && !selectedBookViewModel.isEmpty()) {
                selectionMade()
            }
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container1, bookListFragment)
                .commit()
        }

        if(isSingleContainer && supportFragmentManager.findFragmentById(R.id.container2) == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.container2, BookDetailsFragment())
                .addToBackStack(null)
                .commit()
        } else if(isSingleContainer) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container2, BookDetailsFragment())
                .commit()
        }

    }

    override fun selectionMade() {
        if (!isSingleContainer) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container1, BookDetailsFragment())
                .addToBackStack(null)
                .commit()
        }
        else {
            if (supportFragmentManager.findFragmentById(R.id.container2) == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container2, BookDetailsFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

    }

        override fun onBackPressed() {
            super.onBackPressed()
            selectedBookViewModel.setSelectedBook(null)
        }


}