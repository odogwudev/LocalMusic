package com.odogwudev.localmusic.ui.fragment.main.fragment_home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.odogwudev.localmusic.R
import com.odogwudev.localmusic.abstraction.AbstractFragment
import com.odogwudev.localmusic.app.MyApplication
import com.odogwudev.localmusic.callback.OnItemClickSongListener
import com.odogwudev.localmusic.room.table.Song
import com.odogwudev.localmusic.ui.activity.main.MainViewModel
import com.odogwudev.localmusic.ui.activity.playing_song.PlayingSongActivity
import com.odogwudev.localmusic.utils.Constants
import kotlinx.android.synthetic.main.layout_fragment_home.*

class FragmentHome : AbstractFragment(), OnItemClickSongListener {
    private var mMainViewModel: MainViewModel? = null
    private var mAdapter: AdapterRecyclerView? = null

    override fun initViewModel() {
        mMainViewModel =
            ViewModelProvider(activity!!, MyApplication.Holder.factory!!)[MainViewModel::class.java]

        mMainViewModel!!.getListSong().observe(viewLifecycleOwner, Observer { songList ->
            setupRecyclerView(mRecyclerViewRecommended, songList)
        })

        mMainViewModel!!.getListPlayList().observe(viewLifecycleOwner, Observer { songList ->
            setupRecyclerView(mRecyclerViewMyPlayList, songList)
        })
    }

    override fun init() {}

    override fun onListenerClicked() {}

    override fun getContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_fragment_home, container, false)
    }

    override fun clickOpenItem(songList: List<Song>, position: Int) {
        Intent(context, PlayingSongActivity::class.java).apply {
            putExtra(Constants.keySongList, Gson().toJson(songList))
            putExtra(Constants.keyPosition, position)
            startActivity(this)
        }
    }

    override fun clickDeleteItem(songList: List<Song>, position: Int) {

    }

    private fun setupRecyclerView(recyclerView: RecyclerView, songList: List<Song>) {
        recyclerView.apply {
            mAdapter = AdapterRecyclerView()
            this.setHasFixedSize(true)
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = mAdapter
            mAdapter!!.initData(songList, this@FragmentHome)
        }
    }
}