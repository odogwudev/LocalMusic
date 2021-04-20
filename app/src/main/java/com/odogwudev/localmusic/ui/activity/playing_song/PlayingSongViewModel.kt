package com.odogwudev.localmusic.ui.activity.playing_song

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.odogwudev.localmusic.repositories.MainRepositories
import com.odogwudev.localmusic.room.table.Song
import com.odogwudev.localmusic.utils.SharedPreferences

class PlayingSongViewModel(application: Application) : AndroidViewModel(application) {
    private var mSongLiveData = MutableLiveData<Song>()
    private var mSharedPreferences = SharedPreferences(application)
    private var mMainRepositories = MainRepositories.getInstance(application)

    fun setDataSongMutableLive(song: Song) {
        mSongLiveData.value = song
    }

    fun getDataSongMutableLive(): LiveData<Song> {
        return mSongLiveData
    }

    fun saveSongListSharedPreferences(songList: String?) {
        mSharedPreferences.saveSongList(songList!!)
    }

    fun updateSong(song: Song) {
        mMainRepositories.updateSong(song)
    }
}