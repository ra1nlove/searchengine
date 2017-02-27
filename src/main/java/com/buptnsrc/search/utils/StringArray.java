package com.buptnsrc.search.utils;

import com.buptnsrc.search.resource.WebPage;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by rain on 17-2-23.
 */
public class StringArray {
    public static String[] toStringArray(Collection<WebPage.Field> fields) {
        String[] arr = new String[fields.size()];
        Iterator<WebPage.Field> iter = fields.iterator();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = iter.next().getName();
        }
        return arr;
    }
}
