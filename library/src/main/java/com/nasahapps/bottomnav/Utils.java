package com.nasahapps.bottomnav;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

/**
 * Created by Hakeem on 3/15/16.
 */
class Utils {

    public static boolean isTablet(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            return c.getResources().getConfiguration().smallestScreenWidthDp >= 600;
        } else {
            boolean xlarge = ((c.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                    == Configuration.SCREENLAYOUT_SIZE_XLARGE);
            boolean large = ((c.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                    == Configuration.SCREENLAYOUT_SIZE_LARGE);
            return xlarge || large;
        }
    }

    public static boolean isPortrait(Context c) {
        return c.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

}
