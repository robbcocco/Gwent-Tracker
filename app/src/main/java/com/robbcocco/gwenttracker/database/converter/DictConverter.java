package com.robbcocco.gwenttracker.database.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by robb on 17/12/17.
 */

public class DictConverter {
    @TypeConverter
    public static Map<String, String> toDict(String string) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        return gson.fromJson(string, type);
    }

    @TypeConverter
    public static String toString(Map<String, String> dict) {
        Gson gson = new Gson();
        return gson.toJson(dict);
    }
}
