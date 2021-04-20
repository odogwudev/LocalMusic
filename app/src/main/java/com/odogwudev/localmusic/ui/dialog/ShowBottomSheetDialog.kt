package com.odogwudev.localmusic.ui.dialog

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.odogwudev.localmusic.R
import com.odogwudev.localmusic.app.MyApplication
import com.odogwudev.localmusic.room.table.Song
import com.odogwudev.localmusic.ui.activity.music_list.MusicListViewModel
import kotlinx.android.synthetic.main.layout_fragment_bottom_sheet_dialog.*

class ShowBottomSheetDialog : BottomSheetDialogFragment(), View.OnClickListener {
    private var mView: View? = null
    private var mMusicListViewModel: MusicListViewModel? = null
    private var mSong: Song? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.layout_fragment_bottom_sheet_dialog, container, false)
        initViewModel()
        listenerOnclicked()
        return mView
    }

    private fun listenerOnclicked() {
        mView!!.findViewById<LinearLayout>(R.id.mLinearAddPlayList).setOnClickListener(this)
        mView!!.findViewById<LinearLayout>(R.id.mLinearAddFavourite).setOnClickListener(this)
        mView!!.findViewById<LinearLayout>(R.id.mLinearDeleteSong).setOnClickListener(this)
    }

    private fun initViewModel() {
        mMusicListViewModel = ViewModelProvider(
            activity!!,
            MyApplication.Holder.factory!!
        )[MusicListViewModel::class.java]

        mMusicListViewModel!!.getDataSongMutableLive().observe(viewLifecycleOwner, Observer {
            mSong = it
            mImgAvatar.setImageURI(Uri.parse(it.mPathUriImage))
            mTxtTitle.text = it.mTitle
            mTxtSinger.text = it.mArtist
        })
    }

    override fun onResume() {
        val window = dialog!!.window
        window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.colorPickerStyle)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.mLinearAddPlayList -> mSong?.let {
                it.mPlayList = 1
                mMusicListViewModel!!.updateSong(it)
                Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
            }
            R.id.mLinearAddFavourite -> mSong?.let {
                it.mFavourite = 1
                mMusicListViewModel!!.updateSong(it)
                Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
            }
            R.id.mLinearDeleteSong -> mSong?.let {
                mMusicListViewModel!!.deleteSong(it)
                Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
            }
        }
    }
}