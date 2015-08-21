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

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;

/**
 * Very simple drawable that draws a rounded rectangle background with arbitrary corners and also
 * reports proper outline for L.
 * <p>
 * Simpler and uses less resources compared to GradientDrawable or ShapeDrawable.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class RoundRectDrawable extends Drawable {

    private ColorStateList mBackgroundColor;
    private ColorStateList mBoundColor;

    private int mCurrentBackgroundColor;
    private int mCurrentBoundColor;

    private float mRadius;
    private final Paint mPaint;
    private final Rect mBounds;
    private final RectF mBoundsF;

    private Paint mBoundPaint;
    private float mBoundSize;
    private RectF mInnerF;

    private boolean mDirty = true;

    public RoundRectDrawable(ColorStateList backgroundColor, float radius, ColorStateList boundColor, float boundSize) {
        mBackgroundColor = backgroundColor;
        mBoundColor = boundColor;

        mRadius = radius;

        mCurrentBackgroundColor = Color.WHITE;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(mCurrentBackgroundColor);

        mBounds = new Rect();
        mBoundsF = new RectF();

        mCurrentBoundColor = Color.WHITE;
        mBoundPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mBoundPaint.setColor(mCurrentBoundColor);

        mBoundSize = boundSize;
        mInnerF = new RectF();
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    protected boolean onStateChange(int[] state) {
        boolean result = super.onStateChange(state);

        int backgroundColor = mBackgroundColor.getColorForState(state, Color.WHITE);
        if (mCurrentBackgroundColor != backgroundColor) {
            mCurrentBackgroundColor = backgroundColor;
            mPaint.setColor(backgroundColor);
            result |= true;
        }

        int boundColor = mBoundColor.getColorForState(state, Color.WHITE);
        if (mCurrentBoundColor != boundColor) {
            mCurrentBoundColor = boundColor;
            mBoundPaint.setColor(boundColor);
            result |= true;
        }

        return result;
    }

    private boolean isDrawBounds() {
        return mBoundSize != 0f;
    }

    @Override
    public void draw(Canvas canvas) {
        if (mDirty) {
            buildComponents(getBounds());
            mDirty = false;
        }

        if (isDrawBounds()) {
            canvas.drawRoundRect(mBoundsF, mRadius, mRadius, mBoundPaint);
            canvas.drawRoundRect(mInnerF, mRadius, mRadius, mPaint);
        } else {
            canvas.drawRoundRect(mBoundsF, mRadius, mRadius, mPaint);
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        updateBounds();
    }

    private void updateBounds() {
        mDirty = true;
    }

    private void buildComponents(Rect bounds) {
        mBounds.set(bounds);
        mBoundsF.left = bounds.left;
        mBoundsF.top = bounds.top;
        mBoundsF.right = bounds.right;
        mBoundsF.bottom = bounds.bottom;

        if (isDrawBounds()) {
            float boundSize = mBoundSize;
            mInnerF.left = mBoundsF.left + boundSize;
            mInnerF.top = mBoundsF.top + boundSize;
            mInnerF.right = mBoundsF.right - boundSize;
            mInnerF.bottom = mBoundsF.bottom - boundSize;
        } else {
            mInnerF.set(mBoundsF);
        }
    }

    @Override
    public void getOutline(@NonNull Outline outline) {
        outline.setRoundRect(getBounds(), mRadius);
    }

    @Override
    public void setAlpha(int alpha) {
        // not supported
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // not supported
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setCornerRadius(float cornerRadius) {
        mRadius = cornerRadius;
        invalidateSelf();
    }

    public void setColor(ColorStateList color) {
        mBackgroundColor = color;
        mPaint.setColor(color.getColorForState(getState(), Color.WHITE));
        invalidateSelf();
    }

    public void setBoundSize(float boundSize) {
        mBoundSize = boundSize;
        updateBounds();
        invalidateSelf();
    }

    public float getBoundSize() {
        return mBoundSize;
    }

    public void setBoundColor(ColorStateList color) {
        mBoundColor = color;
        mBoundPaint.setColor(color.getColorForState(getState(), Color.WHITE));
        invalidateSelf();
    }
}
