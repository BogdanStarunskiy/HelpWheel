package com.example.helpwheel.utils

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.example.helpwheel.R
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec

class BuildBalloon(context: Context, text: String, viewLifecycleOwner: LifecycleOwner) {
    val balloon = Balloon.Builder(context)
        .setWidth(BalloonSizeSpec.WRAP)
        .setHeight(BalloonSizeSpec.WRAP)
        .setText(text)
        .setTextColorResource(R.color.white)
        .setTextSize(15f)
        .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
        .setArrowSize(10)
        .setArrowPosition(0.5f)
        .setPadding(12)
        .setCornerRadius(8f)
        .setBackgroundColorResource(R.color.gray)
        .setBalloonAnimation(BalloonAnimation.ELASTIC)
        .setLifecycleOwner(viewLifecycleOwner)
        .setAutoDismissDuration(5000L)
        .setMarginRight(15)
        .build()
}