package com.odogwudev.localmusic.ui.activity.playing_song

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.odogwudev.localmusic.R
import com.odogwudev.localmusic.abstraction.AbstractActivity
import com.odogwudev.localmusic.app.MyApplication
import com.odogwudev.localmusic.broadcast.SongBroadCast
import com.odogwudev.localmusic.callback.OnListenerBroadCast
import com.odogwudev.localmusic.model.OnSeekBarChangeListener
import com.odogwudev.localmusic.room.table.Song
import com.odogwudev.localmusic.services.PlayMusicServices
import com.odogwudev.localmusic.ui.fragment.music_list.MusicListFragment
import com.odogwudev.localmusic.utils.Constants
import com.odogwudev.localmusic.utils.Helpers
import kotlinx.android.synthetic.main.activity_playing_song.*

class PlayingSongActivity : AbstractActivity(), View.OnClickListener, OnListenerBroadCast {
    private var mPlayingSongViewModel: PlayingSongViewModel? = null
    private var mSongList: List<Song>? = null
    private var mPosition: Int = 0
    private var mAdapter: PlayingSongViewpagerAdapter? = null
    private var mCheckServices = false
    private var mSongBroadCast = SongBroadCast()

    companion object {
        var mRunnable: Runnable? = null
        var mHandler: Handler = Handler()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSongBroadCast.setUpdate(this)
        registerReceiver(mSongBroadCast, IntentFilter(Constants.sendActionBroadCastActivity))
    }

    override fun getContentView(): Int {
        return R.layout.activity_playing_song
    }

    override fun onListenerClicked() {
        mImgPause.setOnClickListener(this)
        mImgNext.setOnClickListener(this)
        mImgPrevious.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onInit() {
        handlingData()
        initSeekBar()
        setAdapterViewpager()
        onCompletionListener()
    }

    private fun onCompletionListener() {
        MusicListFragment.mRunnable?.let {
            MusicListFragment.mHandler?.removeCallbacks(
                MusicListFragment.mRunnable!!
            )
        }
        var runnable = Runnable {
            mSeekBar.max = PlayMusicServices.mMediaPlayer!!.duration
            mTxtTimeDuration.text =
                Helpers.getTimeMusicFromMediaplayerDuration(PlayMusicServices.mMediaPlayer!!.duration)

            mSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener() {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    super.onProgressChanged(seekBar, progress, fromUser)
                    if (fromUser) {
                        PlayMusicServices.mMediaPlayer!!.seekTo(progress)
                        mSeekBar.progress = progress
                    }
                }
            })

            PlayMusicServices.mMediaPlayer!!.setOnCompletionListener {
                onMusicNext()
            }
        }
        Handler().postDelayed(runnable, 500)
    }

    private fun initSeekBar() {
        mRunnable = Runnable {
            mTxtTimeStar.text =
                Helpers.getTimeMusicFromMediaplayerDuration(PlayMusicServices.mMediaPlayer!!.currentPosition)
            mSeekBar.progress = PlayMusicServices.mMediaPlayer!!.currentPosition
            mHandler.postDelayed(mRunnable!!, 1000)
        }
        mHandler.postDelayed(mRunnable!!, 500)
    }

    private fun setAdapterViewpager() {
        mAdapter = PlayingSongViewpagerAdapter(supportFragmentManager, applicationContext)
        mViewPagerPlaying.adapter = mAdapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handlingData() {
        if (intent.hasExtra(Constants.keySongList) && intent.hasExtra(Constants.keyPosition)) {
            Helpers.stopService(this)

            mSongList = Helpers.getSongListFromString(intent.getStringExtra(Constants.keySongList))
            mPosition = intent.getIntExtra(Constants.keyPosition, 0)

            mPlayingSongViewModel!!.saveSongListSharedPreferences(intent.getStringExtra(Constants.keySongList))

            onStartServices()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onStartServices() {
        Intent(this, PlayMusicServices::class.java).apply {
            putExtra(Constants.keyPosition, mPosition)
            startForegroundService(this)
            onCompletionListener()
        }
    }

    override fun initViewModel() {
        mPlayingSongViewModel = ViewModelProvider(
            this,
            MyApplication.Holder.factory!!
        )[PlayingSongViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.mImgPause -> {
                if (mCheckServices) {
                    onStartServices()
                    mCheckServices = false
                }
                Helpers.sendBoardCastServices(Constants.keyActionPlay, this)
            }
            R.id.mImgNext -> onMusicNext()
            R.id.mImgPrevious -> Helpers.sendBoardCastServices(Constants.keyActionPrevious, this)

        }
    }

    private fun onMusicNext() {
        Helpers.sendBoardCastServices(Constants.keyActionNext, this)
        onCompletionListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mSongBroadCast)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_OK)
            finish()
        }
        return true
    }

    override fun onListenerActionClose(position: Int, songList: List<Song>, action: String) {
        mImgPause.setImageResource(R.drawable.ic_pause_white)
        if (action == Constants.keyActionClose) {
            mCheckServices = true
            mImgPause.setImageResource(R.drawable.ic_play_white)
        }
        mPlayingSongViewModel!!.setDataSongMutableLive(songList[position])
    }
}