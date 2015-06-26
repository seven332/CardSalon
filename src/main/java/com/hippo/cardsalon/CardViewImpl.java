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

/**
 * Interface for platform specific CardView implementations.
 */
interface CardViewImpl {

    void initStatic();

    void initialize(CardHelper cardHelper, Context context, int backgroundColor,
            float radius, int boundColor, float boundSize, float elevation);

    void updatePadding(CardHelper cardHelper);

    void setCornerRadius(CardHelper cardHelper, float radius);

    void setBackgroundColor(CardHelper cardHelper, int color);

    void setBoundSize(CardHelper cardHelper, float size);

    void setBoundColor(CardHelper cardHelper, int color);

    void setElevation(CardHelper cardHelper, float elevation);
}
