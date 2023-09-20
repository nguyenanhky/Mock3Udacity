package com.udacity

import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityDetailBinding
import com.udacity.utils.Constance

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val notificationId = intent.extras?.getInt(Constance.NOTIFICATION_ID_KEY)
        binding.inc.filenameValue.text = intent.extras?.getString(Constance.FILE_NAME_KEY)
        binding.inc.statusValue.text = intent.extras?.getString(Constance.STATUS_KEY)

        binding.inc.fab.setOnClickListener {
            goBack()
        }

        val nm = getSystemService(NotificationManager::class.java) as NotificationManager

        nm.cancel(notificationId!!)
    }

    private fun goBack() {
        onSupportNavigateUp()
    }
    override fun onBackPressed() {
        onSupportNavigateUp()
    }
}
