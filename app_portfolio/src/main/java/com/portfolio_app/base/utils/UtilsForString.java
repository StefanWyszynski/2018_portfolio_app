package com.portfolio_app.base.utils;

import android.text.Html;
import android.text.Spanned;

/*
 * Copyright 2018, The Portfolio project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
