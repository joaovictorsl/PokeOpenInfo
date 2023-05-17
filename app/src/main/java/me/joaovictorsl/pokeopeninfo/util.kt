package me.joaovictorsl.pokeopeninfo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileInputStream

fun getFileAsBitmap(context: Context, filename: String): Bitmap {
    val f = File(context.filesDir, filename)
    val stream = FileInputStream(f)
    val byteArray = stream.readBytes()

    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

    stream.close()
    return bitmap
}
