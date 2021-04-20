package com.odogwudev.localmusic.ui.activity.main

import androidx.lifecycle.ViewModelProvider
import com.odogwudev.localmusic.R
import com.odogwudev.localmusic.abstraction.AbstractActivity
import com.odogwudev.localmusic.app.MyApplication
import com.odogwudev.localmusic.utils.ManagerMediaPlayer
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AbstractActivity() {
    private var mMediaManagerMediaPlayer: ManagerMediaPlayer? = null
    private var mMainViewModel: MainViewModel? = null
    private var mMainViewPagerAdappter: MainViewpagerAdapter? = null;

    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun onListenerClicked() {

    }

    override fun onInit() {
        initViewModel()
        initViewpager()
        setUpDataRoom()
    }

    override fun initViewModel() {
        mMainViewModel =
            ViewModelProvider(this, MyApplication.Holder.factory!!)[MainViewModel::class.java]
    }

    private fun initViewpager() {
        mMainViewPagerAdappter = MainViewpagerAdapter(supportFragmentManager, this)
        mViewPager.adapter = mMainViewPagerAdappter

        mTabLayout.apply {
            setupWithViewPager(mViewPager)
            getTabAt(0)!!.setIcon(R.drawable.ic_home)
            getTabAt(1)!!.setIcon(R.drawable.ic_search)
            getTabAt(2)!!.setIcon(R.drawable.ic_libary)
        }
    }

    private fun setUpDataRoom() {
        mMediaManagerMediaPlayer = ManagerMediaPlayer(this)

        mMainViewModel!!.apply {
            deleteTableSong()
            insertListSong(mMediaManagerMediaPlayer!!.getListSong())
        }
    }
}