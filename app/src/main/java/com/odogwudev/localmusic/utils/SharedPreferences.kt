package com.odogwudev.localmusic.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.odogwudev.localmusic.room.table.Song

class SharedPreferences(context: Context) {
    private var mSharedPreferences: SharedPreferences =
        context.getSharedPreferences("datalogin", Context.MODE_PRIVATE)
    private var mEditTor: SharedPreferences.Editor = mSharedPreferences!!.edit()
    private val mGson = Gson()
    private val TAG = "SharePrefs"

    fun saveSongList(songList: String) {
        mEditTor.putString(Constants.keySongList, songList).commit()
    }

    fun getSongList(): List<Song> {
        var type = object : TypeToken<List<Song>>() {}.type
        return mGson.fromJson(mSharedPreferences.getString(Constants.keySongList, ""), type)
    }

}