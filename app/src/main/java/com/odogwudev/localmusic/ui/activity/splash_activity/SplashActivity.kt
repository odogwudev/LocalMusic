package com.odogwudev.localmusic.ui.activity.splash_activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.odogwudev.localmusic.R
import com.odogwudev.localmusic.abstraction.AbstractActivity
import com.odogwudev.localmusic.ui.activity.main.MainActivity

class SplashActivity : AbstractActivity() {
    private var mRunnalble: Runnable? = null
    private var mHandler: Handler? = null
    override fun getContentView(): Int {
        return R.layout.activity_splash
    }

    override fun onListenerClicked() {

    }

    override fun onInit() {
        mHandler = Handler()
        mRunnalble = Runnable {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        mHandler!!.postDelayed(mRunnalble!!, 2000)
    }

    override fun initViewModel() {

    }


}