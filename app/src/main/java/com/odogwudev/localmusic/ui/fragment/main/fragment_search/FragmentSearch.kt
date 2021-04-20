package com.odogwudev.localmusic.ui.fragment.main.fragment_search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.odogwudev.localmusic.R
import com.odogwudev.localmusic.abstraction.AbstractFragment
import com.odogwudev.localmusic.app.MyApplication
import com.odogwudev.localmusic.callback.OnItemClickSongListener
import com.odogwudev.localmusic.model.TextWatcher
import com.odogwudev.localmusic.room.table.Song
import com.odogwudev.localmusic.ui.activity.main.MainViewModel
import com.odogwudev.localmusic.ui.activity.playing_song.PlayingSongActivity
import com.odogwudev.localmusic.utils.Constants
import kotlinx.android.synthetic.main.layout_fragment_search.*

class FragmentSearch : AbstractFragment(), OnItemClickSongListener {
    private var mView: View? = null
    private var mMainViewModel: MainViewModel? = null
    private var mSongListHistory: List<Song> = listOf()
    private var mAdapter = AdapterRecycler()
    private var mBoolean = true

    override fun initViewModel() {
        mMainViewModel =
            ViewModelProvider(activity!!, MyApplication.Holder.factory!!)[MainViewModel::class.java]

        mMainViewModel!!.getListMyHistory().observe(viewLifecycleOwner, Observer { songList ->
            if (mBoolean) {
                mSongListHistory = songList
                mTxt1.visibility = VISIBLE
                mTxt2.visibility = VISIBLE
                mAdapter.setupData(songList, this@FragmentSearch, true)
            }
        })
    }

    override fun init() {
        initRecyclerView()
        listenerEditText()
    }

    private fun listenerEditText() {
        mView!!.findViewById<EditText>(R.id.mEdtSearch)
            .addTextChangedListener(object : TextWatcher() {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    super.onTextChanged(s, start, before, count)
                    if (count == 0) {
                        mBoolean = true
                        mTxt1.visibility = VISIBLE
                        mTxt2.visibility = VISIBLE
                        mAdapter.setupData(mSongListHistory!!, this@FragmentSearch, true)
                    } else {
                        mBoolean = false
                        mMainViewModel!!.searchSong(s.toString())
                            .observe(viewLifecycleOwner, Observer { songList ->
                                if (!mBoolean) {
                                    mTxt1.visibility = GONE
                                    mTxt2.visibility = GONE
                                    mAdapter.setupData(songList, this@FragmentSearch, false)
                                }
                            })
                    }
                }
            })
    }

    override fun onListenerClicked() {}

    override fun getContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.layout_fragment_search, container, false)
        return mView
    }

    private fun initRecyclerView() {
        var recyclerView = mView!!.findViewById<RecyclerView>(R.id.mRecyclerViewHistory)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 1)
            adapter = mAdapter
        }
    }

    override fun clickOpenItem(songList: List<Song>, position: Int) {
        mBoolean = true
        songList[position].apply {
            this.mHistory = 1
            mMainViewModel!!.updateSong(this)
        }

        Intent(context, PlayingSongActivity::class.java).apply {
            putExtra(Constants.keySongList, Gson().toJson(songList))
            putExtra(Constants.keyPosition, position)
            startActivity(this)
        }
    }

    override fun clickDeleteItem(songList: List<Song>, position: Int) {
        songList[position].apply {
            mBoolean = true
            this.mHistory = 0
            mMainViewModel!!.updateSong(this)
        }
    }
}