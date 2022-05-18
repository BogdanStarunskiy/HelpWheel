package com.example.helpwheel.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class ZoomPageTransformer implements ViewPager2.PageTransformer {
private static final float MIN_SCALE = 0.85f;

@Override
public void transformPage(@NonNull View page, float position) {
        int pageWidth = page.getWidth();
        int pageHeight = page.getHeight();
        if (position < -1) {
        page.setAlpha(0f);
        } else if (position <= 1) {
        float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
        float vertMargin = pageHeight * (1 - scaleFactor) / 2;
        float horzMargin = pageWidth * (1 - scaleFactor) / 2;
        if (position < 0) {
        page.setTranslationX(horzMargin - vertMargin / 2);

        } else {
        page.setTranslationX(-horzMargin + vertMargin / 2);
        }
        page.setScaleX(scaleFactor);
        page.setScaleY(scaleFactor);
        }
        }
        }
