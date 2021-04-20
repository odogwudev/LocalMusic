package com.odogwudev.localmusic.ui.fragment.playing_song

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.odogwudev.localmusic.R
import com.odogwudev.localmusic.abstraction.AbstractFragment
import com.odogwudev.localmusic.app.MyApplication
import com.odogwudev.localmusic.room.table.Song
import com.odogwudev.localmusic.ui.activity.playing_song.PlayingSongViewModel
import com.odogwudev.localmusic.utils.Constants
import kotlinx.android.synthetic.main.layout_fragment_info.*

class InfoFragment : AbstractFragment(), View.OnClickListener {
    private var mPlayingSongViewModel: PlayingSongViewModel? = null
    private var mSong: Song? = null
    private var mView: View? = null

    override fun initViewModel() {
        mPlayingSongViewModel = ViewModelProvider(
            activity!!,
            MyApplication.Holder.factory!!
        )[PlayingSongViewModel::class.java]

        mPlayingSongViewModel!!.getDataSongMutableLive()
            .observe(viewLifecycleOwner, Observer { song ->
                mSong = song
                updateUi(mSong)
            })
    }

    private fun updateUi(song: Song?) {
        mImgInfo.setImageURI(Uri.parse(Constants.pathUriImgAlbum + song!!.mIdAlbum))
        mTxtName.text = song.mTitle
        mTxtSinger.text = song.mArtist
        mImgFavorite1.setImageResource(if (song.mFavourite == 1) R.drawable.ic_favorite_white else R.drawable.ic_favorite)
    }

    override fun init() {
        mView!!.findViewById<View>(R.id.mImgFavorite1).setOnClickListener(this)
    }

    override fun onListenerClicked() {}

    override fun getContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.layout_fragment_info, container, false)
        return mView
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.mImgFavorite1 -> {
                mSong!!.mFavourite = if (mSong!!.mFavourite == 0) 1 else 0
                mPlayingSongViewModel!!.updateSong(mSong!!)
                mImgFavorite1.setImageResource(if (mSong!!.mFavourite == 1) R.drawable.ic_favorite_white else R.drawable.ic_favorite)
            }
        }
    }
}