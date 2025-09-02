package com.yandex.ads.sample

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yandex.ads.sample.tv.TVActivity

class LauncherActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isTv = packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK)
        val intent = if (isTv) {
            Intent(this, TVActivity::class.java)
        } else {
            Intent(this, HomeActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
