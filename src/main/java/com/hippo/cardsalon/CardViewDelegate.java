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

import android.graphics.drawable.Drawable;

/**
 * Interface provided by CardView to implementations.
 * <p>
 * Necessary to resolve circular dependency between base CardView and platform implementations.
 */
interface CardViewDelegate {

    void setRawPadding(int left, int top, int right, int bottom);

    void setRawBackgroundDrawable(Drawable drawable);

    void setCardRadius(float radius);

    float getCardRadius();

    void setCardBackgroundColor(int color);

    int getCardBackgroundColor();

    void setCardBoundSize(float size);

    float getCardBoundSize();

    void setCardBoundColor(int color);

    int getCardBoundColor();

    void setCardElevation(float elevation);

    float getCardElevation();
}
