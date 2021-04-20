package com.odogwudev.localmusic.utils

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import com.odogwudev.localmusic.room.table.Song

class ManagerMediaPlayer {
    private val TAG = "MediaManager"
    private var mContext: Context? = null
    private var mListSong: ArrayList<Song>? = null

    constructor(context: Context) {
        this.mContext = context
        initData();
    }

    private fun initData() {
        mListSong = ArrayList()

        var audioUri =
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI; // quan ly toan bo file am thanh va hinh anh

        var projection = arrayOf(
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.DURATION,
            MediaStore.Audio.AudioColumns.ALBUM_ID,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.AudioColumns.ARTIST
        )

        var where = MediaStore.Audio.AudioColumns.DISPLAY_NAME + " LIKE '%.mp3'"
        var cursor = mContext!!.contentResolver.query(audioUri, projection, where, null, null)

        var path: String
        var title: String
        var duration: String
        var albumId: String
        var album: String
        var artist: String
        var pathUriImageAlbum: String

        if (cursor == null) {
            return;
        }

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            path =
                cursor.getString(0) ///storage/emulated/0/Download/TinhBanDieuKy-AMeeRickyStarLangLD-6927558.mp3
            title = cursor.getString(1)
            duration = cursor.getString(2)
            albumId = cursor.getString(3)
            album = cursor.getString(4)
            artist = cursor.getString(5)
            pathUriImageAlbum = Constants.pathUriImgAlbum + albumId

            var pathLyrics = "defaul"
            var lyrics = path.substring(path.lastIndexOf('-') + 1, path.length).replace(".mp3", "")
            lyrics = lyrics.substring(lyrics.lastIndexOf('/') + 1)

            if (path.contains("/")) {
                pathLyrics = path.substring(0, path.lastIndexOf('/')) + "/.lyric/$lyrics.lrc"
                ///storage/emulated/0/Download/Lyrics/6927558
            }
            mListSong!!.add(
                Song(
                    Helpers.getTimeToDay(),
                    path,
                    title,
                    duration,
                    albumId,
                    album,
                    artist,
                    pathUriImageAlbum,
                    0,
                    pathLyrics,
                    0,
                    0
                )
            )
            cursor.moveToNext()
        }
        cursor.close()
    }

    fun getListSong(): ArrayList<Song> {
        return this.mListSong!!
    }
}