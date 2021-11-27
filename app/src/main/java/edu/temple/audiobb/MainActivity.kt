package edu.temple.audiobb

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import edu.temple.audlibplayer.PlayerService

class MainActivity : AppCompatActivity(), BookListFragment.BookSelectedInterface, ControlFragment.ControlInterface {

    private var isSingleContainer : Boolean = false
    private lateinit var selectedBookViewModel: BookVM
    private var bookList = BookList()
    private lateinit var bookProgress: PlayerService.BookProgress

    var isConnected = false
    lateinit var mediaBinder: PlayerService.MediaControlBinder

    val mediaHandler = Handler(Looper.getMainLooper()) {
        bookProgress = it.obj as PlayerService.BookProgress
        ViewModelProvider(this).get(PlayingBookVM::class.java).setPlayingBook(bookProgress)
        true
    }

    private val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            isConnected = true
            mediaBinder = service as PlayerService.MediaControlBinder
            mediaBinder.setProgressHandler(mediaHandler)

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isConnected = false
        }
    }


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == RESULT_OK) {
            bookList = it.data?.getSerializableExtra("edu.temple.audiobb.BookSearchActivity.SEARCH_RESULTS") as BookList
            ViewModelProvider(this).get(BookListViewModel::class.java).setBookList(bookList)
            Log.d("TAG", ": ${bookList[0].title} ")


        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindService(Intent(this, PlayerService::class.java)
            , serviceConnection
            , BIND_AUTO_CREATE)

        isSingleContainer = findViewById<View>(R.id.container2) != null
        selectedBookViewModel = ViewModelProvider(this).get(BookVM::class.java)

        val launchSearchButton = findViewById<Button>(R.id.launchMainSearchButton)
        launchSearchButton.setOnClickListener {
            val intent = Intent(this, BookSearchActivity::class.java)
            launcher.launch(intent)
        }

        bookList.add(Book(0, "", "", "", 0))

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

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
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

    override fun play(id: Int) {
        mediaBinder.play(id)

        startService(Intent(this, PlayerService::class.java))

    }

    override fun pause() {
       mediaBinder.pause()
    }

    override fun stop() {
        mediaBinder.stop()
        stopService(Intent(this, PlayerService::class.java))
    }

    override fun seek(position: Int) {
        mediaBinder.seekTo(position)
    }


}