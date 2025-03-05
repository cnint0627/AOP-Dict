package org.example;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DictManager {
    private static volatile Map<String, Map<Integer, String>> dictMap;
    private static void initialize() {
        synchronized (DictManager.class) {
            if (dictMap == null) {
                dictMap = new ConcurrentHashMap<>();
            }
        }
    }
    public static void addDictItem(String dictName, int key, String value) {
        if (dictMap == null) {
            initialize();
        }
        Map<Integer, String> dict = dictMap.get(dictName);
        if (dict == null) {
            dict = new ConcurrentHashMap<>();
            dictMap.put(dictName, dict);
        }
        dict.put(key, value);
    }
    public static String getDictItem(String dictName, int key) {
        if (dictMap == null) {
            return null;
        }
        Map<Integer, String> dict = dictMap.get(dictName);
        if (dict == null) {
            return null;
        }
        return dict.get(key);
    }
}
