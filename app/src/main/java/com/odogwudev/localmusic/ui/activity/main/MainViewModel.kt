package com.odogwudev.localmusic.ui.activity.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.odogwudev.localmusic.repositories.MainRepositories
import com.odogwudev.localmusic.room.table.Song

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var mMainRepositories: MainRepositories? = null

    init {
        mMainRepositories = MainRepositories.getInstance(application)
    }

    fun deleteTableSong() {
        mMainRepositories!!.deleteTableSong()
    }

    fun insertListSong(songList: ArrayList<Song>) {
        mMainRepositories!!.insertListSong(songList)
    }

    fun getListSong(): LiveData<List<Song>> {
        return mMainRepositories!!.getListSong()
    }

    fun getListPlayList(): LiveData<List<Song>> {
        return mMainRepositories!!.getListPlayList()
    }

    fun getListMyHistory(): LiveData<List<Song>> {
        return mMainRepositories!!.getListMyHistory()
    }

    fun updateSong(song: Song) {
        mMainRepositories!!.updateSong(song)
    }

    fun searchSong(search: String?): LiveData<List<Song>> {
        return mMainRepositories!!.searchSong(search)
    }
}