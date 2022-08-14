package com.example.notes.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.FragmentActivity
import com.example.notes.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*

object ContentViewUtil {
    fun getContentFromGallery(
        requireActivity: FragmentActivity,
        selectedMediaUri: Intent
    ): String? {
        val ppp = getRealPathFromURI(
            requireActivity,
            Uri.parse(selectedMediaUri.data.toString())
        )
        if (ppp != null) {
            val source = File(ppp)
            var ext = "image"
            if (selectedMediaUri.toString().contains("image")) {
                //handle image
                ext = "image"
            } else if (selectedMediaUri.toString().contains("video")) {
                //handle video
                ext = "video"
            }

            val dest = createFile(
                requireActivity,
                source.extension, ext
            )
            copyFile(source, dest)
            return dest.absolutePath
        } else
            return null
    }

    @SuppressLint("Range")
    fun getRealPathFromURI(requireActivity: FragmentActivity, uri: Uri): String? {
        var displayName: String? = null
        if (uri.toString().startsWith("content://")) {
            var cursor: Cursor? = null
            try {
                cursor = requireActivity.contentResolver.query(uri, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    displayName =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
                }
            } finally {
                cursor!!.close()
            }
        } else if (uri.toString().startsWith("file://")) {
            val myFile = File(uri.toString())
            val path = myFile.absolutePath
            displayName = path
        }
        return displayName
    }

    private fun createFile(context: Context, extension: String, type: String): File {
        val sdf = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS", Locale.US)
        return File(getOutputDirectory(context, type), "$type${sdf.format(Date())}.$extension")
    }

    private fun getOutputDirectory(context: Context, s: String): File {
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, context.resources.getString(R.string.app_name) + "/$s").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else context.filesDir
    }

    @Throws(IOException::class)
    fun copyFile(sourceFile: File?, destFile: File) {
        if (!destFile.getParentFile().exists()) destFile.getParentFile().mkdirs()
        if (!destFile.exists()) {
            destFile.createNewFile()
        }
        var source: FileChannel? = null
        var destination: FileChannel? = null
        try {
            source = FileInputStream(sourceFile).channel
            destination = FileOutputStream(destFile).channel
            destination.transferFrom(source, 0, source.size())
        } finally {
            source?.close()
            destination?.close()
        }
    }
}