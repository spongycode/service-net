package com.spongycode.servicenet

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.spongycode.servicenet.MainActivity.Companion.viewModel
import java.util.Timer
import java.util.TimerTask


class NetworkRequestService : Service() {

    private var time: Int = 0
    private val offset: Int = 5 // regular time interval (5 sec)
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val stopSelf = Intent(this, NetworkRequestService::class.java)
        stopSelf.action = Actions.STOP.toString()
        val pendingIndentStopSelf =
            PendingIntent.getService(
                this, 0, stopSelf,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val contentIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                time++
                if (time % offset == 0) {
                    viewModel.universalSearch()
                }
                time %= offset

                val notification = NotificationCompat.Builder(applicationContext, "network_channel")
                    .setSmallIcon(R.drawable.app_logo)
                    .setContentTitle("Fetching ${viewModel.selectedOptionText.value}...")
                    .setContentText("Last refresh data: $time secs ago")
                    .addAction(
                        R.drawable.ic_launcher_foreground, "STOP",
                        pendingIndentStopSelf
                    )
                    .setContentIntent(contentIntent)
                    .build()
                startForeground(1, notification)
            }

        }, 0, 1000)
    }

    enum class Actions {
        START, STOP
    }
}