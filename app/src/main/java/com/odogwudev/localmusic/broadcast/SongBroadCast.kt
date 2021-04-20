package com.odogwudev.localmusic.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.odogwudev.localmusic.callback.OnListenerBroadCast
import com.odogwudev.localmusic.room.table.Song
import com.odogwudev.localmusic.utils.Constants
import com.odogwudev.localmusic.utils.Helpers

class SongBroadCast : BroadcastReceiver() {
    private var mPosition = 0
    private var mSongList: List<Song> = listOf()
    private var mOnListenerBroadCast: OnListenerBroadCast? = null

    fun setUpdate(onListenerBroadCast: OnListenerBroadCast) {
        mOnListenerBroadCast = onListenerBroadCast
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        var action: String? = "hhh"

        if (intent!!.action == Constants.sendActionBroadCastActivity) {
            mPosition = intent.getIntExtra(Constants.keyPosition, 0)
            mSongList = Helpers.getSongListFromString(intent.getStringExtra(Constants.keySongList))!!

            if (intent.hasExtra(Constants.keyAction)) {
                action = intent.getStringExtra(Constants.keyAction)
            }
            mOnListenerBroadCast!!.onListenerActionClose(mPosition, mSongList, action ?: "kk")
        }
    }
}