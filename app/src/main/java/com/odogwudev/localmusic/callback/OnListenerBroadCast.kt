package com.odogwudev.localmusic.callback

import com.odogwudev.localmusic.room.table.Song

interface OnListenerBroadCast {
    fun onListenerActionClose(position: Int, songList: List<Song>, action: String)
}