package com.umeng.findyou.dialog;

import android.app.Dialog;
import android.content.Context;

import com.umeng.findyou.R;

/**   
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved 
 *
 * @Title: NavgationDialog.java
 * @Package com.umeng.gotme.activities
 * @Description: 
 *
 * @author Honghui He  
 * @version V1.0   
 */

public class NavgationDialog extends Dialog {

    public NavgationDialog(Context context) {
        super(context);
        getWindow().setWindowAnimations(R.anim.dialog_anim);
    }

    public NavgationDialog(Context context, int theme) {
        super(context, theme);
    }

    public NavgationDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

}
