package com.odogwudev.localmusic.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import com.odogwudev.localmusic.R
import com.odogwudev.localmusic.utils.Constants

class MyApplication : Application() {
    object Holder {
        var factory: ViewModelProvider.AndroidViewModelFactory? = null
    }

    override fun onCreate() {
        super.onCreate()
        Holder.factory = ViewModelProvider.AndroidViewModelFactory(this)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannel = NotificationChannel(
                Constants.CHANNEL_ID,
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_NONE
            )
            notificationChannel.description = getString(R.string.channel_description)

            var notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}