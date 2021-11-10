package edu.temple.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

class BookDetailsFragment : Fragment() {

    lateinit var titleTextView: TextView
    lateinit var authorTextView: TextView
    lateinit var coverImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_book_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleTextView = view.findViewById(R.id.bookTitleTextView)
        authorTextView =  view.findViewById(R.id.bookDetailsTextView)
        coverImageView = view.findViewById(R.id.coverImageView)

        ViewModelProvider(requireActivity())
            .get(BookVM::class.java)
            .getBook()
            .observe(requireActivity()) {
                updateBook(it)
            }
    }

    private fun updateBook(book: Book?) {
        book?.run {
            titleTextView.text = title
            authorTextView.text = author
        }
    }
}