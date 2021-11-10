package edu.temple.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val BOOK_LIST = "booklist"

class BookListFragment : Fragment() {
    private var bookList: BookList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bookList = it.getSerializable(BOOK_LIST) as BookList?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_book_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookViewModel = ViewModelProvider(requireActivity()).get(BookVM::class.java)

        val onClick : (Book) -> Unit = {
                book: Book -> bookViewModel.setSelectedBook(book)
                (activity as BookSelectedInterface).bookSelected()
        }
        with (view as RecyclerView) {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = BookAdapter (bookList!!, onClick)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(bookList: BookList) =
            BookListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(BOOK_LIST, bookList)
                }
            }
    }

    interface BookSelectedInterface {
        fun bookSelected()
    }
}