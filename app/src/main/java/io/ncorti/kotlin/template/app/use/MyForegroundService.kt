package io.ncorti.kotlin.template.app.use

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import io.ncorti.kotlin.template.app.R
import io.smooth.constraint.Constraint
import io.smooth.use_cases.UseCase
import io.smooth.use_cases.android.foreground.ForegroundService
import io.smooth.use_cases.work.Work

class MyForegroundService(private val context: Context) : ForegroundService<String> {

    init {
        Log.i("MyForegroundService", "CREATED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "wee"
            val descriptionText = "HAHA"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("11", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    override var foregroundServiceCallback: ForegroundService.ForegroundServiceCallback? = null

    override fun getDefaultNotification(): Notification = builder.build()

    var builder = NotificationCompat.Builder(context, "11")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("start")
        .setContentText("starting...")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    override fun getDefaultNotificationId(): Int = 50

    override fun onExecuting(work: Work<*, *, *>): Boolean {
        Log.i("MyForegroundService", "Executing")
        return false
    }

    override fun onProgress(work: Work<*, *, *>, progress: Map<String, Any>): Boolean {
        Log.i("MyForegroundService", "Progress")
        updateNotification(
            builder
                .setContentText(progress["progress"].toString())
                .build()
        )
        return false
    }

    override fun onResult(work: Work<*, *, *>, result: String): Boolean {
        Log.i("MyForegroundService", result)
        updateNotification(
            builder
                .setContentTitle(result)
                .build()
        )
        return false
    }

    override fun onFailed(
        work: Work<*, *, *>,
        error: Throwable
    ): Boolean = false

    override fun onConstraintsNotMet(
        work: Work<*, *, *>,
        blockingConstraints: List<Constraint>
    ): Boolean = false

    override fun onCancelled(
        work: Work<*, *, *>,
        cancelReason: Throwable?
    ): Boolean = false

    override fun onCompleted(work: Work<*, *, *>): Boolean =
        false

}