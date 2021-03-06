/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hippo.cardsalon;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

class CardViewEclairMr1 implements CardViewImpl {

    final RectF sCornerRect = new RectF();

    @Override
    public void initStatic() {
        // Draws a round rect using 7 draw operations. This is faster than using
        // canvas.drawRoundRect before JBMR1 because API 11-16 used alpha mask textures to draw
        // shapes.
        RoundRectDrawableWithShadow.sRoundRectHelper
                = new RoundRectDrawableWithShadow.RoundRectHelper() {
            @Override
            public void drawRoundRect(Canvas canvas, RectF bounds, float cornerRadius,
                    Paint paint) {
                final float twoRadius = cornerRadius * 2;
                final float innerWidth = bounds.width() - twoRadius - 1;
                final float innerHeight = bounds.height() - twoRadius - 1;
                // increment it to account for half pixels.
                if (cornerRadius >= 1f) {
                    cornerRadius += .5f;
                    sCornerRect.set(-cornerRadius, -cornerRadius, cornerRadius, cornerRadius);
                    int saved = canvas.save();
                    canvas.translate(bounds.left + cornerRadius, bounds.top + cornerRadius);
                    canvas.drawArc(sCornerRect, 180, 90, true, paint);
                    canvas.translate(innerWidth, 0);
                    canvas.rotate(90);
                    canvas.drawArc(sCornerRect, 180, 90, true, paint);
                    canvas.translate(innerHeight, 0);
                    canvas.rotate(90);
                    canvas.drawArc(sCornerRect, 180, 90, true, paint);
                    canvas.translate(innerWidth, 0);
                    canvas.rotate(90);
                    canvas.drawArc(sCornerRect, 180, 90, true, paint);
                    canvas.restoreToCount(saved);
                    //draw top and bottom pieces
                    canvas.drawRect(bounds.left + cornerRadius - 1f, bounds.top,
                            bounds.right - cornerRadius + 1f, bounds.top + cornerRadius,
                            paint);
                    canvas.drawRect(bounds.left + cornerRadius - 1f,
                            bounds.bottom - cornerRadius + 1f, bounds.right - cornerRadius + 1f,
                            bounds.bottom, paint);
                }
                // center
                canvas.drawRect(bounds.left, bounds.top + Math.max(0, cornerRadius - 1f),
                        bounds.right, bounds.bottom - cornerRadius + 1f, paint);
            }
        };
    }

    @Override
    public void initialize(CardHelper cardHelper, Context context, ColorStateList backgroundColor,
            float radius, ColorStateList boundColor, float boundSize, float elevation) {
        final RoundRectDrawableWithShadow backgroundDrawable =
                new RoundRectDrawableWithShadow(backgroundColor, radius,
                        boundColor, boundSize, Math.round(elevation));
        cardHelper.setBackgroundDrawable(backgroundDrawable);

        CardViewDelegate delegate = cardHelper.getDelegate();
        delegate.setRawBackgroundDrawable(backgroundDrawable);
    }

    @Override
    public void updatePadding(CardHelper cardHelper) {
        RoundRectDrawableWithShadow drawable = (RoundRectDrawableWithShadow) cardHelper.getBackgroundDrawable();
        float boundSize = drawable.getBoundSize();
        CardViewDelegate delegate = cardHelper.getDelegate();
        delegate.setPadding(
                cardHelper.getOriginalPaddingLeft() + Math.round(boundSize + drawable.getExtraPaddingLeft()),
                cardHelper.getOriginalPaddingTop() + Math.round(boundSize + drawable.getExtraPaddingTop()),
                cardHelper.getOriginalPaddingRight() + Math.round(boundSize + drawable.getExtraPaddingRight()),
                cardHelper.getOriginalPaddingBottom() + Math.round(boundSize + drawable.getExtraPaddingBottom()));
    }

    @Override
    public void setCornerRadius(CardHelper cardHelper, float radius) {
        RoundRectDrawableWithShadow drawable = (RoundRectDrawableWithShadow) cardHelper.getBackgroundDrawable();
        drawable.setCornerRadius(radius);
    }

    @Override
    public void setBackgroundColor(CardHelper cardHelper, ColorStateList color) {
        RoundRectDrawableWithShadow drawable = (RoundRectDrawableWithShadow) cardHelper.getBackgroundDrawable();
        drawable.setColor(color);
    }

    @Override
    public void setBoundSize(CardHelper cardHelper, float size) {
        RoundRectDrawableWithShadow drawable = (RoundRectDrawableWithShadow) cardHelper.getBackgroundDrawable();
        drawable.setBoundSize(size);
    }

    @Override
    public void setBoundColor(CardHelper cardHelper, ColorStateList color) {
        RoundRectDrawableWithShadow drawable = (RoundRectDrawableWithShadow) cardHelper.getBackgroundDrawable();
        drawable.setBoundColor(color);
    }

    @Override
    public void setElevation(CardHelper cardHelper, float elevation) {
        RoundRectDrawableWithShadow drawable = (RoundRectDrawableWithShadow) cardHelper.getBackgroundDrawable();
        drawable.setElevation(elevation);
    }
}
