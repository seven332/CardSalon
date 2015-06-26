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
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class CardView extends FrameLayout implements CardViewDelegate {

    private CardHelper mCardHelper;

    public CardView(Context context) {
        super(context);
        init(context, null);
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mCardHelper = new CardHelper(this);
        mCardHelper.initialize(context, attrs);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setBackgroundDrawable(Drawable drawable) {
        throw new IllegalStateException("Can't set background drawable directly.");
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        mCardHelper.setPadding(left, top, right, bottom);
    }

    @Override
    public void setRawPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
    }

    @Override
    public void setRawBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
    }

    @Override
    public void setCardRadius(float radius) {
        mCardHelper.setCardRadius(radius);
    }

    @Override
    public float getCardRadius() {
        return mCardHelper.getCardRadius();
    }

    @Override
    public void setCardBackgroundColor(int color) {
        mCardHelper.setCardBackgroundColor(color);
    }

    @Override
    public int getCardBackgroundColor() {
        return mCardHelper.getCardBackgroundColor();
    }

    @Override
    public void setCardBoundSize(float size) {
        mCardHelper.setCardBoundSize(size);
    }

    @Override
    public float getCardBoundSize() {
        return mCardHelper.getCardBoundSize();
    }

    @Override
    public void setCardBoundColor(int color) {
        mCardHelper.setCardBoundColor(color);
    }

    @Override
    public int getCardBoundColor() {
        return mCardHelper.getCardBoundColor();
    }

    @Override
    public void setCardElevation(float elevation) {
        mCardHelper.setCardElevation(elevation);
    }

    @Override
    public float getCardElevation() {
        return mCardHelper.getCardElevation();
    }
}
