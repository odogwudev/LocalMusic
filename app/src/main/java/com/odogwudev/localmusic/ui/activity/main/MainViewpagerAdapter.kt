package com.odogwudev.localmusic.ui.activity.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.odogwudev.localmusic.R
import com.odogwudev.localmusic.ui.fragment.main.fragment_home.FragmentHome
import com.odogwudev.localmusic.ui.fragment.main.fragment_library.FragmentLibrary
import com.odogwudev.localmusic.ui.fragment.main.fragment_search.FragmentSearch

class MainViewpagerAdapter(manager: FragmentManager, context: Context) :
    FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var mListFragment: ArrayList<Fragment> = arrayListOf(
        FragmentHome(),
        FragmentSearch(),
        FragmentLibrary()
    )
    private var mListTitle: ArrayList<String> = arrayListOf(
        context.getString(R.string.lbl_home),
        context.getString(R.string.lbl_search),
        context.getString(R.string.lbl_library)
    )

    override fun getCount(): Int {
        return mListFragment.size
    }

    override fun getItem(position: Int): Fragment {
        return mListFragment[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mListTitle[position]
    }
}