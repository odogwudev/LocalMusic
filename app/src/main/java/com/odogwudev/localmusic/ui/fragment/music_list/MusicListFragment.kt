package com.odogwudev.localmusic.ui.fragment.music_list

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startForegroundService
import androidx.lifecycle.ViewModelProvider
import com.odogwudev.localmusic.R
import com.odogwudev.localmusic.abstraction.AbstractFragment
import com.odogwudev.localmusic.app.MyApplication
import com.odogwudev.localmusic.broadcast.SongBroadCast
import com.odogwudev.localmusic.callback.OnListenerBroadCast
import com.odogwudev.localmusic.room.table.Song
import com.odogwudev.localmusic.services.PlayMusicServices
import com.odogwudev.localmusic.ui.activity.music_list.MusicListViewModel
import com.odogwudev.localmusic.ui.activity.playing_song.PlayingSongActivity
import com.odogwudev.localmusic.ui.fragment.music_list.MusicListFragment.Companion.mHandler
import com.odogwudev.localmusic.utils.Constants
import com.odogwudev.localmusic.utils.Helpers
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_playing_song.mImgPause
import kotlinx.android.synthetic.main.layout_fragment_music_list.*

class MusicListFragment : AbstractFragment(), OnListenerBroadCast, View.OnClickListener {
    private var mCheckServices = false
    private var mView: View? = null
    private var mMusicListViewModel: MusicListViewModel? = null
    private var mSongBroadCast = SongBroadCast()
    private var mPosition = 0

    companion object {
        var mRunnable: Runnable? = null
        var mHandler: Handler = Handler()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSongBroadCast.setUpdate(this)
        context!!.registerReceiver(
            mSongBroadCast,
            IntentFilter(Constants.sendActionBroadCastActivity)
        )
    }

    override fun initViewModel() {
        mMusicListViewModel = ViewModelProvider(
            activity!!,
            MyApplication.Holder.factory!!
        )[MusicListViewModel::class.java]
    }

    override fun init() {
        onCompletionListener()
    }

    private fun onCompletionListener() {
        PlayingSongActivity.mRunnable?.let {
            PlayingSongActivity.mHandler?.removeCallbacks(
                PlayingSongActivity.mRunnable!!
            )
        }
        mRunnable = Runnable {
            PlayMusicServices.mMediaPlayer?.let { it.setOnCompletionListener { onMusicNext() } }
        }
        mHandler.postDelayed(mRunnable!!, 500)
    }

    private fun onMusicNext() {
        Helpers.sendBoardCastServices(Constants.keyActionNext, context!!)
        onCompletionListener()
    }

    override fun onListenerClicked() {
        mView!!.findViewById<ImageView>(R.id.mImgPrevious).setOnClickListener(this)
        mView!!.findViewById<ImageView>(R.id.mImgPause).setOnClickListener(this)
        mView!!.findViewById<ImageView>(R.id.mImgNext).setOnClickListener(this)
    }

    override fun getContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.layout_fragment_music_list, container, false)
        return mView
    }

    override fun onListenerActionClose(position: Int, songList: List<Song>, action: String) {
        mPosition = position
        mImgPause.setImageResource(R.drawable.ic_pause_white)
        if (action == Constants.keyActionClose) {
            mCheckServices = true
            mImgPause.setImageResource(R.drawable.ic_play_white)
        }
        Picasso.get().load(songList[position].mPathUriImage).error(R.drawable.img_music_error)
            .into(mImgAvatar)
        mTxtTitle.text = songList[position].mTitle
        mTxtSinger.text = songList[position].mArtist
    }

    override fun onDestroy() {
        super.onDestroy()
        context!!.unregisterReceiver(mSongBroadCast)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.mImgPrevious -> Helpers.sendBoardCastServices(
                Constants.keyActionPrevious,
                context!!
            )
            R.id.mImgPause -> {
                if (mCheckServices) {
                    onStartServices()
                    mCheckServices = false
                }
                mImgPause.setImageResource(if (PlayMusicServices.mMediaPlayer!!.isPlaying) R.drawable.ic_play_white else R.drawable.ic_pause_white)
                Helpers.sendBoardCastServices(Constants.keyActionPlay, context!!)
            }
            R.id.mImgNext -> Helpers.sendBoardCastServices(Constants.keyActionPrevious, context!!)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onStartServices() {
        Intent(context, PlayMusicServices::class.java).apply {
            putExtra(Constants.keyPosition, mPosition)
            startForegroundService(context!!, this)
            onCompletionListener()
        }
    }
}