
package com.umeng.findyou.utils;

import android.content.Context;
import android.text.ClipboardManager;
import android.text.TextUtils;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: BoardUtil.java
 * @Package com.umeng.gotme.utils
 * @Description:
 * @author Honghui He
 * @version V1.0
 */

public class ClipboardUtil {

    /**
     * @Title: getContent
     * @Description: 获取剪切板中的内容
     * @param context
     * @return
     * @throws
     */
    public static String getContent(Context context) {
        if (context == null) {
            return "";
        }
        ClipboardManager cbm = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        String text = "";
        if ( cbm != null && !TextUtils.isEmpty( cbm.getText()) ) {
            text = cbm.getText().toString().trim() ;
        }
        return text;
    }

}
