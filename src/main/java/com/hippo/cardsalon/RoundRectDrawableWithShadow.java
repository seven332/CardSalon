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

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import com.hippo.yorozuya.MathUtils;

/**
 * A rounded rectangle drawable which also includes a shadow around.
 */
class RoundRectDrawableWithShadow extends Drawable {

    /**
     * This helper is set by CardView implementations.
     * <p>
     * Prior to API 17, canvas.drawRoundRect is expensive; which is why we need this interface
     * to draw efficient rounded rectangles before 17.
     */
    static RoundRectHelper sRoundRectHelper;

    private static int SHADOW_START_COLOR = 0x32000000;
    private static int SHADOW_HALF_COLOR = 0x19000000;
    private static int SHADOW_QUARTER_COLOR = 0x0C000000;
    private static int SHADOW_END_COLOR = Color.TRANSPARENT;

    private static float SHADOW_HALF_SCALE = 3f/8f;
    private static float SHADOW_QUARTER_SCALE = 5f/8f;

    private static float SHADOW_LEFT_SCALE = 0.9f;
    private static float SHADOW_TOP_SCALE = 0.55f;
    private static float SHADOW_RIGHT_SCALE = 0.55f;
    private static float SHADOW_BOTTOM_SCALE = 1f;

    private ColorStateList mBackgroundColor;
    private ColorStateList mBoundColor;

    private int mCurrentBackgroundColor;
    private int mCurrentBoundColor;

    private final Paint mPaint;
    private final Paint mBoundPaint;
    private final Paint mCornerShadowPaint;
    private final Paint mEdgeShadowPaint;

    private final RectF mBoundRect;
    private final RectF mInnerRect;
    private final Path mCornerShadowPath;

    private float mCornerRadius;
    private float mBoundSize;
    private float mElevation;

    private boolean mDirty = true;

    private final RectF mTempInnerRectF;
    private final RectF mTempOuterRectF;

    public RoundRectDrawableWithShadow(ColorStateList backgroundColor, float radius,
            ColorStateList boundColor, float boundSize, float elevation) {
        mBackgroundColor = backgroundColor;
        mBoundColor = boundColor;

        mCurrentBackgroundColor = Color.WHITE;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(mCurrentBackgroundColor);

        mCurrentBoundColor = Color.WHITE;
        mBoundPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mBoundPaint.setColor(mCurrentBoundColor);

        mCornerShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mCornerShadowPaint.setStyle(Paint.Style.FILL);

        mEdgeShadowPaint = new Paint(mCornerShadowPaint);
        mEdgeShadowPaint.setAntiAlias(false);

        mBoundRect = new RectF();
        mInnerRect = new RectF();
        mCornerShadowPath = new Path();

        mCornerRadius = radius;
        mBoundSize = boundSize;
        mElevation = elevation * 2;

        mTempInnerRectF = new RectF();
        mTempOuterRectF = new RectF();
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

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        updateBounds();
    }

    private void updateBounds() {
        mDirty = true;
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
        drawShadow(canvas);
        if (isDrawBounds()) {
            sRoundRectHelper.drawRoundRect(canvas, mBoundRect, mCornerRadius, mBoundPaint);
            sRoundRectHelper.drawRoundRect(canvas, mInnerRect, mCornerRadius, mPaint);
        } else {
            sRoundRectHelper.drawRoundRect(canvas, mBoundRect, mCornerRadius, mPaint);
        }
    }

    private void drawShadow(Canvas canvas) {
        int width = getBounds().width();
        int height = getBounds().height();
        float cornerRadius = mCornerRadius;
        float elevation = mElevation;
        float totalRadius = cornerRadius + elevation;
        final boolean drawHorizontalEdges = getBounds().width() - 2 * totalRadius > 0;
        final boolean drawVerticalEdges = getBounds().height() - 2 * totalRadius > 0;
        // LT
        int saved = canvas.save();
        canvas.translate(totalRadius, totalRadius);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        if (drawHorizontalEdges) {
            canvas.drawRect(0, -totalRadius, width - (totalRadius * 2), 0,
                    mEdgeShadowPaint);
        }
        canvas.restoreToCount(saved);
        // RB
        saved = canvas.save();
        canvas.translate(width - totalRadius, height - totalRadius);
        canvas.rotate(180f);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        if (drawHorizontalEdges) {
            canvas.drawRect(0, -totalRadius, width - (totalRadius * 2), 0,
                    mEdgeShadowPaint);
        }
        canvas.restoreToCount(saved);
        // LB
        saved = canvas.save();
        canvas.translate(totalRadius, height - totalRadius);
        canvas.rotate(270f);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        if (drawVerticalEdges) {
            canvas.drawRect(0, -totalRadius, height - (totalRadius * 2), 0,
                    mEdgeShadowPaint);
        }
        canvas.restoreToCount(saved);
        // RT
        saved = canvas.save();
        canvas.translate(width - totalRadius, totalRadius);
        canvas.rotate(90f);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        if (drawVerticalEdges) {
            canvas.drawRect(0, -totalRadius, height - (totalRadius * 2), 0,
                    mEdgeShadowPaint);
        }
        canvas.restoreToCount(saved);
    }

    private void buildShadowCorners() {
        float cornerRadius = mCornerRadius;
        float elevation = mElevation;
        RectF innerBounds = mTempInnerRectF;
        RectF outerBounds = mTempOuterRectF;

        innerBounds.set(-cornerRadius, -cornerRadius, cornerRadius, cornerRadius);
        outerBounds.set(innerBounds);
        outerBounds.inset(-elevation, -elevation);

        mCornerShadowPath.reset();
        mCornerShadowPath.setFillType(Path.FillType.EVEN_ODD);
        mCornerShadowPath.moveTo(-mCornerRadius, 0);
        mCornerShadowPath.rLineTo(-mElevation, 0);
        // outer arc
        mCornerShadowPath.arcTo(outerBounds, 180f, 90f, false);
        // inner arc
        mCornerShadowPath.arcTo(innerBounds, 270f, -90f, false);
        mCornerShadowPath.close();
        float startRatio = cornerRadius / (cornerRadius + elevation);
        mCornerShadowPaint.setShader(new RadialGradient(0, 0, cornerRadius + elevation,
                new int[]{SHADOW_START_COLOR, SHADOW_START_COLOR, SHADOW_HALF_COLOR,
                        SHADOW_QUARTER_COLOR, SHADOW_END_COLOR},
                new float[]{0f, startRatio, MathUtils.lerp(startRatio, 1f, SHADOW_HALF_SCALE),
                        MathUtils.lerp(startRatio, 1f, SHADOW_QUARTER_SCALE), 1f}, Shader.TileMode.CLAMP));

        // we offset the content shadowSize/2 pixels up to make it more realistic.
        // this is why edge shadow shader has some extra space
        // When drawing bottom edge shadow, we use that extra space.
        mEdgeShadowPaint.setShader(new LinearGradient(0, -cornerRadius + elevation, 0,
                -cornerRadius - elevation,
                new int[]{SHADOW_START_COLOR, SHADOW_START_COLOR, SHADOW_HALF_COLOR,
                        SHADOW_QUARTER_COLOR, SHADOW_END_COLOR},
                new float[]{0f, 0.5f, MathUtils.lerp(0.5f, 1f, SHADOW_HALF_SCALE),
                        MathUtils.lerp(0.5f, 1f, SHADOW_QUARTER_SCALE), 1f}, Shader.TileMode.CLAMP));
        mEdgeShadowPaint.setAntiAlias(false);
    }

    private void buildComponents(Rect bounds) {
        float elevation = mElevation;
        mBoundRect.left = bounds.left + (elevation * SHADOW_LEFT_SCALE);
        mBoundRect.top = bounds.top + (elevation * SHADOW_TOP_SCALE);
        mBoundRect.right = bounds.right - (elevation * SHADOW_RIGHT_SCALE);
        mBoundRect.bottom = bounds.bottom - (elevation * SHADOW_BOTTOM_SCALE);

        mInnerRect.set(mBoundRect);
        mInnerRect.inset(mBoundSize, mBoundSize);

        buildShadowCorners();
    }

    float getExtraPaddingLeft() {
        return mElevation * SHADOW_LEFT_SCALE;
    }

    float getExtraPaddingTop() {
        return mElevation * SHADOW_TOP_SCALE;
    }

    float getExtraPaddingRight() {
        return mElevation * SHADOW_RIGHT_SCALE;
    }

    float getExtraPaddingBottom() {
        return mElevation * SHADOW_BOTTOM_SCALE;
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
        mCornerRadius = cornerRadius;
        updateBounds();
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

    public void setElevation(float elevation) {
        mElevation = elevation;
        updateBounds();
        invalidateSelf();
    }

    public float getElevation() {
        return mElevation;
    }

    interface RoundRectHelper {
        void drawRoundRect(Canvas canvas, RectF bounds, float cornerRadius, Paint paint);
    }
}
