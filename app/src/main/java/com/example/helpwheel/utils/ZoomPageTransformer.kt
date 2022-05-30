package com.example.helpwheel.utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs
import kotlin.math.max

class ZoomPageTransformer: ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val pageHeight = page.height
        if(position<=-1){
            page.alpha = 0F
        }else if(position<=1){
            val scaleFactor = max(MIN_SCALE, 1 - abs(position))
            val vertMargin = pageHeight * (1 - scaleFactor) / 2
            val horzMargin = pageWidth * (1 - scaleFactor) / 2
            if (position<0){
                page.translationX = horzMargin - vertMargin / 2
            }
            else{
                page.translationX = -horzMargin + vertMargin / 2
            }
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
        }
    }
}