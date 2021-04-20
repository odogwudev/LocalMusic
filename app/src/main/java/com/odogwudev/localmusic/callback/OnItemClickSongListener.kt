package com.odogwudev.localmusic.callback

import com.odogwudev.localmusic.room.table.Song

interface OnItemClickSongListener {
    fun clickOpenItem(songList: List<Song>, position: Int)
    fun clickDeleteItem(songList: List<Song>, position: Int)
}