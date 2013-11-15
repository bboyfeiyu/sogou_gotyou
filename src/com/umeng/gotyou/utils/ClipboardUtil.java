
package com.umeng.gotyou.utils;

import android.content.Context;
import android.text.ClipboardManager;

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
     * @Description:
     * @param context
     * @return
     * @throws
     */
    public static String getContent(Context context) {
        ClipboardManager cbm = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        return cbm.getText().toString().trim();
    }

}
