package com.odogwudev.localmusic.ui.fragment.main.fragment_search

import android.net.Uri
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.odogwudev.localmusic.R
import com.odogwudev.localmusic.callback.OnItemClickSongListener
import com.odogwudev.localmusic.room.table.Song
import com.squareup.picasso.Picasso

class AdapterRecycler : RecyclerView.Adapter<AdapterRecycler.ViewHolder>() {
    private var mListSong: List<Song> = listOf()
    private var mOnItemClickSongListener: OnItemClickSongListener? = null
    private var mCheckHistory = false

    fun setupData(
        songList: List<Song>,
        onItemClickSongListener: OnItemClickSongListener,
        checkHistory: Boolean
    ) {
        mListSong = songList
        mOnItemClickSongListener = onItemClickSongListener
        mCheckHistory = checkHistory
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View =
            View.inflate(parent.context, R.layout.item_list_music_fragment_search_history, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var song: Song = mListSong[position]

        holder.mTxtTitle.text = song.mTitle
        holder.mTxtSinger.text = song.mArtist
        Picasso.get().load(Uri.parse(song.mPathUriImage)).error(R.drawable.img_music_error)
            .into(holder.mImgAlbum)

        if (!mCheckHistory) holder.mImgClose.visibility = GONE else holder.mImgClose.visibility =
            VISIBLE

        holder.mRelativeBox.setOnClickListener {
            mOnItemClickSongListener!!.clickOpenItem(mListSong, position)
        }

        holder.mImgClose.setOnClickListener {
            mOnItemClickSongListener!!.clickDeleteItem(
                mListSong,
                position
            )
        }
    }

    override fun getItemCount(): Int {
        return mListSong.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mRelativeBox: RelativeLayout = itemView.findViewById(R.id.mRelativeBox)
        var mImgAlbum: ImageView = itemView.findViewById(R.id.mImgAlbum)
        var mTxtTitle: TextView = itemView.findViewById(R.id.mTxtTitle)
        var mTxtSinger: TextView = itemView.findViewById(R.id.mTxtSinger)
        var mImgClose: ImageView = itemView.findViewById(R.id.mImgClose)
    }
}