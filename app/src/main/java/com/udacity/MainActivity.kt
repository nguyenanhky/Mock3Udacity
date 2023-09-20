package com.udacity

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.udacity.databinding.ActivityMainBinding
import com.udacity.utils.Constance

class MainActivity : AppCompatActivity() {

    private lateinit var loadingButton: LoadingButton
    private lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0
    private lateinit var currentUrl: String
    private lateinit var filename: String
    private lateinit var rdGroup: RadioGroup

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        loadingButton = findViewById(R.id.custom_button)
        rdGroup = findViewById(R.id.radioGroup)
        notificationManager = getSystemService(NotificationManager::class.java)
        Constance.radioButtonIsSelected = false
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

       notificationManager.createNotificationChannel(
           CHANNEL_ID,
           getString(R.string.notification_channel_name)
       );
        loadingButton.setOnClickListener {

            if (Constance.radioButtonIsSelected) {
                download()

            } else {
                Toast.makeText(
                    this,
                    getString(R.string.no_radio_option_selected_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("Range")
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val intentAction = intent?.action
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intentAction) {
                Constance.isDownloadComplete = true
            }

            var statusString = getString(R.string.download_failed_string)

            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE)
                    as DownloadManager
            val cursor = downloadManager.query(
                DownloadManager
                    .Query()
                    .setFilterById(downloadID)
            )

            if (cursor.moveToFirst()) {
                val valueOfStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                cursor.columnNames.forEach { println(it) }
                if (valueOfStatus == DownloadManager.STATUS_SUCCESSFUL) {
                    statusString = getString(R.string.download_success_string)
                }
            }

            cursor.close()

            notificationManager.sendNotification(this@MainActivity,id!!,statusString,filename)
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    fun onRadioButtonClicked(view: View) {

        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked
            if (checked) Constance.radioButtonIsSelected = true

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_glide ->
                    if (checked) {
                        currentUrl = GLIDE_URL
                        filename = getString(R.string.radio_option_1)
                    }
                R.id.radio_loadapp ->
                    if (checked) {
                        // Ninjas rule
                        currentUrl = URL
                        filename = getString(R.string.radio_option_2)
                    }
                else -> if (checked) {
                    currentUrl = RETROFIT_URL
                    filename = getString(R.string.radio_option_3)
                }

            }
        }
    }

    companion object {
        private const val GLIDE_URL = "https://github.com/bumptech/glide"
        private const val RETROFIT_URL = "https://github.com/square/retrofit"
        private const val URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }
}