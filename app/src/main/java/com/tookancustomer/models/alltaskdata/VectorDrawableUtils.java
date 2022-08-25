package com.tookancustomer.models.alltaskdata;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

public final class VectorDrawableUtils {

    @Nullable
    public final Drawable getDrawable(@NotNull Context context, @DrawableRes int drawableRes) {
        return (Drawable) VectorDrawableCompat.create(context.getResources(), drawableRes, context.getTheme());
    }

    public final Drawable getDrawable(Context context, int drawableRes, int color) {
        Drawable var = getDrawable(context, drawableRes);
        Drawable drawable = var;
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable;
    }
}

