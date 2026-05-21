package org.sharad.velvetinvestment.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readRawBytes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PdfDownloader(
    private val context: Context,
    private val client: HttpClient
) : PdfDownloadManager {

    override fun downloadPdf(
        pdfBytes: ByteArray,
        fileName: String,
        onSuccess: (String?) -> Unit,
        onFailed: (String) -> Unit
    ) {
        try {
            val resolver = context.contentResolver

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // ✅ Android 10+
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.pdf")
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }

                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                    ?: throw Exception("Failed to create file")

                resolver.openOutputStream(uri)?.use {
                    it.write(pdfBytes)
                }

                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)


                val openIntent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/pdf")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                onSuccess(null)
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    openIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                showNotification(fileName, pendingIntent)

            } else {
                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "$fileName.pdf"
                )

                FileOutputStream(file).use {
                    it.write(pdfBytes)
                }

                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )

                val openIntent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/pdf")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    openIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                showNotification(fileName, pendingIntent)
                onSuccess(null)
            }
        } catch (e: Exception) {
            Log("PDF_ERROR", e.message ?: "Unknown error")
            onFailed(e.message ?: "Download failed")
        }
    }

    override suspend fun downloadUrlPdf(
        url: String,
        fileName: String,
        onProgress: (progress: Int) -> Unit,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        println("[DEBUG_LOG] DownloadPdf Android: Starting download - URL: $url, fileName: $fileName")

        try {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Starting download...", Toast.LENGTH_SHORT).show()
                onProgress(0)
            }


            println("[DEBUG_LOG] DownloadPdf Android: Fetching from URL...")

            // Download with progress tracking
            val response: HttpResponse = withContext(Dispatchers.IO) {
                client.get(url) {
                    onDownload { bytesSentTotal, contentLength ->
                        val totalBytes = contentLength ?: 0L
                        if (totalBytes > 0) {
                            val progress = ((bytesSentTotal * 100) / totalBytes).toInt()
                            println("[DEBUG_LOG] DownloadPdf Android: Progress: $progress% ($bytesSentTotal/$totalBytes bytes)")
                            CoroutineScope(Dispatchers.Main).launch {
                                onProgress(progress)
                            }
                        }
                    }
                }
            }

            if (response.status.value in 200..299) {
                val bytes = response.readRawBytes()
                println("[DEBUG_LOG] DownloadPdf Android: Downloaded ${bytes.size} bytes")
                client.close()

                // Save to Downloads folder
                withContext(Dispatchers.IO) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        // Android 10+ use MediaStore
                        val contentValues = ContentValues().apply {
                            put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.pdf")
                            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                        }

                        val resolver = context.contentResolver
                        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                        if (uri != null) {
                            resolver.openOutputStream(uri)?.use { outputStream ->
                                outputStream.write(bytes)
                            }
                            println("[DEBUG_LOG] DownloadPdf Android: File saved via MediaStore")
                        } else {
                            println("[DEBUG_LOG] DownloadPdf Android: Failed to create MediaStore entry")
                            withContext(Dispatchers.Main) {
                                onFailure()
                            }
                            return@withContext
                        }
                    } else {
                        // Android 9 and below - use legacy method
                        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        if (!downloadsDir.exists()) {
                            downloadsDir.mkdirs()
                        }
                        val file = File(downloadsDir, "$fileName.pdf")
                        FileOutputStream(file).use { outputStream ->
                            outputStream.write(bytes)
                        }
                        println("[DEBUG_LOG] DownloadPdf Android: File saved to ${file.absolutePath}")
                    }
                }

                withContext(Dispatchers.Main) {
                    onProgress(100)
                    Toast.makeText(context, "Report downloaded to Downloads folder", Toast.LENGTH_LONG).show()
                    onSuccess()
                }
            } else {
                println("[DEBUG_LOG] DownloadPdf Android: HTTP error ${response.status}")
                client.close()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Download failed: ${response.status}", Toast.LENGTH_SHORT).show()
                    onFailure()
                }
            }
        } catch (e: Exception) {
            println("[DEBUG_LOG] DownloadPdf Android: Exception: ${e.message}")
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Download error: ${e.message}", Toast.LENGTH_LONG).show()
                onFailure()
            }
        }
    }

    private fun showNotification(
        fileName: String,
        pendingIntent: PendingIntent
    ) {

        if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val channelId = "pdf_download_channel"
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create channel (for Android O+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "PDF Downloads",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Download Complete")
            .setContentText("$fileName.pdf saved")
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}