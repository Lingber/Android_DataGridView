package com.lingber.mycontrol.datagridview.i18n.Util;

import android.view.View;
import android.view.ViewGroup;

import com.lingber.mycontrol.datagridview.i18n.ILanguageView;

public class ViewUtil {
    public static void updateViewLanguage(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            int count = vg.getChildCount();
            for (int i = 0; i < count; i++) {
                updateViewLanguage(vg.getChildAt(i));
            }
        } else if (view instanceof ILanguageView) {
            ILanguageView tv = (ILanguageView) view;
            tv.reLoadLanguage();
        }
    }
}
