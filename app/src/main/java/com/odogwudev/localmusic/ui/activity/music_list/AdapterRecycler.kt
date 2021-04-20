package com.odogwudev.localmusic.ui.activity.music_list

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.odogwudev.localmusic.R
import com.odogwudev.localmusic.callback.OnItemClickSongListener
import com.odogwudev.localmusic.room.table.Song
import com.squareup.picasso.Picasso

class AdapterRecycler : RecyclerView.Adapter<AdapterRecycler.ViewHolder>() {
    private var mListSong: List<Song> = listOf()
    private var mOnItemClickSongListener: OnItemClickSongListener? = null

    fun setupData(songList: List<Song>, onItemClickSongListener: OnItemClickSongListener) {
        mListSong = songList
        mOnItemClickSongListener = onItemClickSongListener
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mLinerBody: LinearLayout = itemView.findViewById(R.id.line1)
        var mImgAvatar: RoundedImageView = itemView.findViewById(R.id.mImgAvatar)
        var mTxtTitle: TextView = itemView.findViewById(R.id.mTxtTitle)
        var mTxtSinger: TextView = itemView.findViewById(R.id.mTxtSinger)
        var mImgMenu: ImageView = itemView.findViewById(R.id.mImgMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = View.inflate(parent.context, R.layout.item_list_music_list, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var song = mListSong[position]
        holder.mTxtSinger.text = song.mArtist
        holder.mTxtTitle.text = song.mTitle
        Picasso.get().load(Uri.parse(song.mPathUriImage)).error(R.drawable.img_music_error)
            .into(holder.mImgAvatar)
        holder.mLinerBody.setOnClickListener {
            mOnItemClickSongListener!!.clickOpenItem(
                mListSong,
                position
            )
        }
        holder.mImgMenu.setOnClickListener {
            mOnItemClickSongListener!!.clickDeleteItem(
                mListSong,
                position
            )
        }
    }

    override fun getItemCount(): Int {
        return mListSong.size
    }
}