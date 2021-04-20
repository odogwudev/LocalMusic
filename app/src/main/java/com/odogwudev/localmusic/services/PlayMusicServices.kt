package com.odogwudev.localmusic.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.odogwudev.localmusic.R
import com.odogwudev.localmusic.room.table.Song
import com.odogwudev.localmusic.utils.Constants
import com.odogwudev.localmusic.utils.Notification
import com.odogwudev.localmusic.utils.SharedPreferences
import java.io.IOException


class PlayMusicServices : Service() {
    private var mNotification: Notification = Notification()
    private var mSharedPreferences: SharedPreferences? = null
    private var mList: List<Song>? = null
    private var mPosition = 0

    companion object {
        var mMediaPlayer: MediaPlayer? = null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        registerReceiver(broadcastReceiver, IntentFilter(Constants.sendActionBroadCastServices))
        mPosition = intent!!.getIntExtra(Constants.keyPosition, 0)
        init()

        sendBroadCastPlayingSongActivity(null)
        startForeground(Constants.NOTIFICATION_ID, mNotification!!.mNotification)
        return START_NOT_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init() {
        mSharedPreferences = SharedPreferences(this)
        mList = mSharedPreferences!!.getSongList()

        starMediaPlayWithUri()

        starForegroundServices(this, R.drawable.ic_pause_black)
    }


    override fun onDestroy() {
        super.onDestroy()
        mMediaPlayer!!.stop()
        unregisterReceiver(broadcastReceiver)
    }

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Constants.sendActionBroadCastServices) {
                if (intent.hasExtra(Constants.keyAction)) {
                    var action = intent.getStringExtra(Constants.keyAction)
                    when (action) {
                        Constants.keyActionPlay -> {
                            starForegroundServices(
                                context,
                                if (mMediaPlayer!!.isPlaying) R.drawable.ic_play_black else R.drawable.ic_pause_black
                            )
                            if (mMediaPlayer!!.isPlaying) mMediaPlayer!!.pause() else mMediaPlayer!!.start()
                        }

                        Constants.keyActionNext -> {
                            mPosition = if (mPosition >= mList!!.size - 1) 0 else ++mPosition

                            sendBroadCastPlayingSongActivity(null)
                            starMediaPlayWithUri()
                            starForegroundServices(context, R.drawable.ic_pause_black)
                        }
                        Constants.keyActionPrevious -> {
                            mPosition =
                                if (mPosition >= 1 && mList!!.size > 1) --mPosition else mList!!.size - 1
                            sendBroadCastPlayingSongActivity(null)
                            starMediaPlayWithUri()
                            starForegroundServices(context, R.drawable.ic_pause_black)
                        }
                        Constants.keyActionClose -> {
                            sendBroadCastPlayingSongActivity(Constants.keyActionClose)
                            stopSelf()
                        }
                        else -> Toast.makeText(
                            context,
                            getString(R.string.lbl_erro_song),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun starMediaPlayWithUri() {
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer!!.stop()
            }
            mMediaPlayer = MediaPlayer.create(this, Uri.parse(mList!![mPosition].mPath))
            mMediaPlayer!!.start()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, getString(R.string.lbl_erro_song), Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun starForegroundServices(context: Context, icon: Int) {
        mNotification!!.createNotification(context, icon, mPosition, Gson().toJson(mList))
        startForeground(Constants.NOTIFICATION_ID, mNotification!!.mNotification)
    }

    private fun sendBroadCastPlayingSongActivity(action: String?) {
        Intent(Constants.sendActionBroadCastActivity).apply {
            putExtra(Constants.keyPosition, mPosition)
            putExtra(Constants.keyAction, action)
            putExtra(Constants.keySongList, Gson().toJson(mList))
            sendBroadcast(this)
        }
    }
}