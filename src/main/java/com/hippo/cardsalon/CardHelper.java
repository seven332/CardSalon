package com.hippo.cardsalon;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Base on cardview-v7 22.2.0
 */
public class CardHelper {

    private static final CardViewImpl NO_ELEVATION_IMPL;
    private static final CardViewImpl IMPL;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            IMPL = new CardViewApi21();
            NO_ELEVATION_IMPL = new CardViewJellybeanMr1();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            IMPL = new CardViewJellybeanMr1();
            NO_ELEVATION_IMPL = IMPL;
        } else {
            IMPL = new CardViewEclairMr1();
            NO_ELEVATION_IMPL = IMPL;
        }
        IMPL.initStatic();
        NO_ELEVATION_IMPL.initStatic();
    }

    private CardViewDelegate mDelegate;
    private CardViewImpl mImpl;

    private Drawable mBackgroundDrawable;

    private int mBackgroundColor;
    private float mRadius;
    private int mBoundColor;
    private float mBoundSize;
    private float mElevation;

    private int mOriginalPaddingLeft;
    private int mOriginalPaddingTop;
    private int mOriginalPaddingRight;
    private int mOriginalPaddingBottom;

    public CardHelper(CardViewDelegate delegate) {
        mDelegate = delegate;
        if (!(delegate instanceof View)) {
            throw new IllegalStateException("CardViewDelegate must be a view");
        }
    }

    public void initialize(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CardHelper);

        mImpl = a.getBoolean(R.styleable.CardHelper_forceNoElevation, false) ? NO_ELEVATION_IMPL : IMPL;
        mBackgroundColor = a.getColor(R.styleable.CardHelper_cardBackgroundColor, Color.WHITE);
        mRadius = a.getDimension(R.styleable.CardHelper_cardCornerRadius,
                context.getResources().getDimension(R.dimen.default_card_corner_radius));
        mBoundColor = a.getColor(R.styleable.CardHelper_cardBoundColor, Color.TRANSPARENT);
        mBoundSize = a.getDimension(R.styleable.CardHelper_cardBoundSize, 0f);
        mElevation = a.getDimension(R.styleable.CardHelper_cardElevation,
                context.getResources().getDimension(R.dimen.default_card_elevation));
        mImpl.initialize(this, context, mBackgroundColor, mRadius, mBoundColor, mBoundSize, mElevation);
        initPadding();

        a.recycle();
    }

    private void initPadding() {
        View view = (View) mDelegate;
        mOriginalPaddingLeft = view.getPaddingLeft();
        mOriginalPaddingTop = view.getPaddingTop();
        mOriginalPaddingRight = view.getPaddingRight();
        mOriginalPaddingBottom = view.getPaddingBottom();

        mImpl.updatePadding(this);
    }

    CardViewDelegate getDelegate() {
        return mDelegate;
    }

    void setBackgroundDrawable(Drawable drawable) {
        mBackgroundDrawable = drawable;
    }

    Drawable getBackgroundDrawable() {
        return mBackgroundDrawable;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        mOriginalPaddingLeft = left;
        mOriginalPaddingTop = top;
        mOriginalPaddingRight = right;
        mOriginalPaddingBottom = bottom;

        if (mImpl != null) {
            mImpl.updatePadding(this);
        }
    }

    int getOriginalPaddingLeft() {
        return mOriginalPaddingLeft;
    }

    int getOriginalPaddingTop() {
        return mOriginalPaddingTop;
    }

    int getOriginalPaddingRight() {
        return mOriginalPaddingRight;
    }

    int getOriginalPaddingBottom() {
        return mOriginalPaddingBottom;
    }

    public void setCardRadius(float radius) {
        if (mRadius != radius) {
            mRadius = radius;
            mImpl.setCornerRadius(this, radius);
        }
    }

    public float getCardRadius() {
        return mRadius;
    }

    public void setCardBackgroundColor(int color) {
        if (mBackgroundColor != color) {
            mBackgroundColor = color;
            mImpl.setBackgroundColor(this, color);
        }
    }

    public int getCardBackgroundColor() {
        return mBackgroundColor;
    }

    public void setCardBoundSize(float size) {
        if (mBoundSize != size) {
            mBoundSize = size;
            mImpl.setBoundSize(this, size);
        }
    }

    public float getCardBoundSize() {
        return mBoundSize;
    }

    public void setCardBoundColor(int color) {
        if (mBoundColor != color) {
            mBoundColor = color;
            mImpl.setBoundColor(this, color);
        }
    }

    public int getCardBoundColor() {
        return mBoundColor;
    }

    public void setCardElevation(float elevation) {
        if (mElevation != elevation) {
            mElevation = elevation;
            mImpl.setElevation(this, elevation);
        }
    }

    public float getCardElevation() {
        return mElevation;
    }
}
