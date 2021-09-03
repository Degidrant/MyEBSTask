package com.flexeiprata.androidmytaskapplication.common

import android.content.Context
import android.content.res.ColorStateList
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun FloatingActionButton.paintFab(context: Context, iconTint: Int, bgTing: Int){
    this.backgroundTintList =
        ColorStateList.valueOf(context.getColor(bgTing))
    this.imageTintList =
        ColorStateList.valueOf(context.getColor(iconTint))
}