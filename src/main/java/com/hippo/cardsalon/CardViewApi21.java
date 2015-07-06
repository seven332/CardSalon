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
import android.content.Context;
import android.os.Build;
import android.view.View;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class CardViewApi21 implements CardViewImpl {

    @Override
    public void initStatic() {
    }

    @Override
    public void initialize(CardHelper cardHelper, Context context, int backgroundColor,
            float radius, int boundColor, float boundSize, float elevation) {
        final RoundRectDrawable backgroundDrawable =
                new RoundRectDrawable(backgroundColor, radius, boundColor, boundSize);
        cardHelper.setBackgroundDrawable(backgroundDrawable);

        CardViewDelegate delegate = cardHelper.getDelegate();
        delegate.setRawBackgroundDrawable(backgroundDrawable);

        View view = (View) delegate;
        view.setBackground(backgroundDrawable);
        view.setClipToOutline(true);
        view.setElevation(elevation);
    }

    @Override
    public void updatePadding(CardHelper cardHelper) {
        int boundSize = Math.round(((RoundRectDrawable) cardHelper.getBackgroundDrawable()).getBoundSize());
        CardViewDelegate delegate = cardHelper.getDelegate();
        delegate.setPadding(cardHelper.getOriginalPaddingLeft() + boundSize,
                cardHelper.getOriginalPaddingTop() + boundSize,
                cardHelper.getOriginalPaddingRight() + boundSize,
                cardHelper.getOriginalPaddingBottom() + boundSize);
    }

    @Override
    public void setCornerRadius(CardHelper cardHelper, float radius) {
        RoundRectDrawableWithShadow drawable = (RoundRectDrawableWithShadow) cardHelper.getBackgroundDrawable();
        drawable.setCornerRadius(radius);
    }

    @Override
    public void setBackgroundColor(CardHelper cardHelper, int color) {
        RoundRectDrawableWithShadow drawable = (RoundRectDrawableWithShadow) cardHelper.getBackgroundDrawable();
        drawable.setColor(color);
    }

    @Override
    public void setBoundSize(CardHelper cardHelper, float size) {
        RoundRectDrawableWithShadow drawable = (RoundRectDrawableWithShadow) cardHelper.getBackgroundDrawable();
        drawable.setBoundSize(size);
    }

    @Override
    public void setBoundColor(CardHelper cardHelper, int color) {
        RoundRectDrawableWithShadow drawable = (RoundRectDrawableWithShadow) cardHelper.getBackgroundDrawable();
        drawable.setBoundColor(color);
    }

    @Override
    public void setElevation(CardHelper cardHelper, float elevation) {
        ((View) cardHelper.getDelegate()).setElevation(elevation);
    }
}
