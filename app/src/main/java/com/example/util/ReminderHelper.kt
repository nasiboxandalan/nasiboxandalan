package com.example.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import com.example.data.AgendaEvent
import java.util.Calendar

object ReminderHelper {

    fun scheduleHMinusOneReminder(context: Context, event: AgendaEvent): Result<String> {
        val targetCal = IndoDateUtils.toCalendar(event.tanggalMulai)
            ?: return Result.failure(IllegalArgumentException("Tanggal acara tidak valid"))

        // H-1 at 08:00 local time
        val reminderCal = (targetCal.clone() as Calendar).apply {
            add(Calendar.DAY_OF_YEAR, -1)
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val now = System.currentTimeMillis()
        val triggerAtMillis = if (reminderCal.timeInMillis > now) {
            reminderCal.timeInMillis
        } else {
            // If H-1 has already passed (e.g., today is H-1 or same day), remind shortly in 10 seconds as confirmation
            now + 10_000L
        }

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(ReminderReceiver.EXTRA_TITLE, "Besok: ${event.judul}")
            putExtra(
                ReminderReceiver.EXTRA_BODY,
                "${IndoDateUtils.formatEventDateDisplay(event)} • ${event.lokasi}"
            )
            putExtra(ReminderReceiver.EXTRA_SLUG, event.slug)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            event.slug.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)

        val reminderDateText = "%02d-%02d-%04d 08:00".format(
            reminderCal.get(Calendar.DAY_OF_MONTH),
            reminderCal.get(Calendar.MONTH) + 1,
            reminderCal.get(Calendar.YEAR)
        )
        return Result.success(
            if (reminderCal.timeInMillis > now) {
                "Pengingat H-1 dijadwalkan ($reminderDateText WIB)"
            } else {
                "Pengingat aktif untuk acara ini!"
            }
        )
    }

    fun openAddToDeviceCalendar(context: Context, event: AgendaEvent): Boolean {
        val cal = IndoDateUtils.toCalendar(event.tanggalMulai) ?: return false
        val startMillis = cal.apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
        }.timeInMillis
        val endMillis = startMillis + 3 * 60 * 60 * 1000L

        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, event.judul)
            putExtra(CalendarContract.Events.EVENT_LOCATION, event.lokasi)
            putExtra(
                CalendarContract.Events.DESCRIPTION,
                "${event.deskripsi}\n\nPenyelenggara: ${event.penyelenggara}\nSumber: ${event.sumberUrl}"
            )
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return try {
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            false
        }
    }
}
