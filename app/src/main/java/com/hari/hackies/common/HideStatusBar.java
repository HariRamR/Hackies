package com.hari.hackies.common;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class HideStatusBar {

    public static void hideStatusBar(Activity activity){
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
