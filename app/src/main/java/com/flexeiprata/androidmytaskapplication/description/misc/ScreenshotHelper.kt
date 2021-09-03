package com.flexeiprata.androidmytaskapplication.description.misc

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import eu.bolt.screenshotty.Screenshot
import eu.bolt.screenshotty.ScreenshotManagerBuilder
import java.io.File
import java.io.FileOutputStream

class ScreenshotHelper(activity: Activity) {

    val builtInstance = ScreenshotManagerBuilder(activity).build()
    val context = activity

    fun screenshotHelperFun(onFinish: (Screenshot) -> Unit){
        val screenshotResult = builtInstance.makeScreenshot()
        screenshotResult.observe(
            onSuccess = { onFinish.invoke(it) },
            onError = {})
    }

    fun proceedScreenshotSave(screenshot: Bitmap?){
        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "screen.png"
        )
        val out = FileOutputStream(file)
        screenshot?.compress(Bitmap.CompressFormat.PNG, 90, out)
        out.close()
    }

    fun getUriFromScreenshot() : Uri? = FileProvider.getUriForFile(
        context,
        context.packageName,
        File(
            context
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "screen.png"
        )
    )
}