package com.odogwudev.localmusic.ui.fragment.main.fragment_library


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.odogwudev.localmusic.R
import com.odogwudev.localmusic.abstraction.AbstractFragment
import com.odogwudev.localmusic.app.MyApplication
import com.odogwudev.localmusic.ui.activity.main.MainViewModel
import com.odogwudev.localmusic.ui.activity.music_list.MusicListActivity
import kotlinx.android.synthetic.main.layout_fragment_library.*

class FragmentLibrary : AbstractFragment(), View.OnClickListener {
    private var mView: View? = null
    private var mMainViewModel: MainViewModel? = null

    override fun initViewModel() {
        mMainViewModel =
            ViewModelProvider(activity!!, MyApplication.Holder.factory!!)[MainViewModel::class.java]
    }

    override fun init() {
    }

    override fun onListenerClicked() {
        mView!!.findViewById<CardView>(R.id.mCardSong).setOnClickListener(this)
    }

    override fun getContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.layout_fragment_library, container, false)
        return mView
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.mCardSong -> startActivity(Intent(context, MusicListActivity::class.java))
        }
    }
}