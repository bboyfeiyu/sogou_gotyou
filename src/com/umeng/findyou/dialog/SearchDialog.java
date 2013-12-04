
package com.umeng.findyou.dialog;

import android.app.Dialog;
import android.content.Context;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: SearchDialog.java
 * @Package com.umeng.findyou.dialog
 * @Description: 用于搜索的Dialog， 包含公交搜索、POI搜索
 * @author Honghui He
 * @version V1.0
 */

public class SearchDialog extends Dialog {

    public SearchDialog(Context context) {
        super(context);
    }

    public SearchDialog(Context context, int theme) {
        super(context, theme);
    }

    public SearchDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

}
