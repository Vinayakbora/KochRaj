package com.example.kochraj.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.net.Uri
import java.io.ByteArrayOutputStream

object ImageUtils {

    /**
     * Converts a Uri to a Bitmap and resizes it to the specified dimensions
     */
    fun getBitmapFromUri(context: Context, uri: Uri, targetSize: Int): Bitmap? {
        return try {
            // Get input stream from Uri
            val inputStream = context.contentResolver.openInputStream(uri)

            // Decode image size first to determine scaling
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()

            // Calculate scaling factor
            val scale = calculateScaleFactor(options.outWidth, options.outHeight, targetSize)

            // Decode with scaling
            val scaledOptions = BitmapFactory.Options().apply {
                inSampleSize = scale
            }

            val scaledInputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(scaledInputStream, null, scaledOptions)
            scaledInputStream?.close()

            // Create a square bitmap (crop to square if needed)
            val squareBitmap = createSquareBitmap(bitmap!!)

            // Create a circular bitmap
            createCircularBitmap(squareBitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Converts a Bitmap to a ByteArray for upload
     */
    fun bitmapToByteArray(bitmap: Bitmap, quality: Int = 90): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        return outputStream.toByteArray()
    }

    /**
     * Calculates the sample size for downsampling the image
     */
    private fun calculateScaleFactor(width: Int, height: Int, targetSize: Int): Int {
        var scale = 1
        while (width / (scale * 2) >= targetSize && height / (scale * 2) >= targetSize) {
            scale *= 2
        }
        return scale
    }

    /**
     * Creates a square bitmap by cropping the longer dimension
     */
    private fun createSquareBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        if (width == height) {
            return bitmap
        }

        val size = minOf(width, height)
        val x = (width - size) / 2
        val y = (height - size) / 2

        return Bitmap.createBitmap(bitmap, x, y, size, size)
    }

    /**
     * Creates a circular bitmap from a square bitmap
     */
    private fun createCircularBitmap(squareBitmap: Bitmap): Bitmap {
        val size = squareBitmap.width
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.BLACK
        }

        val rect = Rect(0, 0, size, size)
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(squareBitmap, rect, rect, paint)

        return output
    }
}

