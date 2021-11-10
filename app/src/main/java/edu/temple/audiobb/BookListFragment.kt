package edu.temple.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BookListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var bookList = BookList()
    private lateinit var bookViewModel: BookVM
    private lateinit var adapter : BookAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bookList = if (savedInstanceState == null) {
                it.getSerializable("edu.temple.audiobb.BookListFragment.BOOK_LIST") as BookList
            } else {
                savedInstanceState.getSerializable("edu.temple.audiobb.BookListFragment.BOOK_LIST_STATE") as BookList
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val layout = inflater.inflate(R.layout.fragment_book_list, container, false)

        ViewModelProvider(requireActivity())
            .get(BookListViewModel::class.java)
            .getBookList()
            .observe(requireActivity()) {
                bookList = it
                adapter.updateList(it)
            }

        bookViewModel = ViewModelProvider(requireActivity()).get(BookVM::class.java)
        recyclerView = layout.findViewById(R.id.bookListFragmentRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = BookAdapter(bookList) {
                book ->  myOnClick(book)
        }
        recyclerView.adapter = adapter

        return layout
    }

    private fun myOnClick(book: Book) {
        (activity as BookSelectedInterface).selectionMade()
        bookViewModel.setSelectedBook(book)
    }

    companion object {

        fun newInstance(bookList: BookList) =
            BookListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("edu.temple.audiobb.BookListFragment.BOOK_LIST", bookList)
                }
            }

    }

    interface BookSelectedInterface {

        fun selectionMade()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("edu.temple.audiobb.BookListFragment.BOOK_LIST_STATE", bookList)
    }
}
