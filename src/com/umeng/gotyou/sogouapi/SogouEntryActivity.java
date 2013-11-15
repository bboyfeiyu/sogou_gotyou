
package com.umeng.gotyou.sogouapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sogou.inputmethod.sdk.SogouAPI;
import com.umeng.gotyou.activities.MainActivity;

public class SogouEntryActivity extends Activity {

    public static final int REQUEST_CODE = 101;
    public static final String APP_RESULT_CONTENT_TAG = "APP_RESULT_CONTENT";

    private SogouAPI mSogouAPI;

    /**
     * (非 Javadoc)
     * 
     * @Title: onCreate
     * @Description:
     * @param savedInstanceState
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSogouAPI = new SogouAPI(this);
        gotoMainActivity();
    }

    /**
     * @Title: gotoMainActivity
     * @Description: 跳转到mainActivity
     * @throws
     */
    private void gotoMainActivity() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * (非 Javadoc)
     * @Title: onActivityResult
     * @Description: 
     * 
     * @param requestCode
     * @param resultCode
     * @param data
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        String result = data.getStringExtra(APP_RESULT_CONTENT_TAG);
                        if (result != null) {
                            mSogouAPI.sendResult(result);
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

        this.finish();
    }

}
