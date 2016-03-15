package com.nasahapps.bottomnav;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.DimenRes;
import android.util.TypedValue;

/**
 * Created by Hakeem on 3/15/16.
 */
class Utils {

    public static boolean isTablet(Context c) {
        return c.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }

    public static boolean isPortrait(Context c) {
        return c.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static float getFloatResource(Context c, @DimenRes int res) {
        TypedValue tv = new TypedValue();
        c.getResources().getValue(res, tv, true);
        return tv.getFloat();
    }

}
