
package com.umeng.findyou.activities;

import android.app.Activity;
import android.os.Bundle;

import com.umeng.findyou.R;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: UserManualActivity.java
 * @Package com.umeng.findyou.activities
 * @Description: 用户手册，第一次使用该应用时的手册. 参考
 *               http://www.cnblogs.com/Amandaliu/archive/2012
 *               /12/03/2800072.html
 * @author Honghui He
 * @version V1.0
 */

public class GuideActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_activity);
    }
}
