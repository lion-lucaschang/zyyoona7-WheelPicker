package com.zyyoona7.wheel

import android.graphics.Typeface
import android.text.TextPaint

private val sansSerifMedium: Typeface? by lazy {
    Typeface.create("sans-serif-medium", Typeface.NORMAL)
        .takeIf { it != Typeface.DEFAULT }
}


fun TextPaint.setSansSerifMedium() {
    val sansSerifMedium1 = sansSerifMedium
    this.typeface = sansSerifMedium1
}