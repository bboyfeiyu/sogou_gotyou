
package com.umeng.findyou.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class SharePrefUtil {

    /**
     * </br>城市名
     */
    public static void saveCity(Context context, String city) {
        if (null != context && !TextUtils.isEmpty(city)) {
            SharedPreferences sharePref =
                    context.getSharedPreferences(Constants.SHARE_PREF, Context.MODE_PRIVATE);
            sharePref.edit().putString(Constants.CITY_KEY, city).commit();
        }
    }

    /**
     * </br>获取城市名
     */
    public static String getCity(Context context) {
        String city = "";
        if (null != context) {
            SharedPreferences sharePref =
                    context.getSharedPreferences(Constants.SHARE_PREF, Context.MODE_PRIVATE);
            city = sharePref.getString(Constants.CITY_KEY, "");
        }
        return city;
    }

}
