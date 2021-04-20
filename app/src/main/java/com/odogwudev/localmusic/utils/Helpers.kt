package com.odogwudev.localmusic.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.odogwudev.localmusic.room.table.Song
import com.odogwudev.localmusic.services.PlayMusicServices
import java.util.*


class Helpers {
    companion object {
        fun getTimeToDay(): String {
            var calendar = Calendar.getInstance()
            var year = calendar.get(Calendar.YEAR)
            var mounth = calendar.get(Calendar.MONTH) + 1;
            var day = calendar.get(Calendar.DATE)
            return "$year-$mounth-$day"
        }

        fun stopService(context: Context) {
            context.stopService(Intent(context, PlayMusicServices::class.java))
        }

        fun getTimeMusicFromMediaplayerDuration(duration: Int): String? {
            var timerLabel: String? = ""
            val min = duration / 1000 / 60
            val sec = duration / 1000 % 60
            timerLabel += "$min:"
            if (sec < 10) timerLabel += "0"
            timerLabel += sec
            return timerLabel
        }

        fun sendBoardCastServices(action: String, context: Context) {
            var intent1 = Intent(Constants.sendActionBroadCastServices)
            intent1.putExtra(Constants.keyAction, action)
            context.sendBroadcast(intent1)
        }

        fun senPendingIntent(action: String, context: Context, requestCode: Int): PendingIntent {
            var intent1 = Intent(Constants.sendActionBroadCastServices)
            intent1.putExtra(Constants.keyAction, action)

            return PendingIntent.getBroadcast(
                context,
                requestCode,
                intent1,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        fun getSongListFromString(songList: String?): List<Song>? {
            var type = object : TypeToken<List<Song>>() {}.type
            return Gson().fromJson(songList, type)
        }

        fun hideFragmentDialog(fragmentManager: Fragment, content: String?) {
            val prev = fragmentManager.fragmentManager!!.findFragmentByTag(content)
            if (prev != null) {
                val df = prev as DialogFragment
                df.dismiss()
            }
        }
    }
}