
package com.umeng.findyou.dialog;

import android.app.Dialog;
import android.content.Context;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: RouteDetailDialog.java
 * @Package com.umeng.findyou.dialog
 * @Description: 路线详情、路线选择的Dialog
 * @author Honghui He
 * @version V1.0
 */

public class RouteDialog extends Dialog {

    /**
     * @Title: RouteDialog
     * @Description: RouteDialog Constructor
     * @param context
     */
    public RouteDialog(Context context) {
        super(context);
    }

    /**
     * @Title: RouteDialog
     * @Description: RouteDialog Constructor
     * @param context
     * @param theme
     */
    public RouteDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * @Title: RouteDialog
     * @Description: RouteDialog Constructor
     * @param context
     * @param cancelable
     * @param cancelListener
     */
    public RouteDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

}
