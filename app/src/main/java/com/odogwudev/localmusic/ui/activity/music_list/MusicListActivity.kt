package com.odogwudev.localmusic.ui.activity.music_list

import android.content.Intent
import android.os.Build
import android.os.Handler
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.odogwudev.localmusic.R
import com.odogwudev.localmusic.abstraction.AbstractActivity
import com.odogwudev.localmusic.app.MyApplication
import com.odogwudev.localmusic.callback.OnItemClickSongListener
import com.odogwudev.localmusic.room.table.Song
import com.odogwudev.localmusic.services.PlayMusicServices
import com.odogwudev.localmusic.ui.activity.playing_song.PlayingSongActivity
import com.odogwudev.localmusic.ui.dialog.ShowBottomSheetDialog
import com.odogwudev.localmusic.ui.fragment.music_list.MusicListFragment.Companion.mHandler
import com.odogwudev.localmusic.utils.Constants
import kotlinx.android.synthetic.main.activity_music_list.*
import kotlinx.android.synthetic.main.activity_music_list.mRelativeBottom

class MusicListActivity : AbstractActivity(), View.OnClickListener, OnItemClickSongListener {
    private var mMusicListViewModel: MusicListViewModel? = null
    private var mAdapter: AdapterRecycler = AdapterRecycler()
    private var mSongList: List<Song> = listOf()

    override fun initViewModel() {
        mMusicListViewModel =
            ViewModelProvider(this, MyApplication.Holder.factory!!)[MusicListViewModel::class.java]

        mMusicListViewModel!!.getListSong().observe(this, Observer { songList ->
            mSongList = songList
            mAdapter.setupData(songList, this)
        })
    }

    override fun getContentView(): Int {
        return R.layout.activity_music_list
    }

    override fun onListenerClicked() {
        mImgBack.setOnClickListener(this)
        mCardViewPlayRandom.setOnClickListener(this)
    }

    override fun onInit() {
        initRecyclerView()
    }

    private fun updateUi() {
        PlayingSongActivity.mRunnable?.let { PlayingSongActivity.mHandler?.removeCallbacks(it) }

        var runnable = Runnable {
            if (PlayMusicServices.mMediaPlayer == null) {
                mRelativeBottom.visibility = GONE
            } else {
                mRelativeBottom.visibility = VISIBLE
            }
        }
        Handler().postDelayed(runnable, 500)
    }

    private fun initRecyclerView() {
        var recycler = findViewById<RecyclerView>(R.id.mRecyclerViewMusicList)
        recycler.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@MusicListActivity, 1)
            adapter = mAdapter
            mAdapter.notifyDataSetChanged()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.mImgBack -> finish()
        }
    }

    override fun clickOpenItem(songList: List<Song>, position: Int) {
        Intent(this, PlayingSongActivity::class.java).apply {
            putExtra(Constants.keySongList, Gson().toJson(songList))
            putExtra(Constants.keyPosition, position)
            startActivityForResult(this, Constants.REQUEST_CODE)
        }
    }

    override fun clickDeleteItem(songList: List<Song>, position: Int) {
        ShowBottomSheetDialog().show(supportFragmentManager, Constants.keyBottomSheetDialog)
        mMusicListViewModel!!.setDataSongMutableLive(songList[position])
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK) {
            updateUi()
        }
    }
}