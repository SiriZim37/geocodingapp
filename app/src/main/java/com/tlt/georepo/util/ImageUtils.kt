package com.tlt.georepo.util

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Environment
import android.support.media.ExifInterface
import android.support.v4.content.FileProvider
import android.util.Base64
import android.view.View
import com.tlt.georepo.manager.ContextManager
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


object ImageUtils {

    fun encodeToBase64(uri: Uri): String {
        val bitmap = decodeSampledBitmapFromUri(uri)
        val compress = compress(bitmap)
        return ImageUtils.encodeImage(compress)
    }

    fun getNewWidthAndHeightByMaxScale(width: Float,
                                       height: Float,
                                       max: Float = 600f): Pair<Int, Int> {
        if (width <= max && height <= max) {
            return Pair(width.toInt(), height.toInt())
        }

        var newHeight = max
        var newWidth = max

        if (width > height) {
            newHeight = height / (width / max)
        } else {
            newWidth = width / (height / max)
        }

        return Pair(newWidth.toInt(), newHeight.toInt())
    }

    private fun encodeImage(byteArray: ByteArray): String {
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun compress(bitmap: Bitmap, quality: Int = 80): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    private fun fixImageRotate(bitmap: Bitmap, inputStream: InputStream): Bitmap {
        val exifInterface = ExifInterface(inputStream)
        val matrix = Matrix()
        val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val angle = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }

        matrix.postRotate(angle.toFloat())

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun decodeSampledBitmapFromUri(uri: Uri): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(getInputStreamByUri(uri), null, options)

        options.inSampleSize = calculateInSampleSizeByMaxScaleSize(options)

        options.inJustDecodeBounds = false

        val bitmap = BitmapFactory.decodeStream(getInputStreamByUri(uri), null, options)

        return fixImageRotate(bitmap, getInputStreamByUri(uri))
    }

    private fun calculateInSampleSizeByMaxScaleSize(options: BitmapFactory.Options,
                                                    maxScaleSize: Int = 600): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        val (reqWidth, reqHeight) = getNewWidthAndHeightByMaxScale(width.toFloat(),
                height.toFloat(),
                maxScaleSize.toFloat())

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    private fun getInputStreamByUri(uri: Uri): InputStream {
        val context = ContextManager.getInstance().getApplicationContext()
        return context.contentResolver.openInputStream(uri)
    }

    fun loadBitmapFromView(view: View): Bitmap {
        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background

        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas)
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        }

        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return returnedBitmap
    }

    fun saveToExternalStorage(applicationContext: Context, bitmap: Bitmap): Uri {
        val directory = Environment.getExternalStorageDirectory()
        val dir = File("${directory.absolutePath}/tlt_images")
        dir.mkdirs()

        val fileName = String.format("%d.jpg", System.currentTimeMillis())
        val outFile = File(dir, fileName)
        val outStream = FileOutputStream(outFile)

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        outStream.flush()
        outStream.close()

        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = Uri.fromFile(outFile)
        applicationContext.sendBroadcast(intent)

        return intent.data
    }

    fun saveToExternalStorageAbsolute(applicationContext: Context, bitmap: Bitmap): Uri {
        val directory = Environment.getExternalStorageDirectory()
        val dir = File("${directory.absolutePath}/tlt_images")
        dir.mkdirs()

        val fileName = String.format("%d.jpg", System.currentTimeMillis())
        val outFile = File(dir, fileName)
        val outStream = FileOutputStream(outFile)

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        outStream.flush()
        outStream.close()

        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = FileProvider.getUriForFile(applicationContext, "${applicationContext.packageName}.provider", outFile)
        applicationContext.sendBroadcast(intent)

        return intent.data
    }

    fun saveToInternalStorage(applicationContext: Context, bitmap: Bitmap) {
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val fileName = String.format("%d.jpg", System.currentTimeMillis())
        val outFile = File(directory, fileName)
        val outStream = FileOutputStream(outFile)

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        outStream.flush()
        outStream.close()

        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = Uri.fromFile(outFile)
        applicationContext.sendBroadcast(intent)
    }
}