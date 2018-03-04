package com.robbcocco.gwenttracker.database.converter;

import android.arch.persistence.room.TypeConverter;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rober on 3/3/2018.
 */

public class URLConverter {
    @TypeConverter
    public static URL toURL(String string) {
        if (string.equals("")) {
            return null;
        }
        try {
            return new URL(string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TypeConverter
    public static String toString(URL url) {
        if (url != null) {
            return url.toString();
        }
        return "";
    }
}
