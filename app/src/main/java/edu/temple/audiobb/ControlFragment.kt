package edu.temple.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView


class ControlFragment : Fragment() {
    lateinit var playButton: Button
    lateinit var pauseButton: Button
    lateinit var stopButton: Button
    var seekBar: SeekBar? = null
    var nowPlaying: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_control, container, false)
        playButton = layout.findViewById(R.id.playButton)
        pauseButton = layout.findViewById(R.id.pauseButton)
        stopButton = layout.findViewById(R.id.stopButton)
        seekBar = layout.findViewById(R.id.seekBar)
        nowPlaying = layout.findViewById(R.id.nowPlaying)

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    (activity as ControlInterface).seek(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        val onClickListener = View.OnClickListener {
            val parent = activity as ControlInterface
            when (it.id) {
                R.id.playButton -> parent.play()
                R.id.pauseButton -> parent.pause()
                R.id.stopButton -> parent.stop()

            }
        }

        playButton.setOnClickListener(onClickListener)
        pauseButton.setOnClickListener(onClickListener)
        stopButton.setOnClickListener(onClickListener)
        return layout
    }

    fun setNowPlaying(title: String) {
        nowPlaying?.text = title
    }


    fun setPlayrogress(process: Int) {
        seekBar?.setProgress(process, true)
    }

    interface ControlInterface {
        fun play()
        fun pause()
        fun stop()
        fun seek(position: Int)

    }
}