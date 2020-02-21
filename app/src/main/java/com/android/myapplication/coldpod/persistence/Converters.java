package com.android.myapplication.coldpod.persistence;

import androidx.room.TypeConverter;

import com.android.myapplication.coldpod.network.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Converters {
    @TypeConverter
    public static List<Item> toItemList(String itemString) {
        if (itemString == null) {
            return Collections.emptyList();
        }
        // Create a Gson instance
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Item>>() {}.getType();
        // Deserializes the specified Json into the list of items
        return gson.fromJson(itemString, listType);
    }

    @TypeConverter
    public static String toItemString(List<Item> itemList) {
        if (itemList == null) {
            return null;
        }
        // Create a Gson instance
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Item>>() {}.getType();
        // Serializes the list of items into its equivalent Json representation
        return gson.toJson(itemList, listType);
    }

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
