package com.tlt.georepo.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import android.util.Base64
import java.io.*
import java.lang.Exception
import android.support.v4.content.FileProvider.getUriForFile
import org.apache.commons.io.FileUtils
import android.provider.MediaStore
import android.provider.DocumentsContract
import android.content.ContentUris
import android.os.Build
import com.tlt.georepo.activity.FilePath


object PdfUtils {

    private val dir by lazy {
        File("${Environment.getExternalStorageDirectory().absolutePath}/tlt_pdf")
    }

    fun savePdf(filename: String = "", base64: String = "") {
        if (isFileExist(filename)) {
            return
        }

        dir.mkdirs()

        val outFile = getFileFromFilename(filename)
        val pdfAsBytes = Base64.decode(base64, Base64.DEFAULT)

        FileOutputStream(outFile, false).apply {
            write(pdfAsBytes)
            flush()
            close()
        }
    }


    fun ExportPdfbase64(   context: Context? ,  fileUrl: Uri) : String {
        try {
        val selectedFilePath = FilePath.getPath(context!!, fileUrl)
        val fileS : File = File(selectedFilePath)
        val fileBase64 =  Base64.encodeToString(fileS.readBytes(), Base64.NO_WRAP)
            return  fileBase64
        } catch (e: IOException) {
            e.printStackTrace()
            return ""
        }
    }

    fun openPdf(context: Context?, filename: Uri?) {

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(filename, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        context?.startActivity(intent)
    }

    fun isFileExist(filename: String = ""): Boolean {
        val file = getFileFromFilename(filename)
        return file.exists()
    }

    private fun getFileFromFilename(filename: String) = File("$dir/$filename")


}