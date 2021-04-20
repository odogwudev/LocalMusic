package com.odogwudev.localmusic.ui.activity.playing_song

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.odogwudev.localmusic.ui.fragment.playing_song.InfoFragment
import com.odogwudev.localmusic.ui.fragment.playing_song.LyricFragment

class PlayingSongViewpagerAdapter(manager: FragmentManager, context: Context) :
    FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var mListFragment: ArrayList<Fragment> = arrayListOf(InfoFragment(), LyricFragment())

    override fun getCount(): Int {
        return mListFragment.size
    }

    override fun getItem(position: Int): Fragment {
        return mListFragment[position]
    }
}