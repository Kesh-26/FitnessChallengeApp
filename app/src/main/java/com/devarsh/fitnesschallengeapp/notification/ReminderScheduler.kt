package com.devarsh.fitnesschallengeapp.notification



import android.content.Context
import androidx.work.*
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

object ReminderScheduler {
    fun schedule(context: Context, time: String, enabled: Boolean) {
        val workManager = WorkManager.getInstance(context)
        if (!enabled) {
            workManager.cancelUniqueWork(ReminderWorker.WORK_NAME)
            return
        }

        val targetTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
        var target = LocalDateTime.now().withHour(targetTime.hour).withMinute(targetTime.minute).withSecond(0)
        if (!target.isAfter(LocalDateTime.now())) target = target.plusDays(1)

        val delay = Duration.between(LocalDateTime.now(), target).toMillis()

        val request = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            ReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }
}