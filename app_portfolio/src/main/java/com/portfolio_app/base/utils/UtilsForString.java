package com.portfolio_app.base.utils;

import android.text.Html;
import android.text.Spanned;

/**
 * @author Stefan Wyszynski
 */
public class UtilsForString {
    public static boolean isNullOrEmpty(String updateApkLink) {
        return (updateApkLink == null || updateApkLink.length() == 0);
    }

    public static String getTextFromHtml(String htmltext) {
        if (UtilsForString.isNullOrEmpty(htmltext)) {
            return null;
        }
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(htmltext, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(htmltext);
        }
        return result.toString();
    }
}
