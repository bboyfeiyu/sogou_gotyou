
package com.umeng.findyou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.ListView;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: DetailDialog.java
 * @Package
 * @Description:
 * @author Honghui He
 * @version V1.0
 */

public class DetailDialog extends Dialog {

    /**
     * 路线详情
     */
    private ListView mListView = null;

    /**
     * @Title: DetailDialog
     * @Description: DetailDialog Constructor
     * @param context
     */
    public DetailDialog(Context context) {
        super(context);
    }

    public DetailDialog(Context context, int theme) {
        super(context, theme);
    }

    public DetailDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

}
