package com.example.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import com.example.R
import com.example.data.AgendaEvent
import com.example.data.EventStatus
import com.example.data.PaketData
import java.io.File
import java.io.FileOutputStream

object ExternalIntents {

    fun buildWhatsAppUrl(message: String): String {
        return "https://wa.me/${PaketData.WHATSAPP_NUMBER}?text=${Uri.encode(message)}"
    }

    fun openWhatsApp(context: Context, message: String): Boolean {
        return openUrl(context, buildWhatsAppUrl(message))
    }

    fun openGoogleMapsNasiBox(context: Context): Boolean {
        val query = Uri.encode(PaketData.MAPS_QUERY)
        val geoUri = Uri.parse("geo:0,0?q=$query")
        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return try {
            context.startActivity(mapIntent)
            true
        } catch (_: Exception) {
            openUrl(context, "https://www.google.com/maps/search/?api=1&query=$query")
        }
    }

    fun openUrl(context: Context, url: String): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            false
        }
    }

    fun copyToClipboard(context: Context, label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        clipboard?.setPrimaryClip(ClipData.newPlainText(label, text))
    }

    fun buildEventShareLink(slug: String): String {
        return "https://agendaklaten.id/acara/$slug"
    }

    fun buildEventShareText(event: AgendaEvent): String {
        val dateDisplay = IndoDateUtils.formatEventDateDisplay(event)
        val jamPart = event.jam?.takeIf { it.isNotBlank() }?.let { "\nJam: $it" } ?: ""
        return buildString {
            append("*${event.judul}*\n")
            append("Kategori: ${event.kategori.label}\n")
            append("Tanggal: $dateDisplay$jamPart\n")
            append("Lokasi: ${event.lokasi}\n")
            append("Penyelenggara: ${event.penyelenggara}\n\n")
            append("Info lengkap di AGENDA KLATEN:\n${buildEventShareLink(event.slug)}\n")
            append("Konsumsi acara? Nasi Box Andalan (WA ${PaketData.WHATSAPP_DISPLAY})")
        }
    }

    fun shareEventToWhatsApp(context: Context, event: AgendaEvent) {
        val shareText = buildEventShareText(event)
        try {
            val bitmap = renderShareCardBitmap(context, event)
            val cacheDir = File(context.cacheDir, "share_cards").apply { mkdirs() }
            val imageFile = File(cacheDir, "agenda-${event.slug}.png")
            FileOutputStream(imageFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile
            )
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, shareText)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            // Prefer WhatsApp if installed, otherwise system chooser
            val waIntent = Intent(intent).setPackage("com.whatsapp")
            if (waIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(waIntent)
            } else {
                openWhatsApp(context, shareText)
            }
        } catch (_: Exception) {
            openWhatsApp(context, shareText)
        }
    }

    fun downloadShareCardPng(context: Context, event: AgendaEvent): Result<String> {
        return try {
            val bitmap = renderShareCardBitmap(context, event)
            val fileName = "agenda-klaten-${event.slug}.png"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                    put(
                        MediaStore.Images.Media.RELATIVE_PATH,
                        "${Environment.DIRECTORY_PICTURES}/AgendaKlaten"
                    )
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    ?: return Result.failure(IllegalStateException("MediaStore URI null"))
                resolver.openOutputStream(uri)?.use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                } ?: return Result.failure(IllegalStateException("OutputStream null"))
                values.clear()
                values.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(uri, values, null, null)
                Result.success("Tersimpan di Galeri (Pictures/AgendaKlaten/$fileName)")
            } else {
                // On API 24-28 without broad storage permission, share via FileProvider so user can save
                val cacheDir = File(context.cacheDir, "share_cards").apply { mkdirs() }
                val imageFile = File(cacheDir, fileName)
                FileOutputStream(imageFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    imageFile
                )
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "image/png"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(shareIntent, "Simpan atau Bagikan Kartu Acara"))
                Result.success("Pilih Simpan atau Bagikan kartu acara.")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Renders a crisp 4:5 (1080x1350) PNG visual card matching ShareCard UI.
     */
    fun renderShareCardBitmap(context: Context, event: AgendaEvent): Bitmap {
        val width = 1080
        val height = 1350
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val normalTypeface = try {
            ResourcesCompat.getFont(context, R.font.plus_jakarta_sans) ?: Typeface.SANS_SERIF
        } catch (_: Exception) {
            Typeface.SANS_SERIF
        }
        val boldTypeface = try {
            ResourcesCompat.getFont(context, R.font.plus_jakarta_sans_bold)
                ?: Typeface.create(normalTypeface, Typeface.BOLD)
        } catch (_: Exception) {
            Typeface.create(normalTypeface, Typeface.BOLD)
        }

        // Cream background
        canvas.drawColor(0xFFFFFBF2.toInt())

        // Decorative top green banner
        val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF1F5C3A.toInt() }
        canvas.drawRect(0f, 0f, width.toFloat(), 250f, headerPaint)

        // Accent amber stripe
        val amberPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFF59E0B.toInt() }
        canvas.drawRect(0f, 250f, width.toFloat(), 266f, amberPaint)

        // Header brand text
        val brandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFFFFFFFF.toInt()
            textSize = 46f
            typeface = boldTypeface
            letterSpacing = 0.06f
        }
        canvas.drawText("AGENDA KLATEN", 72f, 120f, brandPaint)

        val taglinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFFFEF3C7.toInt()
            textSize = 28f
            typeface = normalTypeface
        }
        canvas.drawText("Direktori Acara & Konsumsi Warga Klaten", 72f, 174f, taglinePaint)

        // Category + status pill
        val status = IndoDateUtils.effectiveStatus(event)
        val pillBgColor = when (status) {
            EventStatus.TERKONFIRMASI -> 0xFFDCFCE7.toInt()
            EventStatus.DIJADWALKAN -> 0xFFFEF3C7.toInt()
            EventStatus.MENUNGGU_KONFIRMASI -> 0xFFF1F5F9.toInt()
            EventStatus.SUDAH_LEWAT -> 0xFFFCE7F3.toInt()
        }
        val pillTextColor = when (status) {
            EventStatus.TERKONFIRMASI -> 0xFF166534.toInt()
            EventStatus.DIJADWALKAN -> 0xFFB45309.toInt()
            EventStatus.MENUNGGU_KONFIRMASI -> 0xFF475569.toInt()
            EventStatus.SUDAH_LEWAT -> 0xFF9D174D.toInt()
        }
        val pillText = "${event.kategori.label.uppercase()}  •  ${status.label.uppercase()}"
        val pillTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = pillTextColor
            textSize = 26f
            typeface = boldTypeface
        }
        val pillWidth = pillTextPaint.measureText(pillText) + 56f
        val pillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = pillBgColor }
        canvas.drawRoundRect(RectF(72f, 326f, 72f + pillWidth, 392f), 33f, 33f, pillPaint)
        canvas.drawText(pillText, 100f, 368f, pillTextPaint)

        // Event Title (multiline)
        val titlePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFF18221B.toInt()
            textSize = 62f
            typeface = boldTypeface
        }
        val titleLayout = StaticLayout.Builder
            .obtain(event.judul, 0, event.judul.length, titlePaint, width - 144)
            .setLineSpacing(10f, 1f)
            .setMaxLines(4)
            .build()
        canvas.save()
        canvas.translate(72f, 442f)
        titleLayout.draw(canvas)
        canvas.restore()

        // Details box
        val infoTop = 442f + titleLayout.height + 56f
        val boxPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFF6EFE0.toInt() }
        val boxBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFFE5DCC9.toInt()
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }
        val boxRect = RectF(72f, infoTop, width - 72f, 1130f)
        canvas.drawRoundRect(boxRect, 36f, 36f, boxPaint)
        canvas.drawRoundRect(boxRect, 36f, 36f, boxBorder)

        val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFF1F5C3A.toInt()
            textSize = 26f
            typeface = boldTypeface
        }
        val valuePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFF18221B.toInt()
            textSize = 34f
            typeface = boldTypeface
        }

        var cursorY = infoTop + 60f
        fun drawDetailRow(label: String, value: String) {
            canvas.drawText(label, 112f, cursorY, labelPaint)
            cursorY += 16f
            val layout = StaticLayout.Builder
                .obtain(value, 0, value.length, valuePaint, width - 224)
                .setMaxLines(2)
                .build()
            canvas.save()
            canvas.translate(112f, cursorY)
            layout.draw(canvas)
            canvas.restore()
            cursorY += layout.height + 42f
        }

        val dateWithJam = buildString {
            append(IndoDateUtils.formatEventDateDisplay(event))
            if (!event.jam.isNullOrBlank()) append(" • ${event.jam}")
        }
        drawDetailRow("TANGGAL", dateWithJam)
        drawDetailRow("LOKASI", event.lokasi)
        drawDetailRow("PENYELENGGARA", event.penyelenggara)

        // Footer bar
        val footerRect = RectF(0f, 1188f, width.toFloat(), height.toFloat())
        canvas.drawRect(footerRect, headerPaint)
        val footerMainPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFFFFFFFF.toInt()
            textSize = 32f
            typeface = boldTypeface
        }
        val footerSubPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFFFDE68A.toInt()
            textSize = 26f
            typeface = normalTypeface
        }
        canvas.drawText("Konsumsi acara? Nasi Box Andalan", 72f, 1260f, footerMainPaint)
        canvas.drawText("Depan RSI Klaten • WA ${PaketData.WHATSAPP_DISPLAY}", 72f, 1304f, footerSubPaint)

        return bitmap
    }
}
